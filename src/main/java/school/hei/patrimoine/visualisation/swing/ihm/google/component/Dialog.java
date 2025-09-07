package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import static javax.swing.SwingConstants.CENTER;

import java.awt.*;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;

public class Dialog extends JDialog {
  public Dialog(String title, int width, int height) {
    this(AppContext.getDefault().app(), title, width, height);
  }

  public Dialog(Frame owner, String title, int width, int height) {
    super(owner, title, true);

    var label = new JLabel(title);
    label.setHorizontalAlignment(CENTER);
    getContentPane().add(label, CENTER);
    setLocationRelativeTo(this);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(width, height);
  }
}
