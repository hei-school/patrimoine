package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

import static school.hei.patrimoine.modele.recouppement.PossessionRecoupee.PossessionRecoupeeStatus.*;

public class PossessionRecoupeeItem extends JPanel {
    private final Runnable refresh;
    private final PossessionRecoupee possessionRecoupee;

    public PossessionRecoupeeItem(PossessionRecoupee possessionRecoupee, Runnable refresh) {
        this.refresh = refresh;
        this.possessionRecoupee = possessionRecoupee;

        setOpaque(true);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(getBackgroundColor());

        addTitle();
        addActionsButton();
    }

    private void addTitle() {
        var titleString = String.format(
                "<html>"
                        + "<b>Nom:</b> %s<br/>"
                        + "<b>Type:</b> %s<br/>"
                        + "<b>Date prévue:</b> %s<br/>"
                        + "<b>Date réalisée:</b> %s<br/>"
                        + "<b>Valeur prévue:</b> %s<br/>"
                        + "<b>Valeur réalisée:</b> %s"
                        + "</html>",
                possessionRecoupee.possession().nom(),
                possessionRecoupee.possession().getClass().getSimpleName(),
                formatDate(possessionRecoupee.datePrévu()),
                formatDate(possessionRecoupee.dateRéalisé()),
                formatArgent(possessionRecoupee.valeurPrévu()),
                formatArgent(possessionRecoupee.valeurRéalisé())
        );

        var title = new JLabel(titleString);
        title.setFont(new Font("Arial", Font.PLAIN, 16));
        title.setForeground(getForegroundColor());

        add(title, BorderLayout.WEST);
    }

    private void addActionsButton() {
        if(EXECUTE_SANS_CORRECTION.equals(possessionRecoupee.status())){
            return;
        }

        var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        var detailButton = new Button("Voir Details", e -> new PossessionReoupeeDetailDialog(possessionRecoupee));
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
       return String.format("%s %s", argent.ppMontant(), argent.devise());
    }
}
