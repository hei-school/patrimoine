package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.util.Set;
import java.util.stream.IntStream;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.model.Pagination;

public class Footer extends JPanel {
  private final State state;

  private final Button previousPageButton;
  private final JComboBox<Integer> pageSelector;
  private final Button nextPageButton;

  public Footer(State state) {
    this.state = state;

    setLayout(new FlowLayout(FlowLayout.RIGHT));
    setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

    previousPageButton = new Button("Précédente", e -> goToPreviousPage());
    previousPageButton.setFont(new Font("Arial", Font.PLAIN, 14));
    previousPageButton.setBackground(Color.WHITE);
    previousPageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    previousPageButton.setPreferredSize(new Dimension(110, 40));

    pageSelector = new JComboBox<>();
    pageSelector.setFont(new Font("Arial", Font.PLAIN, 14));
    pageSelector.setPreferredSize(new Dimension(70, 40));
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
    nextPageButton = new Button("Suivante", e -> goToNextPage());
    nextPageButton.setFont(new Font("Arial", Font.PLAIN, 14));
    nextPageButton.setBackground(Color.WHITE);
    nextPageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    nextPageButton.setPreferredSize(new Dimension(110, 40));

    state.subscribe(Set.of("totalPages", "pagination"), this::updatePageSelector);

    add(previousPageButton);
    add(pageSelector);
    add(nextPageButton);

    pageSelector.addActionListener(
        e -> {
          if (pageSelector.getSelectedItem() != null) {
            int selectedPage = (Integer) pageSelector.getSelectedItem();

            var currentPage = (Pagination) state.get("pagination");
            state.update("pagination", new Pagination(selectedPage, currentPage.size()));
          }
        });
  }

  private void goToPreviousPage() {
    Pagination current = state.get("pagination");

    if (current.page() > 1)
      state.update("pagination", current.toBuilder().page(current.page() - 1).build());
  }

  private void goToNextPage() {
    Pagination current = state.get("pagination");

    int total = state.get("totalPages");
    if (current.page() < total)
      state.update("pagination", current.toBuilder().page(current.page() + 1).build());
  }

  public void updatePageSelector() {
    int totalPages = state.get("totalPages");
    Pagination pagination = state.get("pagination");
    var currentPage = pagination.page();

    if (totalPages <= 0) {
      pageSelector.removeAllItems();
      return;
    }

    pageSelector.removeAllItems();
    IntStream.rangeClosed(1, totalPages).forEach(pageSelector::addItem);

    if (currentPage > totalPages) {
      currentPage = totalPages;
    }

    pageSelector.setSelectedItem(currentPage);

    previousPageButton.setEnabled(currentPage > 1);
    nextPageButton.setEnabled(currentPage < totalPages);
  }
}
