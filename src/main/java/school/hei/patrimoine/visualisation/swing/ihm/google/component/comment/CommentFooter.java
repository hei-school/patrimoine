package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import java.awt.*;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

public class CommentFooter extends JPanel {
  private final Button previousPageButton;
  private final Button nextPageButton;

  public CommentFooter(Runnable onPrevious, Runnable onNext) {
    setLayout(new FlowLayout(FlowLayout.RIGHT));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    previousPageButton = new Button("Précédente", e -> onPrevious.run());
    nextPageButton = new Button("Suivante", e -> onNext.run());

    add(previousPageButton);
    add(nextPageButton);
  }

  public void updateButtons(boolean hasPrevious, boolean hasNext) {
    previousPageButton.setEnabled(hasPrevious);
    nextPageButton.setEnabled(hasNext);
  }
}
