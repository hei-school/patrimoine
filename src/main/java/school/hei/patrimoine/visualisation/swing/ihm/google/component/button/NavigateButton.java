package school.hei.patrimoine.visualisation.swing.ihm.google.component.button;

import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.MultipleView;

public class NavigateButton extends Button {
  public NavigateButton(String text, String to) {
    super(text);

    addActionListener(e -> MultipleView.navigateTo(to));
  }

  public NavigateButton(MultipleView pageManager, String text, String to) {
    super(text);
    addActionListener(e -> pageManager.navigate(to));
  }
}
