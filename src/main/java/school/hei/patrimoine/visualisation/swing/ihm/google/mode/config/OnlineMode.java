package school.hei.patrimoine.visualisation.swing.ihm.google.mode.config;

import java.awt.*;
import java.util.List;
import java.util.Set;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.mode.AppMode;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.*;

public class OnlineMode implements AppMode {
  @Override
  public boolean isOnline() {
    return true;
  }

  @Override
  public List<Component> appBarRightComponents(State state) {
    return List.of(AppBar.builtInFontSizeControllerButton(state), AppBar.builtInUserInfoPanel());
  }

  @Override
  public Set<Page> pages() {
    return Set.of(
        new LoginPage(),
        new SubmitLinkPage(),
        new LinkValidityPage(),
        new PatriLangFilesPage(this),
        new RecoupementPage(this));
  }

  @Override
  public String defaultPageNames() {
    return LoginPage.PAGE_NAME;
  }

  @Override
  public boolean enableComments() {
    return true;
  }

  @Override
  public boolean enableDriveSync() {
    return true;
  }
}
