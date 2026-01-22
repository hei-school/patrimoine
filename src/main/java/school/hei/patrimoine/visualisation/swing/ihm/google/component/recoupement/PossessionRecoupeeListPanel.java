package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.util.Set;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

@Slf4j
public class PossessionRecoupeeListPanel extends JPanel {
  private final State state;
  private final PieceJustificativeMatcher matcher = new PieceJustificativeMatcher();

  public PossessionRecoupeeListPanel(State state) {
    super();
    this.state = state;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(true);
  }

  public void update(
      Set<PossessionRecoupee> possessions, Set<PieceJustificative> piecesJustificatives) {

    removeAll();

    var pjSet = piecesJustificatives == null ? Set.<PieceJustificative>of() : piecesJustificatives;
    log.info("possessions={} pjs={}", possessions.size(), pjSet.size());

    possessions.forEach(
        possession -> {
          PieceJustificative matched = null;

          if (possession.hasSupportingDocument()) {
            matched = matcher.findMatchingPiece(pjSet, possession.possession().nom());
          }

          add(new PossessionRecoupeeItem(state, possession, matched));
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
