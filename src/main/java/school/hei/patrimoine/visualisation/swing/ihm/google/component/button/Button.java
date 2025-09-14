package school.hei.patrimoine.visualisation.swing.ihm.google.component.button;

import static java.awt.Cursor.HAND_CURSOR;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Button extends JButton {
  public Button(String text) {
    super(text);

    setCursor(new Cursor(HAND_CURSOR));
    setMargin(new Insets(12, 16, 8, 16));
    setVerticalAlignment(CENTER);
  }

  public Button(String text, ActionListener actionListener) {
    this(text);

    addActionListener(actionListener);
  }
}
