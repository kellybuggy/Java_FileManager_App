package filemanager;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.SwingWorker;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class FileManagerToolbar extends JToolBar {
    private FileManagerTree tree;
    private FileManagerList list;
    private FileManagerStatusBar statusBar; // Add status bar reference
    private Map<String, Boolean> driveDataLoaded; // Keep track of loaded drive data

    public FileManagerToolbar(FileManagerTree tree, FileManagerList list, FileManagerStatusBar statusBar) {
        this.tree = tree;
        this.list = list;
        this.statusBar = statusBar; // Initialize the status bar reference
        this.driveDataLoaded = new HashMap<>();

        // Set up the toolbar
        setFloatable(false);

        // Create the drive selection combo box
        JComboBox<String> driveComboBox = new JComboBox<>();

        // Populate the combo box with available drives
        File[] drives = File.listRoots();
        for (File drive : drives) {
            driveComboBox.addItem(drive.getPath());
        }

        driveComboBox.addActionListener(new DriveSelectionListener());
        add(driveComboBox);

        // Create the Simple and Details buttons
        JButton simpleButton = new JButton("Simple");
        simpleButton.addActionListener(new SimpleButtonListener());
        add(simpleButton);

        JButton detailsButton = new JButton("Details");
        detailsButton.addActionListener(new DetailsButtonListener());
        add(detailsButton);
    }

    private class DriveSelectionListener implements ActionListener {
        @SuppressWarnings("unchecked")
        @Override
        public void actionPerformed(ActionEvent e) {
            // Handle drive selection events
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            String selectedDrive = (String) comboBox.getSelectedItem();
            LoadDriveDataTask task = new LoadDriveDataTask(selectedDrive);
            task.execute();
        }
    }

    private class SimpleButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Handle Simple button click event
            list.setSimpleView();
        }
    }

    private class DetailsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Handle Details button click event
            list.setDetailsView();
        }
    }

    private class LoadDriveDataTask extends SwingWorker<Void, Void> {
        private String selectedDrive;

        public LoadDriveDataTask(String selectedDrive) {
            this.selectedDrive = selectedDrive;
        }

        @Override
        protected Void doInBackground() throws Exception {
            // Check if the drive data is already loaded
            if (!driveDataLoaded.containsKey(selectedDrive) || !driveDataLoaded.get(selectedDrive)) {
                // Load the tree and list data in the background
                tree.populateTree(selectedDrive);
                list.populateList(selectedDrive);
                driveDataLoaded.put(selectedDrive, true); // Mark the drive data as loaded
            }
            return null;
        }

        @Override
        protected void done() {
            // Update the status bar with the selected drive
            statusBar.updateStatus(selectedDrive);
        }
    }
}
