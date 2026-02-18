package school.hei.patrimoine.visualisation.swing.ihm.google.mode;

import java.awt.*;
import java.util.List;
import java.util.Set;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public interface AppMode {
  boolean isOnline();

  List<Component> appBarRightComponents(State state);

  Set<Page> pages();

  String defaultPageNames();

  boolean enableComments();

  boolean enableDriveSync();
}
