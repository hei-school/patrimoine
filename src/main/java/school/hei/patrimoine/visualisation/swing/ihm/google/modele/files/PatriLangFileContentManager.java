package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import static java.nio.file.Files.readString;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getCasSet;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileWriter.FileWriterInput;

public class PatriLangFileContentManager {
  private static final Map<String, FileWriterInput> tempContents = new HashMap<>();

  public static TypedContent getContent(PatriLangFileContext file) {
    var key = file.getDriveId();
    if (tempContents.containsKey(key)) {
      return new TypedContent(tempContents.get(key), false);
    }

    try {
      var content = readString(Path.of(key));
      var input =
          FileWriterInput.builder().file(file).casSet(getCasSet(file)).content(content).build();
      return new TypedContent(input, true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void saveTempContent(PatriLangFileContext file, String content) {
    var input =
        FileWriterInput.builder().file(file).casSet(getCasSet(file)).content(content).build();
    tempContents.put(file.getDriveId(), input);
  }

  public static void clearAllTempContents() {
    tempContents.clear();
  }

  public static Set<FileWriterInput> getAllModifiedFiles() {
    return new HashSet<>(tempContents.values());
  }

  public static void removeInTempContent(PatriLangFileContext file) {
    tempContents.remove(file.getDriveId());
  }

  public record TypedContent(FileWriterInput input, boolean original) {}
}
