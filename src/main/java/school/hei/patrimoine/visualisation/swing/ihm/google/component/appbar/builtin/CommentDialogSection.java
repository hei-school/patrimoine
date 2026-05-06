package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import java.awt.*;
import java.util.Collection;
import java.util.Objects;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.CustomBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.GroupedByComment;

public class CommentDialogSection implements DialogSection {
  static final String COMMENT_ICON_PATH = "/icons/comment.png";
  private final Collection<GroupedByComment> pendings;

  public CommentDialogSection(Collection<GroupedByComment> pendings) {
    this.pendings = pendings;
  }

  @Override
  public boolean isEmpty() {
    return pendings.isEmpty();
  }

  @Override
  public void addTo(JPanel container) {
    container.add(getSectionTitle("Commentaires modifiés"));
    container.add(getCommentPanel());
  }

  private JPanel getCommentPanel() {
    var panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    panel.setBorder(
        CustomBorder.builder()
            .radius(20)
            .borderColor(new Color(180, 180, 180))
            .padding(10, 0)
            .thickness(1)
            .build());
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(new JLabel(getIcon(COMMENT_ICON_PATH)));
    panel.add(new JLabel(String.format("%d - Nouveaux commentaires modifiés", pendings.size())));
    return panel;
  }

  private static ImageIcon getIcon(String path) {
    var url = CommentDialogSection.class.getResource(path);
    var icon = new ImageIcon(Objects.requireNonNull(url));
    var scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    return new ImageIcon(scaled);
  }

  private static JLabel getSectionTitle(String message) {
    var label = new JLabel(message);
    label.setFont(new Font("Arial", Font.BOLD, 15));
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    label.setBorder(CustomBorder.builder().thickness(0).padding(0, 0, 3, 0).build());
    return label;
  }
}
