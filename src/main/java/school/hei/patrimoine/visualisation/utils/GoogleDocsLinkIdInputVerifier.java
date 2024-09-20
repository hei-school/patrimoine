package school.hei.patrimoine.visualisation.utils;

import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static school.hei.patrimoine.visualisation.utils.GoogleDocsLinkIdParser.GOOGLE_DOCS_ID_PATTERN;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class GoogleDocsLinkIdInputVerifier extends InputVerifier {
  @Override
  public boolean verify(JComponent input) {
    if (input instanceof JTextField textField) {
      String text = textField.getText();
      if (GOOGLE_DOCS_ID_PATTERN.matcher(text).find()) {
        textField.setBackground(GREEN); // Valid input
        return true;
      } else {
        textField.setBackground(RED); // Invalid input
        return false;
      }
    }
    return true;
  }
}
