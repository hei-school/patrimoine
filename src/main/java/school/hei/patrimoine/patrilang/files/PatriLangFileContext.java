package school.hei.patrimoine.patrilang.files;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.FileCategory.*;

import school.hei.patrimoine.visualisation.swing.ihm.google.modele.FileCategory;

public class PatriLangFileContext {
  private final PatriLangFileWritter.FileWritterInput input;
  private final String filePath;

  public PatriLangFileContext(PatriLangFileWritter.FileWritterInput input) {
    this.input = input;
    this.filePath = input.file().getAbsolutePath().replace("\\", "/");
  }

  public FileCategory getCategory() {
    if (isJustificative()) {
      return JUSTIFICATIVE;
    } else if (isPlanned()) {
      return PLANNED;
    } else if (isDone()) {
      return DONE;
    } else {
      throw new IllegalStateException(
          "File path does not match any category: " + input.file().getAbsolutePath());
    }
  }

  private boolean isJustificative() {
    return filePath.contains("/download/justificatifs");
  }

  private boolean isPlanned() {
    return filePath.contains("/download/planifies");
  }

  private boolean isDone() {
    return filePath.contains("/download/realises");
  }
}
