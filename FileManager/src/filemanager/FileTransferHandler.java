package filemanager;

import java.awt.datatransfer.*;
import java.io.File;
import java.util.List;
import javax.swing.tree.TreePath;

public class FileTransferHandler implements Transferable {
    private final DataFlavor[] supportedFlavors = { DataFlavor.javaFileListFlavor, DataFlavor.stringFlavor };
    private final List<File> fileList;
    private final String[] filePaths;

    public FileTransferHandler(String[] selectedFiles) {
        this.fileList = FileManagerUtil.convertToFiles(selectedFiles);
        this.filePaths = selectedFiles;
    }

    public FileTransferHandler(TreePath[] selectedPaths) {
        this.fileList = null;
        this.filePaths = null;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(DataFlavor.javaFileListFlavor) || flavor.equals(DataFlavor.stringFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor.equals(DataFlavor.javaFileListFlavor)) {
            return fileList != null ? fileList : FileManagerUtil.convertToFiles(filePaths);
        } else if (flavor.equals(DataFlavor.stringFlavor)) {
            return FileManagerUtil.convertToString(filePaths);
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
