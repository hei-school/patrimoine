package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.model.Info;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.DateFormatter;

public class CreateRealisationCard extends JPanel {
  public CreateRealisationCard(Info<Possession> info, String commentaire) {

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground(new Color(203, 203, 203));
    setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(192, 192, 192)),
            new EmptyBorder(8, 10, 8, 0)));

    setAlignmentX(Component.LEFT_ALIGNMENT);
    add(Box.createVerticalStrut(5));

    var nomLabel = new JLabel("<html><b>Nom: </b> " + info.possession().nom() + "</html>");
    nomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    add(nomLabel);

    var detailLabel =
        new JLabel(
            String.format(
                "<html>" + "<b>Date:</b>%s,&nbsp;<b>Valeur:</b>%s" + "</html>",
                DateFormatter.format(info.t()), ArgentFormatter.format(info.valeur())));

    detailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    add(detailLabel);

    if (commentaire != null && !commentaire.isBlank()) {
      add(Box.createVerticalStrut(5));
      var commentLabel = new JLabel("<html><b>Commentaire:</b> " + commentaire + "</html>");
      commentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
      add(commentLabel);

      setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
    } else {
      setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
    }
  }
}
