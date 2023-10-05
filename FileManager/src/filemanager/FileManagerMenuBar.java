package filemanager;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class FileManagerMenuBar extends JMenuBar {
    private FileManagerList list;
    private static JMenuItem renameItem;
    private static JMenuItem copyItem;
    private static JMenuItem deleteItem;
    
    public FileManagerMenuBar(FileManagerFrame parentFrame) {
        // Create the File menu and its items
        // Create the File menu and its items
        JMenu fileMenu = new JMenu("File");
        renameItem = new JMenuItem("Rename"); // Properly initializing the class variable
        copyItem = new JMenuItem("Copy"); // Properly initializing the class variable
        deleteItem = new JMenuItem("Delete"); // Properly initializing the class variable
        JMenuItem runItem = new JMenuItem("Run");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem renameItem = new JMenuItem("Rename");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem deleteItem = new JMenuItem("Delete");

        // Create the Tree menu and its items
        JMenu treeMenu = new JMenu("Tree");
        JMenuItem expandItem = new JMenuItem("Expand Branch");
        JMenuItem collapseItem = new JMenuItem("Collapse Branch");

        // Create the Window menu and its items
        JMenu windowMenu = new JMenu("Window");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem cascadeItem = new JMenuItem("Cascade");

        // Create the Help menu and its items
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpItem = new JMenuItem("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        

        // Add action listeners to the menu items
        renameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for renaming a file
                JOptionPane.showMessageDialog(parentFrame, "Rename action triggered.",
                        "Rename", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for copying a file
                JOptionPane.showMessageDialog(parentFrame, "Copy action triggered.",
                        "Copy", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for deleting a file
            	list.deleteSelectedFile();
            }
        });

        runItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for running a file
                JOptionPane.showMessageDialog(parentFrame, "Run action triggered.",
                        "Run", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for exiting the application
                System.exit(0);
            }
        });

        expandItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for expanding a branch in the tree
                JOptionPane.showMessageDialog(parentFrame, "Expand action triggered.",
                        "Expand", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        collapseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for collapsing a branch in the tree
                JOptionPane.showMessageDialog(parentFrame, "Collapse action triggered.",
                        "Collapse", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for creating a new internal frame
                JOptionPane.showMessageDialog(parentFrame, "New action triggered.",
                        "New", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        cascadeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for cascading open frames
                JOptionPane.showMessageDialog(parentFrame, "Cascade action triggered.",
                        "Cascade", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        helpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for displaying Help information
                JOptionPane.showMessageDialog(parentFrame, "Help Information",
                        "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for displaying the About dialog
                JOptionPane.showMessageDialog(parentFrame, "FileManager - Version 1.0\n"
                		+ "Created by: UDay",
                        "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add the items to the corresponding menus
        fileMenu.add(renameItem);
        fileMenu.add(copyItem);
        fileMenu.add(deleteItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(runItem);
        fileMenu.add(new JSeparator());
     // Add the items to the File menu
        fileMenu.add(exitItem);
        treeMenu.add(expandItem);
        treeMenu.add(collapseItem);
        windowMenu.add(newItem);
        windowMenu.add(cascadeItem);
        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);

        // Add the menus to the menu bar
        add(fileMenu);
        add(treeMenu);
        add(windowMenu);
        add(helpMenu);
    }

    // Method to enable or disable menu items based on file selection
    public static void setEnableFileMenuItems(boolean enable) {
        renameItem.setEnabled(enable);
        copyItem.setEnabled(enable);
        deleteItem.setEnabled(enable);
    }

    // Helper methods to show messages and errors
    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
