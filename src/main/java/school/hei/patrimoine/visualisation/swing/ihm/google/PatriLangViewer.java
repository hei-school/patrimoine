package school.hei.patrimoine.visualisation.swing.ihm.google;

import static javax.swing.SwingUtilities.invokeLater;

import com.formdev.flatlaf.FlatLightLaf;
import java.util.Set;
import school.hei.patrimoine.google.GoogleApiUtilities;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.App;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkListDownloader;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.*;

public class PatriLangViewer extends App {
  public PatriLangViewer() {
    super("patrilang-app", "Patrimoine", 1_200, 700);
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
