package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.PJ_FILE_EXTENSION;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkListDownloader.getJustificativeDirectoryPath;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FilesProvider {
  public static List<File> getPatriLangJustificativeFiles() {
    return Arrays.stream(requireNonNull(new File(getJustificativeDirectoryPath()).listFiles()))
        .filter(file -> file.getName().endsWith(PJ_FILE_EXTENSION))
        .toList();
  }
}
