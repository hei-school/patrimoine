package school.hei.patrimoine.visualisation.swing.ihm.google.utils;

import static javax.swing.JOptionPane.showMessageDialog;

import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;

public class MessageDialog {
  private MessageDialog() {}

  public static void info(String title, String message) {
    showMessageDialog(
        AppContext.getDefault().app(), message, title, JOptionPane.INFORMATION_MESSAGE);
  }

  public static void error(String title, String message) {
    showMessageDialog(AppContext.getDefault().app(), message, title, JOptionPane.ERROR_MESSAGE);
  }
}
