package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

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
            "Date=%s, Nom=%s, Type=%s",
            formatDate(possessionRecoupee.possession().t()),
            possessionRecoupee.possession().nom(),
            possessionRecoupee.possession().getClass().getSimpleName()
        );

        var title = new JLabel(titleString);
        title.setFont(new Font("Arial", Font.PLAIN, 15));
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
        return switch (possessionRecoupee.status()){
            case IMPREVU -> Color.RED;
            case NON_EXECUTE -> Color.BLUE;
            case EXECUTE_AVEC_CORRECTION -> Color.YELLOW;
            case EXECUTE_SANS_CORRECTION -> Color.GREEN;
        };
    }

    private Color getForegroundColor() {
        return switch (possessionRecoupee.status()){
            case IMPREVU, NON_EXECUTE -> Color.WHITE;
            case EXECUTE_AVEC_CORRECTION, EXECUTE_SANS_CORRECTION -> Color.BLACK;
        };
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
}
