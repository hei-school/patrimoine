package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeItem.formatDate;

public class PossessionReoupeeDetailDialog extends Dialog {
    private final PossessionRecoupee possessionRecoupee;

    public PossessionReoupeeDetailDialog(PossessionRecoupee possessionRecoupee) {
        super("Details", 800, 300, false);
        this.possessionRecoupee = possessionRecoupee;

        setLayout(new BorderLayout());

        addTitle();
        addCorrections();

        setVisible(true);
    }

    private void addTitle() {
        var title = String.format(
                "Date=%s, Nom=%s, Type=%s",
                formatDate(possessionRecoupee.possession().t()),
                possessionRecoupee.possession().nom(),
                possessionRecoupee.possession().getClass().getSimpleName()
        );

        var label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setBorder(new EmptyBorder(15, 10, 15, 10));
        label.setForeground(Color.GRAY);

        add(label, BorderLayout.NORTH);
    }

    private void addCorrections(){
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBorder(new EmptyBorder(15, 0, 0, 10));

        for(var correction : possessionRecoupee.corrections()){
            panel.add(new CorrectionItem(correction));
            panel.add(Box.createVerticalStrut(10));
        }

        add(new JScrollPane(panel), BorderLayout.CENTER);
    }
}
