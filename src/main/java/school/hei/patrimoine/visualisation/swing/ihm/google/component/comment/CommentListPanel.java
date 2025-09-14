package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.model.Comment;

public class CommentListPanel extends JPanel {
  private final Component parent;
  private final boolean withActions;
  private final Runnable refresh;

  public CommentListPanel(Component parent, boolean withActions, Runnable refresh) {
    this.parent = parent;
    this.refresh = refresh;
    this.withActions = withActions;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setOpaque(false);
    setDoubleBuffered(true);
    setAutoscrolls(true);

    update(null, List.of());
  }

  public void update(String fileId, List<Comment> comments) {
    removeAll();

    for (var comment : comments) {
      add(new CommentCard(fileId, comment, parent.getWidth() - 15, withActions, refresh));
      add(Box.createVerticalStrut(10));
    }

    revalidate();
    repaint();
  }
}
