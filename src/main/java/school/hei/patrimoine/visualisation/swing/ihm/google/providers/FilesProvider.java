package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.*;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.TOUT_CAS_FILE_EXTENSION;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkListDownloader.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FilesProvider {
  public static List<File> getPatriLangJustificativeFiles() {
    return Arrays.stream(requireNonNull(new File(getJustificativeDirectoryPath()).listFiles()))
        .filter(file -> file.getName().endsWith(PJ_FILE_EXTENSION))
        .toList();
  }

  public static List<File> getPatriLangPlannedFiles() {
    return Arrays.stream(requireNonNull(new File(getPlannedDirectoryPath()).listFiles()))
        .filter(
            file ->
                file.getName().endsWith(TOUT_CAS_FILE_EXTENSION)
                    || file.getName().endsWith(CAS_FILE_EXTENSION))
        .toList();
  }

  public static List<File> getPatriLangDoneFiles() {
    return Arrays.stream(requireNonNull(new File(getDoneDirectoryPath()).listFiles()))
        .filter(
            file ->
                file.getName().endsWith(TOUT_CAS_FILE_EXTENSION)
                    || file.getName().endsWith(CAS_FILE_EXTENSION))
        .toList();
  }

  public static File getPlannedCasSetFile() {
    return Arrays.stream(requireNonNull(new File(getPlannedDirectoryPath()).listFiles()))
        .filter(file -> file.getName().endsWith(TOUT_CAS_FILE_EXTENSION))
        .findFirst()
        .orElseThrow();
  }

  public static File getDoneCasSetFile() {
    return Arrays.stream(requireNonNull(new File(getDoneDirectoryPath()).listFiles()))
        .filter(file -> file.getName().endsWith(TOUT_CAS_FILE_EXTENSION))
        .findFirst()
        .orElseThrow();
  }

  public static List<File> getDonePatrilangFilesWithoutCasSet() {
    return getPatriLangDoneFiles().stream()
        .filter(file -> !file.getName().endsWith(TOUT_CAS_FILE_EXTENSION))
        .toList();
  }
}
