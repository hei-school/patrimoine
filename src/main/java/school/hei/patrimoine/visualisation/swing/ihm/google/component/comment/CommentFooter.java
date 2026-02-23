package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static javax.swing.BorderFactory.createEmptyBorder;

import java.awt.*;
import javax.swing.*;
import school.hei.patrimoine.google.model.Pagination;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class CommentFooter extends JPanel {
  private final State state;
  private final Button nextButton;
  private final Button prevButton;

  public CommentFooter(State state) {
    setLayout(new FlowLayout(FlowLayout.RIGHT));
    setBorder(createEmptyBorder(10, 10, 10, 10));

    this.state = state;
    this.prevButton = prevButton(state, this::updateButtonsVisibilities);
    this.nextButton = nextButton(state, this::updateButtonsVisibilities);

    add(prevButton);
    add(nextButton);
  }

  private void updateButtonsVisibilities() {
    Pagination pagination = state.get("pagination");

    prevButton.setEnabled(pagination.hasPrev());
    nextButton.setEnabled(pagination.hasNext());
  }

  private static Button prevButton(State state, Runnable onFinish) {
    return new Button(
        "Précédente",
        e -> {
          Pagination pagination = state.get("pagination");
          state.update("pagination", pagination.prev());
          onFinish.run();
        });
  }

  private static Button nextButton(State state, Runnable onFinish) {
    return new Button(
        "Suivante",
        e -> {
          Pagination pagination = state.get("pagination");
          state.update("pagination", pagination.next());
          onFinish.run();
        });
  }
}
