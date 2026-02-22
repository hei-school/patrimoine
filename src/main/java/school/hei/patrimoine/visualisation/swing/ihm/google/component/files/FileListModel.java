package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import java.util.List;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

public class FileListModel extends AbstractListModel<PatriLangFileContext> {
  private final List<PatriLangFileContext> files;

  public FileListModel(List<PatriLangFileContext> files) {
    this.files = files;
  }

  @Override
  public int getSize() {
    return files.size();
  }

  @Override
  public PatriLangFileContext getElementAt(int index) {
    return files.get(index);
  }
}
