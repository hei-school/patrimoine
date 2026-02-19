package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.awt.Toolkit.getDefaultToolkit;
import static javax.swing.SwingUtilities.invokeLater;
import static school.hei.patrimoine.visualisation.swing.ihm.google.mode.config.EnvironnementConfigMode.getCurrentMode;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.getStagedDoneFiles;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.getStagedPlannedFiles;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;
import school.hei.patrimoine.google.GoogleApiUtilities;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.App;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.SyncConfirmDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.LocalCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkListDownloader;

public class PatriLangViewer extends App {
  public PatriLangViewer() {
    super(
        "patrilang-app",
        "Patrimoine",
        getDefaultToolkit().getScreenSize().width,
        getDefaultToolkit().getScreenSize().height);

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            if (getCurrentMode().isOffline()) {
              dispose();
              return;
            }

            var localCommentManager = LocalCommentManager.getInstance();
            var doneFiles = getStagedDoneFiles();
            var plannedFiles = getStagedPlannedFiles();
            var hasPendingComments = localCommentManager.hasAnyPendingComments();

            if (plannedFiles.isEmpty() && doneFiles.isEmpty() && !hasPendingComments) {
              dispose();
              return;
            }

            if (SyncConfirmDialog.forQuit()) {
              dispose();
            }
          }
        });
  }

  @Override
  protected Set<Page> pages() {
    return getCurrentMode().pages();
  }

  @Override
  protected String defaultPageName() {
    return getCurrentMode().defaultPageNames();
  }

  public static void main(String[] args) {
    App.setup();
    FlatLightLaf.setup();
    GoogleApiUtilities.setup();

    var mode = getCurrentMode();
    if (mode.isOnline()) {
      GoogleLinkListDownloader.setup();
    }

    invokeLater(PatriLangViewer::new);
  }
}
