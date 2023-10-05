package filemanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class FileManagerUtil {
    public static String getParentFolderPath(String folderPath) {
        // Get the parent folder path from the given folder path
        File folder = new File(folderPath);
        return folder.getParent();
    }
    
    public static DefaultMutableTreeNode[] convertToTreeNodes(TreePath[] treePaths) {
        List<DefaultMutableTreeNode> nodeList = new ArrayList<>();
        for (TreePath treePath : treePaths) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            nodeList.add(node);
        }
        return nodeList.toArray(new DefaultMutableTreeNode[0]);
    }
    
    public static List<File> convertToFiles(String[] filePaths) {
        List<File> fileList = new ArrayList<>();
        for (String filePath : filePaths) {
            File file = new File(filePath);
            fileList.add(file);
        }
        return fileList;
    }
    
    public static String convertToString(String[] filePaths) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String filePath : filePaths) {
            stringBuilder.append(filePath).append("\n");
        }
        return stringBuilder.toString();
    }

    public static boolean copyFileOrFolder(String sourcePath, String destinationPath) {
        // Copy the file or folder from the source path to the destination path
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);

        try {
            if (sourceFile.isDirectory()) {
                // Copy the entire folder and its contents
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                // Copy the individual file
                Files.copy(sourceFile.toPath(), destinationFile.toPath());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFileOrFolder(String filePath) {
        // Delete the file or folder at the given path
        File file = new File(filePath);

        if (file.isDirectory()) {
            // Delete the entire folder and its contents
            return deleteDirectory(file);
        } else {
            // Delete the individual file
            return file.delete();
        }
    }

    private static boolean deleteDirectory(File directory) {
        // Helper method to delete a directory and its contents recursively
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }

    public static boolean moveFileOrFolder(String sourcePath, String destinationPath) {
        // Move the file or folder from the source path to the destination path
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);

        try {
            if (sourceFile.isDirectory()) {
                // Move the entire folder and its contents
                Files.move(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                // Move the individual file
                Files.move(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void runFile(String filePath) {
        // Run the selected file using the default associated application
        File file = new File(filePath);
        
        try {
            if (!file.isDirectory()) {
                // Use the default system application to open the file
                ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", file.getAbsolutePath());
                processBuilder.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean copyFile(String sourcePath, String destinationPath) {
        // Copy the file from the source path to the destination path
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);

        try {
            if (sourceFile.isDirectory()) {
                // Copy the entire folder and its contents
                copyDirectory(sourceFile, destinationFile);
            } else {
                // Copy the individual file
                Files.copy(sourceFile.toPath(), destinationFile.toPath());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyDirectory(File source, File destination) throws IOException {
        // Helper method to copy a directory and its contents recursively
        if (!destination.exists()) {
            destination.mkdir();
        }

        File[] files = source.listFiles();
        if (files != null) {
            for (File file : files) {
                File newFile = new File(destination.getAbsolutePath() + File.separator + file.getName());
                if (file.isDirectory()) {
                    copyDirectory(file, newFile);
                } else {
                    Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

	public static FileManagerList getFileManagerListFromParent(FileManagerTree fileManagerTree) {
		// TODO Auto-generated method stub
		return null;
	}

	public static FileManagerStatusBar getFileManagerStatusBarFromParent(FileManagerTree fileManagerTree) {
		// TODO Auto-generated method stub
		return null;
	}
}
