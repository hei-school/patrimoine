package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.awt.Toolkit.getDefaultToolkit;
import static javax.swing.SwingUtilities.invokeLater;
import static school.hei.patrimoine.visualisation.swing.ihm.google.mode.config.ModeResolver.current;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.getStagedDoneFiles;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.getStagedPlannedFiles;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import school.hei.patrimoine.google.GoogleApiUtilities;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.App;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.SyncConfirmDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.LocalCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.mode.AppMode;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkListDownloader;

public class PatriLangViewer extends App {
  private final AppMode mode;

  public PatriLangViewer(AppMode mode) {
    super(
        "patrilang-app",
        "Patrimoine",
        getDefaultToolkit().getScreenSize().width,
        getDefaultToolkit().getScreenSize().height,
        mode.defaultPageNames(),
        mode::pages);

    this.mode = mode;

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            if (!mode.isOnline()) {
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

  public static void main(String[] args) {
    App.setup();
    FlatLightLaf.setup();
    GoogleApiUtilities.setup();

    var mode = current();

    if (mode.isOnline()) {
      GoogleLinkListDownloader.setup();
    }

    invokeLater(() -> new PatriLangViewer(mode));
  }
}
