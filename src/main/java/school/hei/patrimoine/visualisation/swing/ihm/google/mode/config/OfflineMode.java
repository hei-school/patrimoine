package school.hei.patrimoine.visualisation.swing.ihm.google.mode.config;

import java.awt.*;
import java.util.List;
import java.util.Set;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.mode.AppMode;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.PatriLangFilesPage;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.RecoupementPage;

public class OfflineMode implements AppMode {
  @Override
  public boolean isOnline() {
    return false;
  }

  @Override
  public List<Component> appBarRightComponents(State state) {
    return List.of(AppBar.builtInFontSizeControllerButton(state));
  }

  @Override
  public Set<Page> pages() {
    return Set.of(new PatriLangFilesPage(this), new RecoupementPage(this));
  }

  @Override
  public String defaultPageNames() {
    return PatriLangFilesPage.PAGE_NAME;
  }

  @Override
  public boolean enableComments() {
    return false;
  }

  @Override
  public boolean enableDriveSync() {
    return false;
  }
}
