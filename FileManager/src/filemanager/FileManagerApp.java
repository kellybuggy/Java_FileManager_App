package filemanager;

import java.awt.EventQueue;

public class FileManagerApp {
    @SuppressWarnings("unused")
	public static void main(String[] args) {
        // Launch the application on the Event Dispatch Thread (EDT)
        EventQueue.invokeLater(() -> {
            FileManagerFrame frame = new FileManagerFrame();
        });
    }
}
