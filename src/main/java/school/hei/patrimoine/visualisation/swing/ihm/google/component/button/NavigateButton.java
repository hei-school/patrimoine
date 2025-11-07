package school.hei.patrimoine.visualisation.swing.ihm.google.component.button;

import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.PageManager;

public class NavigateButton extends Button {
  public NavigateButton(String text, String to) {
    super(text);

    addActionListener(e -> PageManager.navigateTo(to));
  }
}
