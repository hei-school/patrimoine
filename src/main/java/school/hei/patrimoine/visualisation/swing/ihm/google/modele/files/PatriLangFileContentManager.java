package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import static java.nio.file.Files.readString;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import school.hei.patrimoine.patrilang.files.PatriLangFileWriter.FileWriterInput;

public class PatriLangFileContentManager {
  private static final Map<String, FileWriterInput> tempContents = new HashMap<>();

  public static TypedContent getContent(File file, File casSet) {
    var key = file.getAbsolutePath();
    if (tempContents.containsKey(key)) {
      return new TypedContent(tempContents.get(key), false);
    }

    try {
      var content = readString(Path.of(key));
      var input = FileWriterInput.builder().file(file).casSet(casSet).content(content).build();
      return new TypedContent(input, true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void saveTempContent(File file, File casSet, String content) {
    var input = FileWriterInput.builder().file(file).casSet(casSet).content(content).build();
    tempContents.put(file.getAbsolutePath(), input);
  }

  public static void clearAllTempContents() {
    tempContents.clear();
  }

  public static Set<FileWriterInput> getAllModifiedFiles() {
    return new HashSet<>(tempContents.values());
  }

  public static void removeInTempContent(File file) {
    tempContents.remove(file.getAbsolutePath());
  }

  public record TypedContent(FileWriterInput input, boolean original) {}
}
