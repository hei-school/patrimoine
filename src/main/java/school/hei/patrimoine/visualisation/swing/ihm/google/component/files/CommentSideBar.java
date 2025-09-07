package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.api.CommentApi;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;

public class CommentSideBar extends JPanel {
  private final CommentApi commentApi;

  public CommentSideBar() {
    this.commentApi = new CommentApi(AppContext.getDefault().getData("drive-api"));

    setLayout(new BorderLayout());

    addTopPanel();
  }

  void addTopPanel() {
    var topPanel = new JPanel(new BorderLayout());
    topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    var title = new JLabel("Commentaires");
    title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
    topPanel.add(title, BorderLayout.WEST);

    add(topPanel, BorderLayout.NORTH);
  }
}
