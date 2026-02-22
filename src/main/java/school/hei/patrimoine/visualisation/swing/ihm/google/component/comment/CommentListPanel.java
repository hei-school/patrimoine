package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.SelectedFileSupplier;

import static javax.swing.SwingUtilities.invokeLater;

public class CommentListPanel extends JPanel {
  private final Runnable refresh;
  private final Component parent;
  private final boolean withActions;

  public CommentListPanel(Component parent, boolean withActions, Runnable refresh) {
    this.parent = parent;
    this.refresh = refresh;
    this.withActions = withActions;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setOpaque(false);
    setDoubleBuffered(true);
    setAutoscrolls(true);

    update(Optional::empty, List.of());
  }

  public void update(SelectedFileSupplier file, List<Comment> comments) {
    removeAll();

    for (var comment : comments) {
      add(new CommentCard(file, parent, comment, withActions, refresh));
      add(Box.createVerticalStrut(10));
    }

    revalidate();
    repaint();
    scrollToTop();
  }

  // reset scroll position to the top
  private void scrollToTop() {
    invokeLater(
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
