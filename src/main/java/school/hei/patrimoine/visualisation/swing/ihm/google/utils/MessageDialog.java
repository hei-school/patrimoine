package school.hei.patrimoine.visualisation.swing.ihm.google.utils;

import static javax.swing.JOptionPane.showMessageDialog;

import javax.swing.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;

public class MessageDialog {
  private MessageDialog() {}

  public static void showInfo(String title, String message) {
    showMessageDialog(
        AppContext.getDefault().app(), message, title, JOptionPane.INFORMATION_MESSAGE);
  }

  public static void showError(String title, String message) {
    showMessageDialog(AppContext.getDefault().app(), message, title, JOptionPane.ERROR_MESSAGE);
  }

  public static boolean showExceptionMessageIfRecognizedException(Exception error) {
    if ((error instanceof IllegalArgumentException)
        || (error instanceof ParseCancellationException)) {
      showError("Erreur", error.getMessage());
      return true;
    }

    return false;
  }
}
