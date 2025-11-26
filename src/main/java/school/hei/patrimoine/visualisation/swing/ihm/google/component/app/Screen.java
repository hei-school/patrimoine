package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.PatriLangStagingFileManager.getStagedDoneFiles;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.PatriLangStagingFileManager.getStagedPlannedFiles;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.SyncConfirmDialog;

public class Screen extends JFrame {
  public Screen(String title, int width, int height) {
    super(title);

    setTitle(title);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setPreferredSize(new Dimension(width, height));

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            List<File> plannedFiles = getStagedPlannedFiles();
            List<File> doneFiles = getStagedDoneFiles();

            if (plannedFiles.isEmpty() && doneFiles.isEmpty()) {
              dispose();
              return;
            }

            boolean confirmed = SyncConfirmDialog.forQuit();
            if (confirmed) {
              dispose();
            }
          }
        });

    pack();

    setVisible(true);
  }
}
