package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static javax.swing.event.HyperlinkEvent.EventType.ACTIVATED;
import static school.hei.patrimoine.modele.recouppement.model.RecoupementStatus.IMPREVU;
import static school.hei.patrimoine.modele.recouppement.model.RecoupementStatus.NON_EXECUTE;

import java.awt.*;
import java.time.Period;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.model.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.model.RecoupementStatus;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.LinkOpener;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.DateFormatter;

@Slf4j
public class PossessionRecoupeeItem extends JPanel {
  private final State state;
  private final PieceJustificative pj;
  private final PossessionRecoupee<Possession> possessionRecoupee;

  public PossessionRecoupeeItem(
      State state, PossessionRecoupee<Possession> possessionRecoupee, PieceJustificative pj) {
    this.state = state;
    this.possessionRecoupee = possessionRecoupee;
    this.pj = pj;

    setOpaque(true);
    setLayout(new BorderLayout());
    setBorder(new EmptyBorder(10, 10, 10, 10));
    setBackground(getColor(possessionRecoupee.status()));

    addTitle();
    addActionsButton();
  }

  private String getSources() {
    var info = possessionRecoupee.info();
    var possession = possessionRecoupee.possession();

    var content =
        switch (possession) {
          case FluxArgent flux -> {
            var amount = flux.getFluxMensuel().montant();
            var label = amount < 0 ? "Source" : "Destination";
            yield "<b>" + label + ":</b> " + info.possessionACorriger().nom();
          }
          case TransfertArgent ignored ->
              "<b>Source:</b> "
                  + info.possessionACorrigerNegativement().nom()
                  + ",&nbsp&nbsp&nbsp<b>Destination:</b> "
                  + info.possessionACorriger().nom();
          default -> "";
        };
    return content.isEmpty() ? "" : "<div style='margin-bottom: 8px;'>" + content + "</div>";
  }

  private void addTitle() {
    var pjHtml =
        pj == null
            ? ""
            : "<div style='margin-bottom: 8px;'>"
                + "<b>Pièce justificative:</b> "
                + "<a href='"
                + pj.link()
                + "'>"
                + pj.id()
                + "</a>"
                + ",&nbsp&nbsp&nbsp"
                + "<b>Référence:</b> "
                + pj.reference()
                + ",&nbsp&nbsp&nbsp"
                + "<b>Date d'insertion:</b> "
                + DateFormatter.format(pj.date())
                + "</div>";

    var titleString =
        String.format(
            "<html>"
                + "<div style='margin-bottom: 8px;'>"
                + "<b>Nom:</b> %s"
                + "</div>"
                + "<div style='margin-bottom: 8px; '>"
                + "<b>Type:</b> %s,&nbsp&nbsp&nbsp"
                + "<b>Date prévue:</b> %s,&nbsp&nbsp&nbsp"
                + "<b>Retard:</b> %s, &nbsp&nbsp&nbsp"
                + "<b>Valeur prévue:</b> %s,&nbsp&nbsp&nbsp"
                + "<b>Valeur réalisée:</b> %s"
                + "</div>"
                + getSources()
                + pjHtml
                + "</html>",
            possessionRecoupee.possession().nom(),
            possessionRecoupee.possession().getClass().getSimpleName(),
            DateFormatter.format(possessionRecoupee.possession().t()),
            formatEcartDate(possessionRecoupee.ecartDateAvecRealises()),
            ArgentFormatter.format(possessionRecoupee.prevu().valeur()),
            ArgentFormatter.format(possessionRecoupee.valeurRealisee()));

    var title = getJEditorPane(titleString);

    add(title, BorderLayout.WEST);
  }

  private String formatEcartDate(Period period) {
    if (period.isZero()) {
      return "0j";
    }

    var inAdvance = period.isNegative();
    var absPeriod = inAdvance ? period.negated() : period;

    var result = new StringBuilder(inAdvance ? "-" : "+");

    if (absPeriod.getYears() > 0) {
      var unit = absPeriod.getYears() > 1 ? "ans" : "an";
      result.append(absPeriod.getYears()).append(unit);
    }
    if (absPeriod.getMonths() > 0) {
      result.append(absPeriod.getMonths()).append("mois");
    }
    if (absPeriod.getDays() > 0) {
      result.append(absPeriod.getDays()).append("j");
    }

    return result.toString();
  }

  private static @NonNull JEditorPane getJEditorPane(String titleString) {
    var title = new JEditorPane();
    title.setContentType("text/html");
    title.setText(titleString);
    title.setEditable(false);
    title.setOpaque(false);
    title.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
    title.setFont(new Font("Arial", Font.PLAIN, 16));

    title.addHyperlinkListener(
        event -> {
          if (event.getEventType() == ACTIVATED) {
            new LinkOpener().accept(event.getURL().toString());
          }
        });
    return title;
  }

  private boolean canBeExecuted() {
    var possession = possessionRecoupee.possession();
    if (IMPREVU.equals(possessionRecoupee.status())) {
      return false;
    }
    return possession instanceof FluxArgent || possession instanceof TransfertArgent;
  }

  private Button executeButton() {
    if (possessionRecoupee.status() == NON_EXECUTE) {
      return new Button(
          "Exécuter",
          e -> new PossessionRecoupeeRealisationsDialog(state, possessionRecoupee, true));
    }
    return new Button(
        "Exécutions",
        e -> new PossessionRecoupeeRealisationsDialog(state, possessionRecoupee, false));
  }

  private void addActionsButton() {
    var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panel.setOpaque(false);

    if (canBeExecuted()) {
      panel.add(executeButton());
    }

    panel.add(
        new Button(
            "Voir Details", e -> new PossessionRecoupeeDetailDialog(possessionRecoupee, pj)));

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
