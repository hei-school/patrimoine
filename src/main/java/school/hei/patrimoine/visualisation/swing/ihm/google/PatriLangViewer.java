package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.awt.Toolkit.getDefaultToolkit;
import static javax.swing.SwingUtilities.invokeLater;
import static school.hei.patrimoine.visualisation.swing.ihm.google.config.EnvironmentConfig.isOfflineMode;
import static school.hei.patrimoine.visualisation.swing.ihm.google.config.EnvironmentConfig.isOnlineMode;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;
import school.hei.patrimoine.google.GoogleApiUtilities;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.App;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.ExitConfirmDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkListDownloader;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.*;

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
            if (isOfflineMode()) {
              dispose();
              return;
            }

            var stagedFiles = PatriLangStagingFileManager.getFiles();
            var pendingComments = PendingCommentManager.getPendings();
            if (stagedFiles.isEmpty() && pendingComments.isEmpty()) {
              dispose();
              return;
            }

            var confirmDialog = new ExitConfirmDialog();
            if (confirmDialog.isConfirmed()) {
              dispose();
            }
          }
        });
  }

  @Override
  protected String defaultPageName() {
    return isOnlineMode() ? LoginPage.PAGE_NAME : PatriLangFilesPage.PAGE_NAME;
  }

  @Override
  protected Set<Page> pages() {
    if (isOfflineMode()) {
      return Set.of(new PatriLangFilesPage(), new RecoupementPage());
    }

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

    if (isOnlineMode()) {
      GoogleLinkListDownloader.setup();
    }

    invokeLater(PatriLangViewer::new);
  }
}
