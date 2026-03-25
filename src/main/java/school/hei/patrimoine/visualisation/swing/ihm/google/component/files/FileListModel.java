package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import java.util.List;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

public class FileListModel extends AbstractListModel<PatriLangFileContext> {
  private List<PatriLangFileContext> files;

  public FileListModel(List<PatriLangFileContext> files) {
    this.files = files;
  }

  @Override
  public int getSize() {
    return files.size();
  }

  public void refresh(List<PatriLangFileContext> newFiles) {
    this.files = newFiles;
    fireContentsChanged(this, 0, getSize());
  }

  @Override
  public PatriLangFileContext getElementAt(int index) {
    return files.get(index);
  }
}
