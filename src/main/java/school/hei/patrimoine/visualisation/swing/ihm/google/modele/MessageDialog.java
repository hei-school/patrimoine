package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import static javax.swing.JOptionPane.showMessageDialog;

import javax.swing.*;
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

  public static void showError(Exception error) {
    showError("Erreur", error.getMessage());
  }

  public static void showError(String message) {
    showError("Erreur", message);
  }
}
