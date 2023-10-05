package filemanager;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("serial")
public class FileManagerTree extends JTree {
    private DefaultTreeModel treeModel;
    private FileManagerTree tree; // Add this variable

    public FileManagerTree() {
        initializeTree();
    }

    public void setFileManagerMenuBar(FileManagerMenuBar fileManagerMenuBar) {
    }

    public void setTree(FileManagerTree tree) {
        this.tree = tree;
    }

    private void initializeTree() {
        // Create the root node of the tree
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Drives");

        // Get the list of available drives
        File[] drives = File.listRoots();

        // Create child nodes for each drive
        for (File drive : drives) {
            DefaultMutableTreeNode driveNode = new DefaultMutableTreeNode(drive.getPath());
            rootNode.add(driveNode);
        }

        // Create the tree model and set it to the tree
        treeModel = new DefaultTreeModel(rootNode);
        setModel(treeModel);

        // Add a tree selection listener to handle folder selection events
        addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();

                // Check if a node is selected
                if (node == null) {
                    return;
                }

                // Get the path of the selected node
                String selectedPath = node.toString();

                // Populate the file list for the selected folder
                FileManagerList fileList = FileManagerUtil.getFileManagerListFromParent(FileManagerTree.this);
                if (fileList != null) {
                    fileList.populateList(selectedPath);
                }

                // Update the status bar with the current drive information
                FileManagerStatusBar statusBar = FileManagerUtil.getFileManagerStatusBarFromParent(FileManagerTree.this);
                if (statusBar != null) {
                    statusBar.updateStatus(selectedPath);
                }
            }
        });

        // Add a mouse listener to handle folder expansion on single-click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selRow = getRowForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    TreePath selPath = getPathForLocation(e.getX(), e.getY());
                    if (selPath != null && selPath.getLastPathComponent() != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                        if (!node.isLeaf()) {
                            if (isExpanded(selPath)) {
                                collapsePath(selPath);
                            } else {
                                expandPath(selPath);
                            }
                        }
                    }
                }
            }
        });

        // Expand the root node to show available drives
        expandRow(0);
        
     // Enable drag gesture recognition for internal DnD
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, new FileManagerTreeDragGestureListener());

    }
    
    // Implement the drag gesture recognition for internal DnD
    private class FileManagerTreeDragGestureListener implements DragGestureListener {
        public void dragGestureRecognized(DragGestureEvent event) {
            // Get the selected file(s) from the tree
            TreePath[] selectedPaths = getSelectionPaths();

            // Prepare the Transferable object
            Transferable transferable = new FileTransferHandler(selectedPaths);

            // Start the Drag-and-Drop operation
            event.startDrag(null, transferable);
        }
    }

    public void populateTree(String selectedDrive) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(selectedDrive);

        // Get the list of files and subdirectories for the selected drive
        File drive = new File(selectedDrive);
        File[] files = drive.listFiles();

        if (files != null) {
            // Sort the files in reverse order (descending)
            Arrays.sort(files, Comparator.reverseOrder());

            // Create child nodes for each file or directory in the drive
            for (File file : files) {
                DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(file.getName());
                rootNode.add(fileNode);

                // Recursively call the method for subdirectories
                if (file.isDirectory()) {
                    populateNode(fileNode, file);
                }
            }
        }

        // Create the tree model and set it to the tree
        treeModel = new DefaultTreeModel(rootNode);
        setModel(treeModel);
    }

    public void populateTreeForDrive(String drivePath) {
        File drive = new File(drivePath);

        // Check if the selected drive exists and is a directory
        if (drive.exists() && drive.isDirectory()) {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(drivePath);

            // Populate the root node with the files and subdirectories
            populateNode(rootNode, drive);

            // Create the tree model and set it to the tree
            treeModel = new DefaultTreeModel(rootNode);
            setModel(treeModel);
        }
    }

    private void populateNode(DefaultMutableTreeNode parentNode, File parentFile) {
        // Get the list of files and subdirectories for the parent file
        File[] files = parentFile.listFiles();

        if (files != null) {
            // Sort the files in reverse order (descending)
            Arrays.sort(files, Comparator.reverseOrder());

            // Create child nodes for each file or directory in the parent file
            for (File file : files) {
                DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(file.getName());
                parentNode.add(fileNode);

                // Recursively call the method for subdirectories
                if (file.isDirectory()) {
                    populateNode(fileNode, file);
                }
            }
        }
    }

    // Method to refresh the tree when a folder is expanded
    public void refreshTree() {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        populateNode(rootNode, new File(rootNode.toString()));
        treeModel.reload();
    }

	public void removeChildren(DefaultMutableTreeNode selectedNode) {
		// TODO Auto-generated method stub
		
	}

    public void expandSelectedFolder() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        if (selectedNode != null) {
            Object userObject = selectedNode.getUserObject();
            if (userObject instanceof String) {
                String selectedPath = (String) userObject;
                File selectedFile = new File(selectedPath);
                if (selectedFile.isDirectory()) {
                    File[] subdirectories = selectedFile.listFiles(File::isDirectory);
                    if (subdirectories != null) {
                        tree.removeChildren(selectedNode); // Remove any existing child nodes
                        for (File subdirectory : subdirectories) {
                            DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(subdirectory.getPath());
                            treeModel.insertNodeInto(subNode, selectedNode, selectedNode.getChildCount());
                        }
                        treeModel.reload(selectedNode); // Reload the tree model to reflect changes
                    }
                }
            }
        }
    }

    public FileManagerTree getTree() {
        // Initialize the tree with the root node
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new FileTreeNodeData(File.listRoots()[0]));
        treeModel = new DefaultTreeModel(rootNode);
        setModel(treeModel);

        // Add a TreeSelectionListener to handle tree node selection events
        addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
                if (selectedNode != null) {
                    FileTreeNodeData nodeData = (FileTreeNodeData) selectedNode.getUserObject();
                    if (nodeData != null) {
                        File selectedFile = nodeData.getFile();
                        if (selectedFile.isDirectory()) {
                            FileManagerList list = FileManagerFrame.getInstance().getList();
                            list.populateList(selectedFile.getPath());
                        }
                    }
                }
            }
        });

        // Load the first level of the tree
        loadFirstLevel();
		return null;
    }

    private void loadFirstLevel() {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        File[] drives = File.listRoots();
        for (File drive : drives) {
            DefaultMutableTreeNode driveNode = new DefaultMutableTreeNode(new FileTreeNodeData(drive));
            treeModel.insertNodeInto(driveNode, rootNode, rootNode.getChildCount());
        }
    }
}
