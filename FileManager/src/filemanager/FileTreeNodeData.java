package filemanager;

import java.io.File;

public class FileTreeNodeData {
    private File file;

    public FileTreeNodeData(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return file.getName();
    }
}
