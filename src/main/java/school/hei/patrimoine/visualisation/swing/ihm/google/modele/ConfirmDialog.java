package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import static javax.swing.JOptionPane.showConfirmDialog;

import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;

public class ConfirmDialog {
  public static boolean ask(String title, String message) {
    var isConfirmed =
        showConfirmDialog(AppContext.getDefault().app(), message, title, JOptionPane.YES_NO_OPTION);
    return isConfirmed == JOptionPane.YES_OPTION;
  }

  @SuppressWarnings("all")
  public static boolean ask(String title, String message, int optionType) {
    var isConfirmed = showConfirmDialog(AppContext.getDefault().app(), message, title, optionType);
    return isConfirmed == JOptionPane.YES_OPTION;
  }
}
