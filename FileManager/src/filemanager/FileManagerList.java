package filemanager;

import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.Desktop;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class FileManagerList extends JList<String> {
    private boolean simpleView = true; // Flag to keep track of the current view mode
    private String currentFolderPath;
    // Drag-and-drop variables
    private final DragSource dragSource = DragSource.getDefaultDragSource();
    private final FileManagerTransferHandler transferHandler = new FileManagerTransferHandler();
    public void setFileManagerFrame(FileManagerFrame fileManagerFrame) {
    }

    public FileManagerList() {
        // Set up the list to display files and subdirectories
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a ListSelectionListener to handle file selection
        addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                handleFileSelection();
            }
        });

        // Add a MouseListener to handle double-click action
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleFileDoubleClick();
                }
            }
        });

        // Set the transfer handler for drag-and-drop
        setTransferHandler(transferHandler);

        // Add a popup menu to the list
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem renameItem = new JMenuItem("Rename");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        JMenuItem deleteItem = new JMenuItem("Delete");
        popupMenu.add(renameItem);
        popupMenu.add(copyItem);
        popupMenu.add(pasteItem);
        popupMenu.add(deleteItem);

        // Add action listeners to the menu items
        pasteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pasteSelectedFile();
            }
        });

        // Add action listeners to the popup menu items
        renameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renameSelectedFile();
            }
        });

        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copySelectedFile();
            }
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedFile();
            }
        });

        // Set the popup menu for the list
        setComponentPopupMenu(popupMenu);
        // Set the drop mode and add the drop target
        setDropMode(DropMode.ON_OR_INSERT);
                
     // Enable drag gesture recognition for internal DnD
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, new FileManagerListDragGestureListener());
        
        // Set the transfer handler for drag-and-drop
        setTransferHandler(transferHandler);


  
    }
    
    // Implement the drag gesture recognition for internal DnD
    private class FileManagerListDragGestureListener implements DragGestureListener {
        @Override
        public void dragGestureRecognized(DragGestureEvent event) {
            // Get the selected file(s) from the list
            String[] selectedFiles = getSelectedValuesList().toArray(new String[0]);

            // Prepare the Transferable object
            Transferable transferable = new FileTransferHandler(selectedFiles);

            // Start the Drag-and-Drop operation
            event.startDrag(null, transferable);
        }
    }
    
    public abstract class FileManagerDropTargetAdapter extends DropTargetAdapter {
        public abstract void drop(File[] files, String destinationFolder);

        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                Transferable transferable = dtde.getTransferable();
                if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    @SuppressWarnings("unchecked")
					List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    String destinationFolder = ((FileManagerList) dtde.getDropTargetContext().getComponent()).getFolderPath();
                    drop(fileList.toArray(new File[0]), destinationFolder);
                    dtde.dropComplete(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                dtde.dropComplete(false);
            }
        }
    }

    private void handleFileSelection() {
        String selectedFile = getSelectedValue();
        if (selectedFile != null) {
            String filePath = currentFolderPath + File.separator + selectedFile;
            File file = new File(filePath);
            if (file.isFile()) {
                FileManagerMenuBar.setEnableFileMenuItems(true);
            } else {
                FileManagerMenuBar.setEnableFileMenuItems(false);
            }
        }
    }

    private void handleFileDoubleClick() {
        String selectedFile = getSelectedValue();
        if (selectedFile != null && !selectedFile.equals("..")) {
            String filePath = currentFolderPath + File.separator + selectedFile;
            File file = new File(filePath);
            if (file.isFile()) {
                openFileWithDefaultProgram(file);
            } else if (file.isDirectory()) {
                FileManagerFrame.getInstance().getTree().expandSelectedFolder();
            }
        }
    }

    private void openFileWithDefaultProgram(File file) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                try {
                    desktop.open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the error appropriately (e.g., show a message dialog)
                }
            } else {
                // Handle the case where the OPEN action is not supported by the desktop
                // (e.g., show a message dialog)
            }
        } else {
            // Handle the case where the desktop is not supported (e.g., show a message dialog)
        }
    }


    // Implement the methods for renaming, copying, and deleting files
    private void renameSelectedFile() {
        String selectedFile = getSelectedValue();
        if (selectedFile != null) {
            String filePath = currentFolderPath + File.separator + selectedFile;
            String newName = FileManagerDialog.showRenameDialog(currentFolderPath, selectedFile);
            if (newName != null) {
                try {
                    Path sourcePath = Paths.get(filePath);
                    Path destPath = Paths.get(currentFolderPath, newName);

                    Files.move(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
                    FileManagerMenuBar.showMessage("File renamed successfully.");
                    populateList(currentFolderPath); // Refresh the list to show the updated file names
                } catch (IOException e) {
                    e.printStackTrace();
                    FileManagerMenuBar.showError("Failed to rename the file.");
                }
            }
        }
    }

private void copySelectedFile() {
    String selectedFile = getSelectedValue();
    if (selectedFile != null) {
        String filePath = currentFolderPath + File.separator + selectedFile;
        String destinationPath = FileManagerDialog.showCopyDialog(currentFolderPath);
        if (destinationPath != null) {
            new File(filePath);
            File destFile = new File(destinationPath + File.separator + selectedFile);
            if (FileManagerUtil.copyFileOrFolder(filePath, destFile.getAbsolutePath())) {
                FileManagerMenuBar.showMessage("File copied successfully.");
            } else {
                FileManagerMenuBar.showError("Failed to copy the file.");
            }
        }
    }
}

private void pasteSelectedFile() {
    String selectedFile = getSelectedValue();
    if (selectedFile != null) {
        String filePath = currentFolderPath + File.separator + selectedFile;
        String destinationPath = FileManagerDialog.showPasteDialog(currentFolderPath);
        if (destinationPath != null) {
            new File(filePath);
            File destFile = new File(destinationPath + File.separator + selectedFile);
            if (FileManagerUtil.copyFileOrFolder(filePath, destFile.getAbsolutePath())) {
                FileManagerMenuBar.showMessage("File pasted successfully.");
            } else {
                FileManagerMenuBar.showError("Failed to paste the file.");
            }
        }
    }
}

   public  void deleteSelectedFile() {
        String selectedFile = getSelectedValue();
        if (selectedFile != null) {
            String filePath = currentFolderPath + File.separator + selectedFile;
            new File(filePath);
            int option = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this file?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                if (FileManagerUtil.deleteFileOrFolder(filePath)) {
                    FileManagerMenuBar.showMessage("File deleted successfully.");
                } else {
                    FileManagerMenuBar.showError("Failed to delete the file.");
                }
            }
        }
    }
    
    public void populateList(String folderPath) {
        // Get the list of files and subdirectories for the selected folder
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        // Check if the folder is empty
        if (files == null) {
            return;
        }

        // Sort the files and directories alphabetically
        Arrays.sort(files);

        // Create a list to hold the names of files and subdirectories
        List<String> itemList = new ArrayList<>();

        // Add ".." to go back to the parent folder if not already at the root
        if (!folderPath.equals(File.separator)) {
            itemList.add("..");
        }

        // Add each file and subdirectory name to the list
        for (File file : files) {
            if (simpleView) {
                // In simple view, show only the names of files and subdirectories
                itemList.add(file.getName());
            } else {
                // In detailed view, show additional information about each file
                if (file.isDirectory()) {
                    itemList.add("[Folder] " + file.getName());
                } else {
                    itemList.add("[File] " + file.getName() + " - Size: " + formatSize(file.length()));
                }
            }
        }

        // Set the list data with the items
        setListData(itemList.toArray(new String[0]));
        
        // Update the current folder path
        currentFolderPath = folderPath;
    }


    private String formatSize(long size) {
        // Helper method to format the file size for detailed view
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double sizeInDouble = size;

        while (sizeInDouble >= 1024 && unitIndex < units.length - 1) {
            sizeInDouble /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", sizeInDouble, units[unitIndex]);
    }


	public void populateListForDrive(String selectedDrive) {
        // Get the list of files and subdirectories for the selected drive
        File drive = new File(selectedDrive);
        File[] files = drive.listFiles();

        // Check if the drive is valid and not empty
        if (files == null || files.length == 0) {
            return;
        }

        // Sort the files and directories alphabetically
        Arrays.sort(files);

        // Create a list to hold the names of files and subdirectories
        List<String> itemList = new ArrayList<>();

        // Add each file and subdirectory name to the list
        for (File file : files) {
            itemList.add(file.getName());
        }

        // Set the list data with the items
        setListData(itemList.toArray(new String[0]));
    }
	
    public String getFolderPath() {
        return currentFolderPath;
    }

	public void setSimpleView() {
        if (!simpleView) {
            simpleView = true;
            populateList(currentFolderPath); // Refresh the list with the current folder path
        }
    }

    public void setDetailsView() {
        if (simpleView) {
            simpleView = false;
            populateList(currentFolderPath); // Refresh the list with the current folder path
        }
        
    }
    
    public DragSource getDragSource() {
		return dragSource;
	}

	// Drag-and-drop file copying
    private class FileManagerTransferHandler extends TransferHandler {
        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            Transferable transferable = support.getTransferable();
            try {
                @SuppressWarnings("unchecked")
				List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                String destinationFolder = getFolderPath();
                for (File file : fileList) {
                    String sourcePath = file.getAbsolutePath();
                    String destinationPath = destinationFolder + File.separator + file.getName();
                    FileManagerUtil.copyFileOrFolder(sourcePath, destinationPath);
                }
                populateList(destinationFolder); // Refresh the list to show the updated file names
                return true;
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return DnDConstants.ACTION_COPY;
        }
    }
}
