package school.hei.patrimoine.visualisation.swing.ihm.google.component.popup;

import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

import javax.swing.*;
import java.util.List;

public class PopupMenuButton extends Button {
    private final PopupMenu popupMenu;

    public PopupMenuButton(String text, List<JMenuItem> items) {
        super(text);
        this.popupMenu = new PopupMenu(items);

        setToolTipText(text);
        addActionListener(e -> this.popupMenu.show(this, 0, this.getHeight()));
    }
}
