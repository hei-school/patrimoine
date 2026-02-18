package school.hei.patrimoine.patrilang.files;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.FileCategory.*;

import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.FileCategory;

// TODO: refactor ... any google package's code should be imported here (FileCategory)
public class PatriLangFileContext {
  private final String filePath;
  private final PatriLangFileWriter.FileWriterInput input;

  public PatriLangFileContext(PatriLangFileWriter.FileWriterInput input) {
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
