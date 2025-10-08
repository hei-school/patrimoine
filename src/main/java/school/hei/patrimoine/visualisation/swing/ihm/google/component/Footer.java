package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.util.Set;
import java.util.stream.IntStream;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class Footer extends JPanel {
  private final State state;

  private final Button previousPageButton;
  private final JComboBox<Integer> pageSelector;
  private final Button nextPageButton;

  public Footer(State state) {
    this.state = state;

    setLayout(new FlowLayout(FlowLayout.RIGHT));
    setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

    previousPageButton = new Button("Précédente");
    previousPageButton.addActionListener(e -> goToPreviousPage());

    previousPageButton.setFont(new Font("Arial", 0, 14));
    previousPageButton.setBackground(Color.WHITE);
    previousPageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    previousPageButton.setPreferredSize(new Dimension(110, 40));

    pageSelector = new JComboBox<>();
    pageSelector.setFont(new Font("Arial", Font.PLAIN, 14));
    pageSelector.setPreferredSize(new Dimension(70, 28));
    pageSelector.setCursor(new Cursor(Cursor.HAND_CURSOR));
    pageSelector.setRenderer(
        new DefaultListCellRenderer() {
          @Override
          public Component getListCellRendererComponent(
              JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label =
                (JLabel)
                    super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
            label.setOpaque(true);
            label.setHorizontalAlignment(CENTER);

            label.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            label.setBackground(isSelected ? new Color(100, 150, 255) : Color.WHITE);
            label.setForeground(isSelected ? Color.WHITE : Color.BLACK);

            return label;
          }
        });

    nextPageButton = new Button("Suivante");
    nextPageButton.addActionListener(e -> goToNextPage());

    nextPageButton.setFont(new Font("Arial", 0, 14));
    nextPageButton.setBackground(Color.WHITE);
    nextPageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    nextPageButton.setPreferredSize(new Dimension(110, 40));

    add(previousPageButton);
    add(pageSelector);
    add(nextPageButton);

    state.update("currentPage", 1);
    state.update("totalPages", 1);

    state.subscribe(Set.of("currentPage", "totalPages"), () -> updatePageSelector());

    pageSelector.addActionListener(
        e -> {
          if (pageSelector.getSelectedItem() != null) {
            int selectedPage = (Integer) pageSelector.getSelectedItem();
            state.update("currentPage", selectedPage);
          }
        });
  }

  private void goToPreviousPage() {
    int current = (int) state.get("currentPage");
    if (current > 1) {
      state.update("currentPage", current - 1);
    }
  }

  private void goToNextPage() {
    int current = (int) state.get("currentPage");
    int total = (int) state.get("totalPages");
    if (current < total) {
      state.update("currentPage", current + 1);
    }
  }

  private void updatePageSelector() {
    int totalPages = (int) state.get("totalPages");
    int currentPage = (int) state.get("currentPage");

    pageSelector.removeAllItems();
    IntStream.rangeClosed(1, totalPages).forEach(pageSelector::addItem);
    pageSelector.setSelectedItem(currentPage);

    previousPageButton.setEnabled(currentPage > 1);
    nextPageButton.setEnabled(currentPage < totalPages);
  }
}
