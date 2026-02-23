package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import java.awt.*;
import javax.swing.*;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.ImageComponent;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;

public class UserInfoPanel extends JPanel {
  public UserInfoPanel() {
    super(new BorderLayout());

    User user = AppContext.getDefault().getData("connected-user");

    add(avatarPanel(user), BorderLayout.WEST);
    add(textPanel(user), BorderLayout.CENTER);
  }

  private static JPanel textPanel(User user) {
    var nameLabel = new JLabel(user.displayName());
    nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));

    var emailLabel = new JLabel(user.email());
    emailLabel.setFont(emailLabel.getFont().deriveFont(Font.PLAIN, 12f));
    emailLabel.setForeground(Color.GRAY);

    var textPanel = new JPanel();
    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
    textPanel.setOpaque(false);
    textPanel.add(nameLabel);
    textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
    textPanel.add(emailLabel);

    return textPanel;
  }

  private static JPanel avatarPanel(User user) {
    var avatarLabel = new ImageComponent(user.avatarUrl(), user.displayName(), 40, true);
    var avatarPanel = new JPanel(new BorderLayout());
    avatarPanel.setOpaque(false);
    avatarPanel.add(avatarLabel, BorderLayout.CENTER);
    avatarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // espace à droite

    return avatarPanel;
  }
}
