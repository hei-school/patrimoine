package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.formatter.DateFormatter;

public class PossessionRecoupeeDetailDialog extends Dialog {
  private final PossessionRecoupee possessionRecoupee;

  public PossessionRecoupeeDetailDialog(PossessionRecoupee possessionRecoupee) {
    super("Détails de l'opération", 900, 600, false);
    this.possessionRecoupee = possessionRecoupee;

    setLayout(new BorderLayout());
    setBackground(Color.WHITE);

    addMainTitle();
    addContentPanel();
    addCloseButton();

    setVisible(true);
  }

  private void addMainTitle() {
    var title =
        new JLabel(
            "<html><b>Détails de l'opération : "
                + possessionRecoupee.possession().nom()
                + "</b></html>");
    title.setFont(new Font("Arial", Font.BOLD, 20));
    title.setBorder(new EmptyBorder(15, 15, 15, 15));
    add(title, BorderLayout.NORTH);
  }

  private void addContentPanel() {
    var mainPanel = new JPanel(new BorderLayout(20, 0));
    mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    mainPanel.setBackground(Color.WHITE);

    mainPanel.add(createInfoPanel(), BorderLayout.WEST);
    mainPanel.add(createListsPanel(), BorderLayout.CENTER);

    add(mainPanel, BorderLayout.CENTER);
  }

  private JPanel createInfoPanel() {
    var infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    infoPanel.setBackground(Color.WHITE);

    infoPanel.add(makeInfoRow("Type", possessionRecoupee.possession().getClass().getSimpleName()));
    infoPanel.add(Box.createVerticalStrut(10));
    infoPanel.add(makeInfoRow("Date", DateFormatter.format(possessionRecoupee.possession().t())));
    infoPanel.add(Box.createVerticalStrut(10));
    infoPanel.add(
        makeInfoRow("Valeur prévue", ArgentFormatter.format(possessionRecoupee.prevu().valeur())));
    infoPanel.add(Box.createVerticalStrut(10));
    infoPanel.add(
        makeInfoRow(
            "Valeur réalisée", ArgentFormatter.format(possessionRecoupee.valeurRealisee())));
    infoPanel.add(Box.createVerticalStrut(10));
    infoPanel.add(
        makeInfoRow("Écart", ArgentFormatter.format(possessionRecoupee.ecartValeurAvecRealises())));

    return infoPanel;
  }

  private JPanel createListsPanel() {
    var listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    listPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
    listPanel.setBackground(Color.WHITE);

    listPanel.add(
        createList(
            "Réalisations",
            possessionRecoupee.realises().stream()
                .map(
                    info ->
                        String.format(
                            "%s , %s , %s",
                            DateFormatter.format(info.t()),
                            ArgentFormatter.format(info.valeur()),
                            info.possession().nom()))
                .toArray(String[]::new),
            Color.WHITE));

    listPanel.add(Box.createVerticalStrut(20));

    listPanel.add(
        createList(
            "Corrections",
            possessionRecoupee.corrections().stream()
                .map(c -> String.format("%s , %s", DateFormatter.format(c.t()), c.nom()))
                .toArray(String[]::new),
            new Color(255, 245, 200)));

    return listPanel;
  }

  private JPanel createList(String titleText, String[] items, Color bgColor) {
    var panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE);
    panel.setBorder(new EmptyBorder(5, 0, 5, 0));

    var title = new JLabel(titleText);
    title.setFont(new Font("Arial", Font.BOLD, 16));
    title.setBorder(new EmptyBorder(0, 0, 5, 0));
    panel.add(title, BorderLayout.NORTH);

    var list = new JList<>(items);
    list.setBackground(bgColor);
    list.setFont(list.getFont().deriveFont(14f));
    list.setFixedCellHeight(30);
    list.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    panel.add(new JScrollPane(list), BorderLayout.CENTER);

    return panel;
  }

  private void addCloseButton() {
    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setOpaque(true);
    buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    buttonPanel.add(new Button("Fermer", e -> dispose()));

    add(buttonPanel, BorderLayout.SOUTH);
  }

  private JPanel makeInfoRow(String labelText, String valueText) {
    var row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    row.setBackground(new Color(245, 245, 245));
    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    row.setBorder(new EmptyBorder(5, 10, 5, 10));

    var label = new JLabel(labelText + ": ");
    label.setFont(label.getFont().deriveFont(Font.PLAIN));
    row.add(label);

    var value = new JLabel(valueText);
    value.setFont(value.getFont().deriveFont(Font.BOLD));
    row.add(value);

    return row;
  }
}
