package filemanager;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileManagerDialog {

	public static String showRenameDialog(String currentFolderPath, String selectedItem) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        // Top Panel to show current folder path
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel currentFolderLabel = new JLabel("Current Directory:");
        JTextField currentFolderField = new JTextField(currentFolderPath);
        currentFolderField.setEditable(false);
        currentFolderField.setBackground(Color.WHITE);
        topPanel.add(currentFolderLabel, BorderLayout.WEST);
        topPanel.add(currentFolderField, BorderLayout.CENTER);

        // Middle Panel to show current name and new name fields
        JPanel middlePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JLabel nameLabel = new JLabel("From:");
        JTextField nameField = new JTextField(selectedItem); // Set the current name here
        nameField.setEditable(true);
        nameField.setBackground(Color.WHITE);

        JLabel newNameLabel = new JLabel("To:");
        JTextField newNameField = new JTextField(20);

        middlePanel.add(nameLabel);
        middlePanel.add(nameField);
        middlePanel.add(newNameLabel);
        middlePanel.add(newNameField);

        // Add top and middle panels to the main panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(middlePanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Rename", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newName = newNameField.getText().trim();
            if (!newName.isEmpty()) {
                return currentFolderPath + File.separator + newName;
            }
        }
        return null;
    }

public static String showCopyDialog(String currentDrive) {
    JPanel panel = new JPanel(new BorderLayout(5, 5));

    // Top Panel to show current drive
    JPanel topPanel = new JPanel(new BorderLayout());
    JLabel currentDriveLabel = new JLabel("Current Directory:");
    JTextField currentDriveField = new JTextField(currentDrive);
    currentDriveField.setEditable(false);
    currentDriveField.setBackground(Color.WHITE);
    topPanel.add(currentDriveLabel, BorderLayout.WEST);
    topPanel.add(currentDriveField, BorderLayout.CENTER);

    // Middle Panel to show source and destination
    JPanel middlePanel = new JPanel(new GridLayout(2, 2, 5, 5));
    JLabel sourceLabel = new JLabel("From:");
    JTextField sourceField = new JTextField(20);
    JLabel destinationLabel = new JLabel("To:");
    JTextField destinationField = new JTextField(20);
    middlePanel.add(sourceLabel);
    middlePanel.add(sourceField);
    middlePanel.add(destinationLabel);
    middlePanel.add(destinationField);

    // Add top and middle panels to the main panel
    panel.add(topPanel, BorderLayout.NORTH);
    panel.add(middlePanel, BorderLayout.CENTER);

    int result = JOptionPane.showConfirmDialog(null, panel,
            "Copy File", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        String destinationPath = destinationField.getText().trim();
        if (!destinationPath.isEmpty()) {
            return destinationPath;
        }
    }
    return null;
}


public static String showPasteDialog(String currentDrive) {
    JPanel panel = new JPanel(new BorderLayout(5, 5));

    // Top Panel to show current drive
    JPanel topPanel = new JPanel(new BorderLayout());
    JLabel currentDriveLabel = new JLabel("Current Directory:");
    JTextField currentDriveField = new JTextField(currentDrive);
    currentDriveField.setEditable(false);
    currentDriveField.setBackground(Color.WHITE);
    topPanel.add(currentDriveLabel, BorderLayout.WEST);
    topPanel.add(currentDriveField, BorderLayout.CENTER);

    // Middle Panel to show source and destination fields
    JPanel middlePanel = new JPanel(new GridLayout( 3, 6));
    JLabel sourceLabel = new JLabel("From:");
    JTextField sourceField = new JTextField(15);
    sourceField.setEditable(true);
    sourceField.setBackground(Color.WHITE);

    JLabel destinationLabel = new JLabel(" To:");
    JTextField destinationField = new JTextField(10);

    middlePanel.add(sourceLabel);
    middlePanel.add(sourceField);
    middlePanel.add(destinationLabel);
    middlePanel.add(destinationField);

    // Add top and middle panels to the main panel
    panel.add(topPanel, BorderLayout.NORTH);
    panel.add(middlePanel, BorderLayout.CENTER);

    int result = JOptionPane.showConfirmDialog(null, panel,
            "Paste File", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        String destinationPath = destinationField.getText().trim();
        if (!destinationPath.isEmpty()) {
            return destinationPath;
        }
    }
    return null;
}

	
    public static int showDeleteConfirmationDialog() {
        return JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this file?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }
}
