package school.hei.patrimoine.visualisation.swing.ihm.google.mode;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar.builtInFontSizeControllerButton;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar.builtInUserInfoPanel;

import java.awt.*;
import java.util.List;
import java.util.Set;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.*;

public class OnlineMode implements AppMode {
  @Override
  public boolean isOnline() {
    return true;
  }

  @Override
  public boolean isOffline() {
    return false;
  }

  @Override
  public List<Component> appBarRightComponents(State state) {
    return List.of(builtInFontSizeControllerButton(state), builtInUserInfoPanel());
  }

  @Override
  public Set<Page> pages() {
    return Set.of(
        new LoginPage(),
        new SubmitLinkPage(),
        new LinkValidityPage(),
        new PatriLangFilesPage(),
        new RecoupementPage());
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
