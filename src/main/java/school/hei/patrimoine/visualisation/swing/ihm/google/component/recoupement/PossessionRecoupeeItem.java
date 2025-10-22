package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.RecoupementStatus;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.DateFormatter;

public class PossessionRecoupeeItem extends JPanel {
  private final State state;
  private final PossessionRecoupee possessionRecoupee;

  public PossessionRecoupeeItem(State state, PossessionRecoupee possessionRecoupee) {
    this.state = state;
    this.possessionRecoupee = possessionRecoupee;

    setOpaque(true);
    setLayout(new BorderLayout());
    setBorder(new EmptyBorder(10, 10, 10, 10));
    setBackground(getColor(possessionRecoupee.status()));

    addTitle();
    addActionsButton();
  }

  private void addTitle() {
    var titleString =
        String.format(
            "<html>"
                + "<div style='margin-bottom:6px;'><b>Nom:</b> %s</div>"
                + "<div style='margin-bottom:6px;'><b>Type:</b> %s</div>"
                + "<div style='margin-bottom:6px;'><b>Date:</b> %s</div>"
                + "<div style='margin-bottom:6px;'><b>Valeur prévue:</b> %s</div>"
                + "<div style='margin-bottom:6px;'><b>Valeur réalisée:</b> %s</div>"
                + "</html>",
            possessionRecoupee.possession().nom(),
            possessionRecoupee.possession().getClass().getSimpleName(),
            DateFormatter.format(possessionRecoupee.possession().t()),
            ArgentFormatter.format(possessionRecoupee.prevu().valeur()),
            ArgentFormatter.format(possessionRecoupee.valeurRealisee()));

    var title = new JLabel(titleString);
    title.setFont(new Font("Arial", Font.PLAIN, 16));
    title.setForeground(new Color(40, 40, 40));

    add(title, BorderLayout.WEST);
  }

  private void addActionsButton() {
    var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panel.setOpaque(false);
    panel.add(
        new Button(
            "Exécutions",
            e -> new PossessionRecoupeeRealisationsDialog(state, possessionRecoupee)));
    panel.add(
        new Button("Voir Details", e -> new PossessionRecoupeeDetailDialog(possessionRecoupee)));

    add(panel, BorderLayout.EAST);
  }

  @Override
  public Dimension getMaximumSize() {
    var pref = getPreferredSize();
    return new Dimension(Integer.MAX_VALUE, pref.height);
  }

  static Color getColor(RecoupementStatus status) {
    return switch (status) {
      case IMPREVU -> new Color(235, 100, 110);
      case NON_EXECUTE -> new Color(100, 180, 220);
      case EXECUTE_AVEC_CORRECTION -> new Color(255, 160, 80);
      case EXECUTE_SANS_CORRECTION -> new Color(120, 220, 140);
    };
  }
}
