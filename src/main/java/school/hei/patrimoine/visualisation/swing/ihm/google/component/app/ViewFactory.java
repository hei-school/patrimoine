package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import javax.swing.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ViewFactory {
  public static JComponent make(String name, JComponent component) {
    component.setName(name);
    return component;
  }
}
