package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeItem.formatArgent;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeItem.formatDate;

public class PossessionReoupeeDetailDialog extends Dialog {
    private final PossessionRecoupee possessionRecoupee;

    public PossessionReoupeeDetailDialog(PossessionRecoupee possessionRecoupee) {
        super("Détails de l'opération", 800, 400, false);
        this.possessionRecoupee = possessionRecoupee;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        addMainTitle();
        addInfoPanel();
        addCorrections();
        addCloseButton();

        setVisible(true);
    }

    private void addMainTitle() {
        var titleLabel = new JLabel(
            String.format("<html><b>Détails de l'opération: %s</b></html>", possessionRecoupee.possession().nom())
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(titleLabel, BorderLayout.NORTH);
    }

    private void addInfoPanel() {
        var infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 1, 5, 5)); // one column, each row = label+value
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoPanel.setBackground(Color.WHITE);

        infoPanel.add(makeInfoRow("Type", possessionRecoupee.possession().getClass().getSimpleName()));
        infoPanel.add(makeInfoRow("Date prévue", formatDate(possessionRecoupee.datePrévu())));
        infoPanel.add(makeInfoRow("Date réalisée", formatDate(possessionRecoupee.dateRéalisé())));
        infoPanel.add(makeInfoRow("Valeur prévue", formatArgent(possessionRecoupee.valeurPrévu())));
        infoPanel.add(makeInfoRow("Valeur réalisée", formatArgent(possessionRecoupee.valeurRéalisé())));
        infoPanel.add(makeInfoRow("Valeur différence", formatArgent(possessionRecoupee.valeurDifference())));
        infoPanel.add(makeInfoRow("Jours de différence", String.valueOf(possessionRecoupee.dateDifferenceEnJour())));

        add(infoPanel, BorderLayout.WEST);
    }

    private JPanel makeInfoRow(String labelText, String valueText) {
        var row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(new Color(245, 245, 245)); // light background
        row.setBorder(new EmptyBorder(5, 10, 5, 10));

        var label = new JLabel(labelText + " ");
        label.setFont(label.getFont().deriveFont(Font.PLAIN));

        var value = new JLabel(valueText);
        value.setFont(value.getFont().deriveFont(Font.BOLD));

        row.add(label);
        row.add(value);
        return row;
    }

    private void addCorrections() {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 10, 15, 10));

        var correctionsTitle = new JLabel("Corrections");
        correctionsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        correctionsTitle.setBorder(new EmptyBorder(5, 0, 10, 0));
        panel.add(correctionsTitle);

        for (var correction : possessionRecoupee.corrections()) {
            var item = new CorrectionItem(correction);
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(item);
            panel.add(Box.createVerticalStrut(10));
        }

        var scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addCloseButton() {
        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        var closeButton = new Button("Fermer", e -> dispose());
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
