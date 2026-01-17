package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static javax.swing.event.HyperlinkEvent.EventType.ACTIVATED;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.RecoupementStatus;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.DateFormatter;

@Slf4j
public class PossessionRecoupeeItem extends JPanel {
  private final State state;
  private final PossessionRecoupee possessionRecoupee;
  private final PieceJustificative pieceJustificative;

  public PossessionRecoupeeItem(
      State state, PossessionRecoupee possessionRecoupee, PieceJustificative pieceJustificative) {
    this.state = state;
    this.possessionRecoupee = possessionRecoupee;
    this.pieceJustificative = pieceJustificative;

    setOpaque(true);
    setLayout(new BorderLayout());
    setBorder(new EmptyBorder(10, 10, 10, 10));
    setBackground(getColor(possessionRecoupee.status()));

    addTitle();
    addActionsButton();
  }

  private void addTitle() {
    var pjHtml =
        pieceJustificative == null
            ? ""
            : "<div style='margin-bottom: 8px;'>"
                + "<b>Pièce justificative:</b> "
                + "<a href='"
                + pieceJustificative.link()
                + "'>"
                + pieceJustificative.id()
                + "</a>"
                + ",&nbsp&nbsp&nbsp"
                + "<b>Date d'insertion:</b> "
                + DateFormatter.format(pieceJustificative.date())
                + "</div>";

    var titleString =
        String.format(
            "<html>"
                + "<div style='margin-bottom: 8px;'>"
                + "<b>Nom:</b> %s"
                + "</div>"
                + "<div style='margin-bottom: 8px; '>"
                + "<b>Type:</b> %s,&nbsp&nbsp&nbsp"
                + "<b>Date:</b> %s,&nbsp&nbsp&nbsp"
                + "<b>Valeur prévue:</b> %s,&nbsp&nbsp&nbsp"
                + "<b>Valeur réalisée:</b> %s"
                + "</div>"
                + pjHtml
                + "</html>",
            possessionRecoupee.possession().nom(),
            possessionRecoupee.possession().getClass().getSimpleName(),
            DateFormatter.format(possessionRecoupee.possession().t()),
            ArgentFormatter.format(possessionRecoupee.prevu().valeur()),
            ArgentFormatter.format(possessionRecoupee.valeurRealisee()));

    var title = new JEditorPane();
    title.setContentType("text/html");
    title.setText(titleString);
    title.setEditable(false);
    title.setOpaque(false);
    title.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
    title.setFont(new Font("Arial", Font.PLAIN, 16));

    title.addHyperlinkListener(
        e -> {
          if (e.getEventType() == ACTIVATED) {
            try {
              java.awt.Desktop.getDesktop().browse(e.getURL().toURI());
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        });

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
