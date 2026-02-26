package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.io.File;
import java.util.Optional;
import javax.swing.*;

public class FileSelector {
  private static final String DOWNLOADS_PATH = "Downloads";
  private static final String TELECHARGEMENTS_PATH = "Téléchargements";

  public static Optional<File> selectOutputFile(String title, File baseFile, String fileExtension) {
    var fileChooser = new JFileChooser();
    fileChooser.setDialogTitle(title);
    fileChooser.setCurrentDirectory(getDefaultDir());

    var baseName = baseFile.getName().split("\\.", 2)[0];
    fileChooser.setSelectedFile(new File(baseName + fileExtension));
    if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
      return Optional.empty();
    }

    var outputFile = fileChooser.getSelectedFile();
    if (!outputFile.getName().toLowerCase().endsWith(fileExtension)) {
      outputFile = new File(outputFile.getParentFile(), outputFile.getName() + fileExtension);
    }

    return confirmOverwrite(outputFile) ? Optional.of(outputFile) : Optional.empty();
  }

  private static File getDefaultDir() {
    var home = new File(System.getProperty("user.home"));
    var downloadsPath = new File(home, DOWNLOADS_PATH);
    if (!downloadsPath.exists()) {
      downloadsPath = new File(home, TELECHARGEMENTS_PATH);
    }

    return downloadsPath.exists() ? downloadsPath : home;
  }

  private static boolean confirmOverwrite(File file) {
    if (!file.exists()) {
      return true;
    }

    return JOptionPane.showConfirmDialog(
            null,
            "Le fichier existe déjà.\nVoulez-vous le remplacer ?",
            "Fichier existant",
            JOptionPane.YES_NO_OPTION)
        == JOptionPane.YES_OPTION;
  }
}
