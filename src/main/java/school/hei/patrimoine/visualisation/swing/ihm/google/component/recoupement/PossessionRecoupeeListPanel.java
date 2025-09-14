package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.util.List;
import javax.swing.*;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class PossessionRecoupeeListPanel extends JPanel {
  private final State state;

  public PossessionRecoupeeListPanel(State state) {
    super();
    this.state = state;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(true);
  }

  public void update(List<PossessionRecoupee> possessionRecoupees) {
    removeAll();

    for (var possessionRecoupee : possessionRecoupees) {
      add(new PossessionRecoupeeItem(state, possessionRecoupee));
      add(Box.createVerticalStrut(10));
    }

    revalidate();
    repaint();
  }

  public JScrollPane toScrollPane() {
    var scroll =
        new JScrollPane(
            this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.getVerticalScrollBar().setUnitIncrement(20);

    return scroll;
  }
}
