package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static javax.swing.BorderFactory.createEmptyBorder;

import java.awt.*;
import javax.swing.*;
import school.hei.patrimoine.google.model.Pagination;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class CommentFooter extends JPanel {
  public CommentFooter(State state) {
    setLayout(new FlowLayout(FlowLayout.RIGHT));
    setBorder(createEmptyBorder(10, 10, 10, 10));

    add(prevButton(state));
    add(nextButton(state));
  }

  private static Button prevButton(State state) {
    return new Button(
        "Précédente",
        e -> {
          Pagination pagination = state.get("pagination");
          state.update("pagination", pagination.prev());
        });
  }

  private static Button nextButton(State state) {
    return new Button(
        "Suivante",
        e -> {
          Pagination pagination = state.get("pagination");
          state.update("pagination", pagination.next());
        });
  }
}
