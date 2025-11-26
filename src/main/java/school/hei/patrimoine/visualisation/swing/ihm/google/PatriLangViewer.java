package school.hei.patrimoine.visualisation.swing.ihm.google;

import static javax.swing.SwingUtilities.invokeLater;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.PatriLangStagingFileManager.getStagedDoneFiles;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.PatriLangStagingFileManager.getStagedPlannedFiles;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.Set;
import school.hei.patrimoine.google.GoogleApiUtilities;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.App;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.SyncConfirmDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkListDownloader;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.*;

public class PatriLangViewer extends App {
  public PatriLangViewer() {
    super("patrilang-app", "Patrimoine", 1_400, 900);

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
  }

  @Override
  protected String defaultPageName() {
    return LoginPage.PAGE_NAME;
  }

  @Override
  protected Set<Page> pages() {
    return Set.of(
        new LoginPage(),
        new SubmitLinkPage(),
        new LinkValidityPage(),
        new PatriLangFilesPage(),
        new RecoupementPage());
  }

  public static void main(String[] args) {
    App.setup();
    FlatLightLaf.setup();
    GoogleApiUtilities.setup();
    GoogleLinkListDownloader.setup();
    invokeLater(PatriLangViewer::new);
  }
}
