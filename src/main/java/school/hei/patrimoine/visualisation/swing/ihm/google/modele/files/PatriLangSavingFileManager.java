package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.clearAllTempContents;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.*;

import java.util.Set;
import school.hei.patrimoine.patrilang.files.PatriLangFileContext;
import school.hei.patrimoine.patrilang.files.PatriLangFileWriter;
import school.hei.patrimoine.patrilang.files.PatriLangFileWriter.FileWriterInput;

public class PatriLangSavingFileManager {
  private static final PatriLangFileWriter WRITER = new PatriLangFileWriter();

  public static void save(Set<FileWriterInput> inputs) throws Exception {
    WRITER.write(inputs);
    for (var input : inputs) {
      var file = new PatriLangFileContext(input);
      stage(input.file(), file.getCategory());
      clearAllTempContents();
    }
  }
}
