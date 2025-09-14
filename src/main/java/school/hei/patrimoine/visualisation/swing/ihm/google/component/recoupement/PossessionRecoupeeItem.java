package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static school.hei.patrimoine.modele.recouppement.PossessionRecoupee.PossessionRecoupeeStatus.*;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class PossessionRecoupeeItem extends JPanel {
  private final State state;
  private final PossessionRecoupee possessionRecoupee;

  public PossessionRecoupeeItem(State state, PossessionRecoupee possessionRecoupee) {
    this.state = state;
    this.possessionRecoupee = possessionRecoupee;

    setOpaque(true);
    setLayout(new BorderLayout());
    setBorder(new EmptyBorder(10, 10, 10, 10));
    setBackground(getBackgroundColor());

    addTitle();
    addActionsButton();
  }

  private void addTitle() {
    var titleString =
        String.format(
            "<html>"
                + "<div style='margin-bottom:6px;'><b>Nom:</b> %s</div>"
                + "<div style='margin-bottom:6px;'><b>Type:</b> %s</div>"
                + "<div style='margin-bottom:6px;'><b>Date prévue:</b> %s</div>"
                + "<div style='margin-bottom:6px;'><b>Date réalisée:</b> %s</div>"
                + "<div style='margin-bottom:6px;'><b>Valeur prévue:</b> %s</div>"
                + "<div style='margin-bottom:6px;'><b>Valeur réalisée:</b> %s</div>"
                + "</html>",
            possessionRecoupee.possession().nom(),
            possessionRecoupee.possession().getClass().getSimpleName(),
            formatDate(possessionRecoupee.datePrevu()),
            formatDate(possessionRecoupee.dateRealise()),
            formatArgent(possessionRecoupee.valeurPrevu()),
            formatArgent(possessionRecoupee.valeurRealise()));

    var title = new JLabel(titleString);
    title.setFont(new Font("Arial", Font.PLAIN, 16));
    title.setForeground(getForegroundColor());

    add(title, BorderLayout.WEST);
  }

  private void addActionsButton() {
    if (EXECUTE_SANS_CORRECTION.equals(possessionRecoupee.status())) {
      return;
    }
    var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    if (NON_EXECUTE.equals(possessionRecoupee.status())) {
      var executeButton =
          new Button(
              "Exécuter", e -> new PossessionRecoupeeExecuteDialog(state, possessionRecoupee));
      panel.add(executeButton);
    }

    var detailButton =
        new Button(
            "Voir Details", e -> new PossessionReoupeeDetailDialog(state, possessionRecoupee));
    panel.setOpaque(false);
    panel.add(detailButton);

    add(panel, BorderLayout.EAST);
  }

  private Color getBackgroundColor() {
    return switch (possessionRecoupee.status()) {
      case IMPREVU -> new Color(235, 100, 110);
      case NON_EXECUTE -> new Color(100, 180, 220);
      case EXECUTE_AVEC_CORRECTION -> new Color(255, 160, 80);
      case EXECUTE_SANS_CORRECTION -> new Color(120, 220, 140);
    };
  }

  private Color getForegroundColor() {
    return new Color(40, 40, 40);
  }

  @Override
  public Dimension getMaximumSize() {
    var pref = getPreferredSize();
    return new Dimension(Integer.MAX_VALUE, pref.height);
  }

  static String formatDate(LocalDate date) {
    if (LocalDate.MIN.equals(date)) {
      return "Date indéterminée (début)";
    }
    if (LocalDate.MAX.equals(date)) {
      return "Date indéterminée (fin)";
    }
    return date.toString();
  }

  static String formatArgent(Argent argent) {
    return String.format("%s %s", argent.ppMontant().replaceAll("000", "_000"), argent.devise());
  }
}
