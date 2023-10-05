package filemanager;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.*;

@SuppressWarnings("serial")
public class FileManagerFrame extends JFrame {
    private FileManagerTree tree;
    private FileManagerList list;
    private FileManagerMenuBar menuBar;
    private FileManagerStatusBar statusBar;
    private JSplitPane splitPane;

    public FileManagerFrame() {
        initializeUI();
    }

    // Singleton instance
    private static FileManagerFrame instance;
    private void initializeUI() {
        setTitle("CECS 544 File Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create the menu bar and add it to the frame
        menuBar = new FileManagerMenuBar(this);
        setJMenuBar(menuBar);

        // Create the main components
        tree = new FileManagerTree();
        list = new FileManagerList();
        statusBar = new FileManagerStatusBar();
        list.setFileManagerFrame(this); // Set the FileManagerFrame instance


        // Add the tree and list to scroll panes
        JScrollPane treeScrollPane = new JScrollPane(tree);
        JScrollPane listScrollPane = new JScrollPane(list);

        // Set up the split pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, listScrollPane);
        splitPane.setDividerLocation(200);

        // Set up the toolbar panel with FlowLayout to take up minimal space
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.add(new FileManagerToolbar(tree, list, statusBar));

        // Set up the layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolbarPanel, BorderLayout.NORTH);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        // Set the main frame visible
        setVisible(true);
    }

    // Method for minimizing the frame
    public void minimizeFrame() {
        setState(Frame.ICONIFIED);
    }

    // Method for maximizing the frame
    public void maximizeFrame() {
        setState(Frame.NORMAL);
    }

    // Method for closing the frame
    public void closeFrame() {
        dispose();
    }

    public FileManagerTree getTree() {
        return tree;
    }

    public FileManagerList getList() {
        return list;
    }

    public FileManagerStatusBar getStatusBar() {
        return statusBar;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public void expandSelectedFolder() {
        tree.expandSelectedFolder();
    }
    


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FileManagerFrame();
        });
    }


    // Static method to get the singleton instance
    public static FileManagerFrame getInstance() {
        if (instance == null) {
            instance = new FileManagerFrame();
        }
        return instance;
    }
}