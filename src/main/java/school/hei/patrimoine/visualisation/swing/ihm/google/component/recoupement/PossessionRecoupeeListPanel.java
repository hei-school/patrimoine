package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.util.Collection;
import java.util.Map;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.model.PossessionRecoupee;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

@Slf4j
public class PossessionRecoupeeListPanel extends JPanel {
  private final State state;

  public PossessionRecoupeeListPanel(State state) {
    super();
    this.state = state;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(true);
  }

  public void update(
      Collection<PossessionRecoupee<Possession>> recoupees, Map<String, PieceJustificative> pjs) {
    removeAll();

    recoupees.forEach(
        possession -> {
          var pj = pjs.getOrDefault(possession.possession().nom(), null);
          add(new PossessionRecoupeeItem(state, possession, pj));
          add(Box.createVerticalStrut(10));
        });

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
