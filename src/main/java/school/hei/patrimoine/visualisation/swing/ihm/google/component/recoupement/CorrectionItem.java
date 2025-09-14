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

        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.YELLOW);

        addTitle();
    }

    private void addTitle(){
        var titleString = String.format(
            "Date=%s, Nom=%s",
            formatDate(correction.t()),
            correction.nom()
        );

        var label = new JLabel(titleString);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(new EmptyBorder(15, 10, 15, 10));

        add(label);
    }
}
