package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.util.Objects;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import school.hei.patrimoine.modele.objectif.ObjectifNonAtteint;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.DateFormatter;

public class ObjectifItemPanel extends JPanel {
  public ObjectifItemPanel(ObjectifNonAtteint obj) {
    setLayout(new BorderLayout());
    setOpaque(false);
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    var icon = new JLabel(loadIcon("/icons/goal.png"));
    icon.setAlignmentX(Component.CENTER_ALIGNMENT);

    var iconPanel = new JPanel();
    iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));
    iconPanel.setOpaque(false);

    iconPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

    iconPanel.setPreferredSize(new Dimension(50, 0));
    iconPanel.setMaximumSize(new Dimension(50, Integer.MAX_VALUE));

    iconPanel.add(Box.createVerticalGlue());
    iconPanel.add(icon);
    iconPanel.add(Box.createVerticalGlue());

    add(iconPanel, BorderLayout.WEST);

    var text = new JTextPane();

    text.setBorder(
        CustomBorder.builder()
            .borderColor(new Color(141, 141, 141))
            .thickness(1)
            .radius(10)
            .padding(10, 10)
            .build());

    var doc = text.getStyledDocument();
    append(doc, "Nom : ", true);
    append(doc, obj.objectivable().nom() + ", ", false);

    append(doc, "Date : ", true);
    append(doc, DateFormatter.format(obj.objectif().t()) + "\n\n", false);

    append(doc, "Objectif : ", true);
    append(doc, ArgentFormatter.format(obj.objectif().valeurComptable()) + ", ", false);

    append(doc, "Atteint : ", true);
    append(
        doc,
        ArgentFormatter.format(
                obj.objectivable()
                    .valeurAObjectifT(obj.objectif().t())
                    .convertir(obj.objectif().valeurComptable().devise(), obj.objectif().t()))
            + "\n\n",
        false);

    append(doc, "Différence : ", true);
    append(doc, computeDiff(obj), false);

    add(text, BorderLayout.CENTER);
  }

  private static void append(javax.swing.text.Document doc, String txt, boolean bold) {
    var attrs = new SimpleAttributeSet();
    StyleConstants.setBold(attrs, bold);
    StyleConstants.setFontSize(attrs, 14);

    try {
      doc.insertString(doc.getLength(), txt, attrs);
    } catch (BadLocationException e) {
      throw new RuntimeException(e);
    }
  }

  private static String computeDiff(ObjectifNonAtteint obj) {
    var objectif = obj.objectif().valeurComptable();
    var atteint =
        obj.objectivable()
            .valeurAObjectifT(obj.objectif().t())
            .convertir(objectif.devise(), obj.objectif().t());
    var diff = objectif.minus(atteint, obj.objectif().t());
    return ArgentFormatter.format(diff);
  }

  private static ImageIcon loadIcon(String path) {
    var url = ObjectifItemPanel.class.getResource(path);
    var icon = new ImageIcon(Objects.requireNonNull(url));
    Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
    return new ImageIcon(scaled);
  }
}
