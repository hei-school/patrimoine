package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.PossessionRecoupée;

public class PossessionRecoupéeCellRenderer extends JPanel implements ListCellRenderer<Object> {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JLabel label;

    public PossessionRecoupéeCellRenderer() {
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

        if (value instanceof PossessionRecoupée recoupée) {
            var dateStr = recoupée.possession().t().format(DATE_FORMATTER);
            label.setText(dateStr + " - " + recoupée.possession().nom());

            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                switch (recoupée.status()) {
                    case NON_PRÉVU -> label.setBackground(Color.RED);
                    case NON_ÉXECUTÉ -> label.setBackground(Color.BLUE);
                    case ÉXECUTÉ_AVEC_DIFFÉRENCE -> label.setBackground(Color.YELLOW);
                    case ÉXECUTÉ_SANS_DIFFÉRENCE -> label.setBackground(Color.GREEN);
                }
                label.setForeground(Color.BLACK);
            }
        }

        return this;
    }
}