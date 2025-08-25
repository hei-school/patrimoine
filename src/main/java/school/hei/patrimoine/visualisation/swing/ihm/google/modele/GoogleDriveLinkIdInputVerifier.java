package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static school.hei.patrimoine.google.GoogleDriveLinkIdParser.GOOGLE_DRIVE_ID_PATTERN;

import java.awt.*;
import javax.swing.*;

public class GoogleDriveLinkIdInputVerifier extends InputVerifier {
  @Override
  public boolean verify(JComponent input) {
    if (input instanceof JTextField textField) {
      String text = textField.getText();
      if (GOOGLE_DRIVE_ID_PATTERN.matcher(text).find()) {
        textField.setBackground(new Color(92, 145, 101));
        textField.setForeground(WHITE);
        return true;
      } else {
        textField.setBackground(RED);
        return false;
      }
    }
    return true;
  }
}
