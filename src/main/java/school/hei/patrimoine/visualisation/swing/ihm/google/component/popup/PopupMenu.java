package school.hei.patrimoine.visualisation.swing.ihm.google.component.popup;

import javax.swing.*;
import java.util.List;

public class PopupMenu  extends JPopupMenu {
    public PopupMenu(List<JMenuItem> items) {
        items.forEach(this::add);
    }
}
