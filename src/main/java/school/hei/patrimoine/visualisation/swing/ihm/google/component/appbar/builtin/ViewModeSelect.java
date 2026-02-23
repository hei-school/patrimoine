package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import java.awt.*;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class ViewModeSelect extends JComboBox<ViewMode> {
  public ViewModeSelect(State state) {
    super(ViewMode.values());

    setSelectedItem(state.get("viewMode"));
    setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    setCursor(new Cursor(Cursor.HAND_CURSOR));
    addActionListener(e -> state.update("viewMode", getSelectedItem()));
  }
}
