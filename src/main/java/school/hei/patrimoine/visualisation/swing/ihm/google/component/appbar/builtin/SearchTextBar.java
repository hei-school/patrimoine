package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.PlaceholderTextField;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.Debouncer;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class SearchTextBar extends PlaceholderTextField {
  public SearchTextBar(State state) {
    super("Rechercher");

    setPreferredSize(new Dimension(180, 35));
    setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

    var contentSearchDebouncer = new Debouncer(() -> state.update("searchText", getText().trim()));

    addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyReleased(KeyEvent evt) {
            contentSearchDebouncer.restart();
          }
        });
  }
}
