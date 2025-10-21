package school.hei.patrimoine.visualisation.swing.ihm.google.component.popup;

import static java.awt.Cursor.HAND_CURSOR;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PopupItem extends JMenuItem {
  public PopupItem(String text) {
    this(text, null);
  }

  public PopupItem(String text, ActionListener actionListener) {
    super(text);
    setCursor(new Cursor(HAND_CURSOR));
    setMargin(new Insets(8, 16, 8, 16));
    setVerticalAlignment(CENTER);
    addActionListener(actionListener);
  }
}
