package school.hei.patrimoine.visualisation.swing.ihm.google.component.popup;

import java.util.List;
import javax.swing.*;

public class PopupMenu extends JPopupMenu {
  public PopupMenu(List<JMenuItem> items) {
    items.forEach(this::add);
  }
}
