package filemanager;

import javax.swing.JLabel;
import java.io.File;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public class FileManagerStatusBar extends JLabel {
    private static String currentDrive;
    private long totalSpace;
    private long freeSpace;
    private long lastUpdated;

    public FileManagerStatusBar() {
        // Initialize the status bar with default text
        updateStatusText();
    }

    public void updateStatus(String driveName) {
        // Update the status bar with the current drive information
        currentDrive = driveName;
        updateDriveSpaceInBackground(driveName); // Update the drive space information in the background
        updateStatusText(); // Update the status text
    }

    private void updateDriveSpaceInBackground(String drivePath) {
        // Perform I/O operations in the background using SwingWorker
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                updateDriveSpace(drivePath);
                return null;
            }

            @Override
            protected void done() {
                // Do any post-processing after the background task is done
                updateStatusText(); // Update the status text after drive space is updated
            }
        };

        worker.execute();
    }

    void updateDriveSpace(String drivePath) {
        // Update the space information of the current drive
        File drive = new File(drivePath);
        if (drive.exists() && drive.isDirectory()) {
            totalSpace = drive.getTotalSpace();
            freeSpace = drive.getFreeSpace();
            setLastUpdated(System.currentTimeMillis());
        }
    }

    private void updateStatusText() {
        if (currentDrive != null) {
            String formattedFreeSpace = formatSize(freeSpace);
            String formattedTotalSpace = formatSize(totalSpace);
            long usedSpace = totalSpace - freeSpace;
            String formattedUsedSpace = formatSize(usedSpace);

            setText("Current Drive: " + currentDrive + " | Free Space: " + formattedFreeSpace +
                    " | Used Space: " + formattedUsedSpace + " | Total Space: " + formattedTotalSpace);
        } else {
            setText("No Drive Selected");
        }
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

    public static String getCurrentDrive() {
        return currentDrive; // Return the selected drive
    }

	public long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
