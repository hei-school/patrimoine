package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeItem.formatDate;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.modele.possession.Correction;

public class CorrectionItem extends JPanel {
  private final Correction correction;

  public CorrectionItem(Correction correction) {
    this.correction = correction;

    setOpaque(false);
    setLayout(new BorderLayout());
    setBorder(new EmptyBorder(15, 10, 15, 10));

    setBackground(new Color(255, 245, 200));
    setOpaque(true);

    addTitle();
  }

  private void addTitle() {
    var titleString =
        String.format(
            "<html>" + "<b>Date:</b> %s &nbsp;&nbsp; <b>Nom:</b> %s" + "</html>",
            formatDate(correction.t()), correction.nom());

    var label = new JLabel(titleString);
    label.setFont(new Font("Arial", Font.PLAIN, 15));
    label.setForeground(new Color(50, 50, 50));

    add(label, BorderLayout.CENTER);
  }

  @Override
  public Dimension getMaximumSize() {
    var pref = getPreferredSize();
    return new Dimension(Integer.MAX_VALUE, pref.height);
  }
}
