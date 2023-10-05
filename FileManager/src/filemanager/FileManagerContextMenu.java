package filemanager;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@SuppressWarnings("serial")
public class FileManagerContextMenu extends JPopupMenu {
    private FileManagerList list;

    public FileManagerContextMenu(FileManagerList list) {
        this.list = list;

        // Create the menu items
        JMenuItem renameItem = new JMenuItem("Rename");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("paste");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem runItem = new JMenuItem("Run");

        // Add action listeners to the menu items
        renameItem.addActionListener(new RenameActionListener());
        copyItem.addActionListener(new CopyActionListener());
        pasteItem.addActionListener(new PasteActionListener());
        deleteItem.addActionListener(new DeleteActionListener());

        // Add the menu items to the context menu
        add(renameItem);
        add(copyItem);
        add(pasteItem);
        add(deleteItem);
        add(runItem);
    }

    private class RenameActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedItem = list.getSelectedValue();
            if (selectedItem != null && !selectedItem.equals("..")) {
                // Open a dialog to get the new name and perform the rename operation
                String newName = FileManagerDialog.showRenameDialog(list.getFolderPath(), selectedItem);
                if (newName != null && !newName.isEmpty()) {
                    File oldFile = new File(list.getFolderPath() + File.separator + selectedItem);
                    File newFile = new File(newName);
                    boolean success = oldFile.renameTo(newFile);

                    if (success) {
                        // Update the list to reflect the changes
                        list.populateList(list.getFolderPath());
                    } else {
                        // Show an error message if the rename operation fails
                        JOptionPane.showMessageDialog(null, "Failed to rename the file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    private class CopyActionListener implements ActionListener {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        String selectedItem = list.getSelectedValue();
	        if (selectedItem != null && !selectedItem.equals("..")) {
	            // Open a dialog to get the destination folder for the copy operation
	            String destinationFolder = FileManagerDialog.showCopyDialog(list.getFolderPath());
	            if (destinationFolder != null && !destinationFolder.isEmpty()) {
	                // Perform the copy operation
	                File sourceFile = new File(list.getFolderPath() + File.separator + selectedItem);
	                File destinationFile = new File(destinationFolder + File.separator + selectedItem);

	                try {
	                    Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
	                    // Update the list to reflect the changes
	                    list.populateList(destinationFolder);
	                } catch (IOException ex) {
	                    // Show an error message if the copy operation fails
	                    JOptionPane.showMessageDialog(null, "Failed to copy the file.", "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        }
	    }
	}

	private class PasteActionListener implements ActionListener {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        String selectedItem = list.getSelectedValue();
	        if (selectedItem != null && !selectedItem.equals("..")) {
	            // Open a dialog to get the destination folder for the paste operation
	            String destinationFolder = FileManagerDialog.showPasteDialog(list.getFolderPath());
	            if (destinationFolder != null && !destinationFolder.isEmpty()) {
	                // Perform the paste operation
	                File sourceFile = new File(list.getFolderPath() + File.separator + selectedItem);
	                File destinationFile = new File(destinationFolder + File.separator + selectedItem);

	                try {
	                    Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
	                    // Update the list to reflect the changes
	                    list.populateList(destinationFolder);
	                } catch (IOException ex) {
	                    // Show an error message if the paste operation fails
	                    JOptionPane.showMessageDialog(null, "Failed to paste the file.", "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        }
	    }
	}


    private class DeleteActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Handle delete action here
            String selectedItem = list.getSelectedValue();
            if (selectedItem != null && !selectedItem.equals("..")) {
                // Open a confirmation dialog before performing the delete operation
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this file?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Perform the delete operation
                    File fileToDelete = new File(list.getFolderPath() + File.separator + selectedItem);
                    boolean success = fileToDelete.delete();

                    if (success) {
                        // Update the list to reflect the changes
                        list.populateList(list.getFolderPath());
                    } else {
                        // Show an error message if the delete operation fails
                        JOptionPane.showMessageDialog(null, "Failed to delete the file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}
