package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.OperationComment;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.model.Info;
import school.hei.patrimoine.modele.recouppement.model.PossessionRecoupee;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.LinkOpener;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.DateFormatter;

public class PossessionRecoupeeDetailDialog extends Dialog {
  private final PieceJustificative pj;
  private final PossessionRecoupee<Possession> possessionRecoupee;
  private final List<OperationComment> comments;

  public PossessionRecoupeeDetailDialog(
      State state, PossessionRecoupee<Possession> possessionRecoupee, PieceJustificative pj) {
    super("Détails de l'opération", 1000, 600, false);
    this.pj = pj;
    this.possessionRecoupee = possessionRecoupee;

    @SuppressWarnings("unchecked")
    List<OperationComment> loaded = (List<OperationComment>) state.get("operationComments");
    this.comments = loaded != null ? loaded : List.of();

    setLayout(new BorderLayout());
    setBackground(Color.WHITE);

    addMainTitle();
    addContentPanel();
    addActions();

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

    var infoPanel = createInfoPanel();
    infoPanel.setPreferredSize(new Dimension(220, 0));
    mainPanel.add(infoPanel, BorderLayout.WEST);

    var listsScrollPane = new JScrollPane(createListsPanel());
    listsScrollPane.setBorder(null);
    listsScrollPane.getViewport().setBackground(Color.WHITE);

    mainPanel.add(listsScrollPane, BorderLayout.CENTER);

    add(mainPanel, BorderLayout.CENTER);
  }

  private JPanel createInfoPanel() {
    var infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    infoPanel.setBackground(Color.WHITE);
    infoPanel.setAlignmentY(Component.TOP_ALIGNMENT);

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
    listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    listPanel.setBackground(Color.WHITE);
    listPanel.setAlignmentY(Component.TOP_ALIGNMENT);

    var realisationsTitle = new JLabel("Réalisations");
    realisationsTitle.setFont(new Font("Arial", Font.BOLD, 16));
    realisationsTitle.setBorder(new EmptyBorder(0, 10, 5, 0));
    realisationsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    listPanel.add(realisationsTitle);

    var cardsPanel = new JPanel();
    cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
    cardsPanel.setBackground(Color.WHITE);
    cardsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

    possessionRecoupee
        .realises()
        .forEach(
            info -> {
              cardsPanel.add(createRealisationCard(info));
              cardsPanel.add(Box.createVerticalStrut(8));
            });

    var scrollPane = new JScrollPane(cardsPanel);
    scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    scrollPane.getViewport().setBackground(Color.WHITE);
    scrollPane.setPreferredSize(new Dimension(600, 200));
    scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    listPanel.add(scrollPane);

    listPanel.add(Box.createVerticalStrut(20));

    listPanel.add(
        createList(
            possessionRecoupee.corrections().stream().map(Correction::nom).toArray(String[]::new),
            new Color(255, 245, 200)));

    return listPanel;
  }

  private JPanel createRealisationCard(Info<Possession> info) {
    var card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBackground(new Color(203, 203, 203));
    card.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(158, 158, 158)),
            new EmptyBorder(8, 10, 8, 0)));
    card.setAlignmentX(Component.LEFT_ALIGNMENT);
    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

    card.add(Box.createVerticalStrut(5));

    var detailLabel =
        new JLabel(
            String.format(
                "<html><b>Opération:</b> %s,&nbsp;%s,&nbsp;%s</html>",
                DateFormatter.format(info.t()),
                ArgentFormatter.format(info.valeur()),
                info.possession().nom()));
    detailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    card.add(detailLabel);

    comments.stream()
        .filter(c -> c.id().equals(info.possession().nom()))
        .findFirst()
        .ifPresent(
            c -> {
              card.add(Box.createVerticalStrut(5));
              var commentLabel = new JLabel("<html><b>Commentaire:</b> " + c.content() + "</html>");
              commentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
              card.add(commentLabel);
              card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
            });

    return card;
  }

  private JPanel createList(String[] items, Color bgColor) {
    var panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.WHITE);
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);

    var title = new JLabel("Corrections");
    title.setFont(new Font("Arial", Font.BOLD, 16));
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    title.setBorder(new EmptyBorder(0, 10, 5, 0));

    panel.add(title);

    var list = new JList<>(items);
    list.setBackground(bgColor);
    list.setFont(list.getFont().deriveFont(14f));
    list.setFixedCellHeight(30);
    list.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

    var scroll = new JScrollPane(list);
    scroll.setPreferredSize(new Dimension(600, 150));
    scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
    scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

    panel.add(scroll);

    return panel;
  }

  private void addActions() {
    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setOpaque(true);
    buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    if (pj != null) {
      buttonPanel.add(
          new Button("Voir la pièce justificative", e -> new LinkOpener().accept(pj.link())));
    }

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
