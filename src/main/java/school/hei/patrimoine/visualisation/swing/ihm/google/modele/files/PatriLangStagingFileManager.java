package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static school.hei.patrimoine.google.GoogleApiUtilities.getStagingDirectoryPath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PatriLangStagingFileManager {
  public static String getStagedPlannedDir() {
    return getStagingDirectoryPath() + "/planifies";
  }

  public static String getStagedDoneDir() {
    return getStagingDirectoryPath() + "/realises";
  }

  public static String getStagedJustificativeDir() {
    return getStagingDirectoryPath() + "/justificatifs";
  }

  public static List<File> getStagedPlannedFiles() {
    return getFiles(getStagedPlannedDir());
  }

  public static List<File> getStagedDoneFiles() {
    return getFiles(getStagedDoneDir());
  }

  public static List<File> getStagedJustificativeFiles() {
    return getFiles(getStagedJustificativeDir());
  }

  public static void clearStagedPlanned() {
    deleteFolderContent(getStagedPlannedDir());
  }

  public static void clearStagedDone() {
    deleteFolderContent(getStagedDoneDir());
  }

  public static void clearJustificativeStaged() {
    deleteFolderContent(getStagedJustificativeDir());
  }

  public static void clearAllStagedFiles() {
    clearStagedDone();
    clearStagedPlanned();
    clearJustificativeStaged();
  }

  private static String getTargetDir(FileCategory category) {
    return switch (category) {
      case PLANNED -> getStagedPlannedDir();
      case DONE -> getStagedDoneDir();
      case JUSTIFICATIVE -> getStagedJustificativeDir();
    };
  }

  public static void stage(File file, FileCategory category) {
    try {
      var targetDir = getTargetDir(category);
      var target = Paths.get(targetDir, file.getName());
      createDirectories(target.getParent());
      copy(file.toPath(), target, REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException("Error saving staged file: " + file.getName(), e);
    }
  }

  private static void deleteFolderContent(String folder) {
    var directory = new File(folder);
    if (!directory.exists()) {
      return;
    }

    var files = directory.listFiles();
    if (files == null) {
      return;
    }

    for (var file : files) {
      if (file.exists() && !file.delete()) {
        throw new RuntimeException("Impossible de supprimer le fichier staged : " + file.getName());
      }
    }
  }

  private static List<File> getFiles(String folder) {
    var directory = new File(folder);
    var files = directory.listFiles();
    if (files == null) {
      return List.of();
    }
    return Arrays.asList(files);
  }
}
