package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import school.hei.patrimoine.modele.possession.Correction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeItem.formatDate;

public class CorrectionItem extends JPanel {
    private final Correction correction;

    public CorrectionItem(Correction correction) {
        this.correction = correction;

        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 10, 5, 10));

        setBackground(new Color(255, 245, 200)); // couleur douce
        setOpaque(true);

        addTitle();
    }

    private void addTitle() {
        var titleString = String.format(
                "<html>"
                        + "<b>Date:</b> %s &nbsp;&nbsp; <b>Nom:</b> %s"
                        + "</html>",
                formatDate(correction.t()),
                correction.nom()
        );

        var label = new JLabel(titleString);
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setForeground(new Color(50, 50, 50));

        add(label, BorderLayout.CENTER);
    }
}