package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import java.io.File;
import java.util.List;
import javax.swing.*;

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
