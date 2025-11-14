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
    update(fileId, comments, true);
  }

  public void update(String fileId, List<Comment> comments, boolean withRemoveBtn) {
    removeAll();

    for (var comment : comments) {
      add(new CommentCard(parent, fileId, comment, withActions, withRemoveBtn, refresh));
      add(Box.createVerticalStrut(10));
    }

    revalidate();
    repaint();
    scrollToTop();
  }

  // reset scroll position to the top
  private void scrollToTop() {
    SwingUtilities.invokeLater(
        () -> {
          Container parent = getParent();
          if (parent instanceof JViewport viewport) {
            viewport.setViewPosition(new Point(0, 0));
          }
        });
  }

  public JScrollPane toScrollPane() {
    var scroll =
        new JScrollPane(
            this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.getVerticalScrollBar().setUnitIncrement(20);

    return scroll;
  }
}
