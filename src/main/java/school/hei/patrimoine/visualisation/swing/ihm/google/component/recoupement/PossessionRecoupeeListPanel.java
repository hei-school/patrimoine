package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.util.List;
import javax.swing.*;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;

public class PossessionRecoupeeListPanel extends JPanel {
  private final Runnable refresh;

  public PossessionRecoupeeListPanel(Runnable refresh) {
    super();
    this.refresh = refresh;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(true);
  }

  public void update(List<PossessionRecoupee> possessionRecoupees) {
    removeAll();

    for (var possessionRecoupee : possessionRecoupees) {
      add(new PossessionRecoupeeItem(possessionRecoupee, refresh));
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
