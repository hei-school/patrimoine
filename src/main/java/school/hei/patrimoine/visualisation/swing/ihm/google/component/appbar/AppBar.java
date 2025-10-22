package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.ImageComponent;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

@Getter
public class AppBar extends JPanel {
  private final List<Component> leftComponents;
  private final List<Component> rightComponents;

  public AppBar(List<Component> leftComponents, List<Component> rightComponents) {
    super(new BorderLayout());

    this.leftComponents = leftComponents;
    this.rightComponents = rightComponents;

    add(leftControls(), BorderLayout.WEST);
    add(rightControls(), BorderLayout.EAST);
  }

  private JPanel leftControls() {
    var leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftComponents.forEach(leftControls::add);

    return leftControls;
  }

  private JPanel rightControls() {
    var rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightComponents.forEach(rightControls::add);

    return rightControls;
  }

  private static void adjustControlledFontSize(State state, int delta, JTextField fontSizeField) {
    state.update("fontSize", Math.max(8, (int) state.get("fontSize") + delta));
    fontSizeField.setText(String.valueOf((int) state.get("fontSize")));
  }

  public static JComboBox<ViewMode> builtInViewModeSelect(State state) {
    var modeSelect = new JComboBox<>(ViewMode.values());
    modeSelect.setSelectedItem(state.get("viewMode"));
    modeSelect.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    modeSelect.setCursor(new Cursor(Cursor.HAND_CURSOR));
    modeSelect.addActionListener(e -> state.update("viewMode", modeSelect.getSelectedItem()));

    return modeSelect;
  }

  public static JPanel builtInFontSizeControllerButton(State state) {
    var increaseFontButton = new Button("+");
    var decreaseFontButton = new Button("-");

    var fontSizeField = new JTextField(String.valueOf((int) state.get("fontSize")), 3);
    fontSizeField.setBorder(
        BorderFactory.createCompoundBorder(
            fontSizeField.getBorder(), BorderFactory.createEmptyBorder(8, 6, 8, 6)));

    increaseFontButton.addActionListener(e -> adjustControlledFontSize(state, 1, fontSizeField));
    decreaseFontButton.addActionListener(e -> adjustControlledFontSize(state, -1, fontSizeField));
    fontSizeField.addActionListener(
        e -> {
          try {
            var newFontSize = Integer.parseInt(fontSizeField.getText());
            state.update("fontSize", Math.max(8, newFontSize));
          } catch (NumberFormatException ex) {
            state.update("fontSize", 14);
          }

          fontSizeField.setText(String.valueOf((int) state.get("fontSize")));
        });

    var fontSizeControllerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    fontSizeControllerPanel.add(decreaseFontButton);
    fontSizeControllerPanel.add(fontSizeField);
    fontSizeControllerPanel.add(increaseFontButton);

    return fontSizeControllerPanel;
  }

  public static JPanel builtInUserInfoPanel() {
    User user = AppContext.getDefault().getData("connected-user");

    var panel = new JPanel(new BorderLayout());
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    var avatarLabel = new ImageComponent(user.avatarUrl(), user.displayName(), 40, true);
    var avatarPanel = new JPanel(new BorderLayout());
    avatarPanel.setOpaque(false);
    avatarPanel.add(avatarLabel, BorderLayout.CENTER);
    avatarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // espace à droite

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

    panel.add(avatarPanel, BorderLayout.WEST);
    panel.add(textPanel, BorderLayout.CENTER);

    return panel;
  }

  @Getter
  public enum ViewMode {
    VIEW("Affichage"),
    EDIT("Édition");

    private final String label;

    ViewMode(String label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return label;
    }
  }
}
