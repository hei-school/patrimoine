package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.util.Objects;
import javax.swing.*;
import school.hei.patrimoine.modele.objectif.ObjectifNonAtteint;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.DateFormatter;

public class ObjectifItemPanel extends JPanel {
  public ObjectifItemPanel(ObjectifNonAtteint obj) {
    setLayout(new BorderLayout());
    setOpaque(false);
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    add(buildIconPanel(), BorderLayout.WEST);
    add(buildTextPanel(obj), BorderLayout.CENTER);
  }

  private JPanel buildIconPanel() {
    var icon = new JLabel(loadIcon("/icons/goal.png"));
    icon.setAlignmentX(Component.CENTER_ALIGNMENT);

    var iconPanel = new JPanel();
    iconPanel.setOpaque(false);
    iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));
    iconPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

    iconPanel.add(Box.createVerticalGlue());
    iconPanel.add(icon);
    iconPanel.add(Box.createVerticalGlue());

    return iconPanel;
  }

  private JComponent buildTextPanel(ObjectifNonAtteint obj) {
    var editor = new JEditorPane();

    editor.setContentType("text/html");
    editor.setEditable(false);
    editor.setOpaque(false);
    editor.setText(buildHtml(obj));
    editor.setCaretPosition(0);

    editor.setBorder(
        CustomBorder.builder()
            .borderColor(new Color(255, 108, 108))
            .thickness(1)
            .radius(10)
            .padding(0, 0)
            .build());

    return editor;
  }

  private String buildHtml(ObjectifNonAtteint obj) {
    return """
<html>
<body style='background-color:rgb(255, 250, 250); font-family:SansSerif; font-size:12px; margin:0; padding:15px;'>
    <b>Nom :</b> <i>%s</i>,
    <b> Date :</b> <i>%s</i><br/><br/>
    <b>Objectif :</b> <i>%s</i>,
    <b> Atteint :</b> <i>%s</i>,
    <b> Différence :</b> <i>%s</i>
</body>
</html>
"""
        .formatted(
            escape(obj.objectivable().nom()),
            escape(DateFormatter.format(obj.objectif().t())),
            escape(ArgentFormatter.format(obj.objectif().valeurComptable())),
            escape(
                ArgentFormatter.format(
                    obj.objectivable()
                        .valeurAObjectifT(obj.objectif().t())
                        .convertir(obj.objectif().valeurComptable().devise(), obj.objectif().t()))),
            escape(computeDiff(obj)));
  }

  private static String computeDiff(ObjectifNonAtteint obj) {
    var objectif = obj.objectif().valeurComptable();
    var atteint =
        obj.objectivable()
            .valeurAObjectifT(obj.objectif().t())
            .convertir(objectif.devise(), obj.objectif().t());
    return ArgentFormatter.format(objectif.minus(atteint, obj.objectif().t()));
  }

  private static String escape(String s) {
    if (s == null) return "";
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }

  private static ImageIcon loadIcon(String path) {
    var url = ObjectifItemPanel.class.getResource(path);
    var icon = new ImageIcon(Objects.requireNonNull(url));
    Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
    return new ImageIcon(scaled);
  }
}
