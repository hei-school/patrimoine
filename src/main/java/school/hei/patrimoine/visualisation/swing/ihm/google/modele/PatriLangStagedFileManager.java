package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.GoogleApiUtilities;

@Slf4j
public class PatriLangStagedFileManager {

  public static String getStagedPlannedDir() {
    return GoogleApiUtilities.getStagingDirectoryPath() + "/planifies";
  }

  public static String getStagedDoneDir() {
    return GoogleApiUtilities.getStagingDirectoryPath() + "/realises";
  }

  public static void setup() {
    resetDirectory(getStagedPlannedDir());
    resetDirectory(getStagedDoneDir());
  }

  private static void resetDirectory(String path) {
    var dir = Paths.get(path);
    try {
      if (!Files.exists(dir)) {
        Files.createDirectories(dir);
      }
    } catch (IOException e) {
      throw new RuntimeException("Staged directory creation error: " + path, e);
    }
  }

  public static void saveToStaged(File file, boolean isPlanned) {
    try {
      String targetDir = isPlanned ? getStagedPlannedDir() : getStagedDoneDir();
      Path target = Paths.get(targetDir, file.getName());

      Files.createDirectories(target.getParent());
      Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

      log.info("Staged file saved: {}", target);
    } catch (IOException e) {
      throw new RuntimeException("Error saving staged file: " + file.getName(), e);
    }
  }

  public static List<File> getStagedPlannedFiles() {
    return listFilesIn(getStagedPlannedDir());
  }

  public static List<File> getStagedDoneFiles() {
    return listFilesIn(getStagedDoneDir());
  }

  private static List<File> listFilesIn(String folder) {
    File f = new File(folder);
    File[] files = f.listFiles();
    if (files == null) return List.of();
    return Arrays.asList(files);
  }

    public static void clearStagedPlanned() {
        deleteAllFilesIn(getStagedPlannedDir());
    }

    public static void clearStagedDone() {
        deleteAllFilesIn(getStagedDoneDir());
    }

    public static void clearAllStaged() {
        clearStagedPlanned();
        clearStagedDone();
    }

    private static void deleteAllFilesIn(String folder) {
        File dir = new File(folder);
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.exists() && !f.delete()) {
                log.warn("Impossible de supprimer le fichier staged : {}", f.getName());
            }
        }
    }
}
