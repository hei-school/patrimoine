package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.clearAllTempContents;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.*;

import java.util.Set;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileWriter;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileWriter.FileWriterInput;

public class PatriLangSavingFileManager {
  private static final PatriLangFileWriter WRITER = new PatriLangFileWriter();

  public static void save(Set<FileWriterInput> inputs) throws Exception {
    WRITER.write(inputs);
    for (var input : inputs) {
      stage((PatriLangFileContext) input.file());
    }
    clearAllTempContents();
  }
}
