package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import java.awt.*;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class CommentFooter extends JPanel {
  private final State state;

  private final Button previousPageButton;
  private final Button nextPageButton;
  private final JLabel pageNumber;

  public CommentFooter(State state) {
    this.state = state;

    setLayout(new FlowLayout(FlowLayout.RIGHT));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    previousPageButton = new Button("Précédente", e -> goToPreviousPage());
    previousPageButton.setFont(new Font("Arial", Font.PLAIN, 14));
    previousPageButton.setBackground(Color.WHITE);
    previousPageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    previousPageButton.setPreferredSize(new Dimension(110, 40));

    nextPageButton = new Button("Suivante", e -> goToNextPage());
    nextPageButton.setFont(new Font("Arial", Font.PLAIN, 14));
    nextPageButton.setBackground(Color.WHITE);
    nextPageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    nextPageButton.setPreferredSize(new Dimension(110, 40));

    pageNumber = new JLabel();
    pageNumber.setFont(new Font("Arial", Font.PLAIN, 14));
    pageNumber.setPreferredSize(new Dimension(70, 40));
    pageNumber.setCursor(new Cursor(Cursor.HAND_CURSOR));

    add(previousPageButton);
    add(pageNumber);
    add(nextPageButton);
  }

  private void goToPreviousPage() {}

  private void goToNextPage() {}
}
