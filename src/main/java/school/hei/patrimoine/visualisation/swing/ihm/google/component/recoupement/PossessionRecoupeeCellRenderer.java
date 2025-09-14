package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;

public class PossessionRecoupeeCellRenderer extends JPanel implements ListCellRenderer<Object> {
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private final JLabel label;

  public PossessionRecoupeeCellRenderer() {
    setLayout(new BorderLayout());

    label = new JLabel();
    label.setOpaque(true);
    label.setBorder(new EmptyBorder(10, 10, 10, 10));
    label.setFont(new Font("Arial", Font.PLAIN, 16));

    add(label, BorderLayout.CENTER);
    setBorder(new EmptyBorder(0, 0, 8, 0));
  }

  @Override
  public Component getListCellRendererComponent(
      JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

    if (!(value instanceof PossessionRecoupee recoupee)) {
      return this;
    }

    var dateStr = recoupee.possession().t().format(DATE_FORMATTER);
    var typeStr = recoupee.possession().getClass().getSimpleName(); // <-- type de la possession
    label.setText(dateStr + " - " + recoupee.possession().nom() + " (" + typeStr + ")");

    if (isSelected) {
      label.setBackground(list.getSelectionBackground());
      label.setForeground(list.getSelectionForeground());
      return this;
    }

    switch (recoupee.status()) {
      case IMPREVU -> label.setBackground(Color.RED);
      case NON_EXECUTE -> label.setBackground(Color.BLUE);
      case EXECUTE_AVEC_CORRECTION -> label.setBackground(Color.YELLOW);
      case EXECUTE_SANS_CORRECTION -> label.setBackground(Color.GREEN);
      default -> label.setBackground(Color.LIGHT_GRAY);
    }
    label.setForeground(Color.BLACK);
    return this;
  }
}
