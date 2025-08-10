package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class FileListModel extends AbstractListModel<File> {
    private final List<File> files;

    public FileListModel(List<File> files) {
        this.files = files;
    }

    @Override
    public int getSize() {
        return files.size();
    }

    @Override
    public File getElementAt(int index) {
        return files.get(index);
    }
}