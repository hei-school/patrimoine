package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.*;
import javax.swing.*;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.ImageComponent;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.NavigateButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.RecoupementPage;

@Getter
public class AppBar extends JPanel {
    private ViewMode currentMode;
    private int controlledFontSize;
    private final Runnable stateHandler;
    private final Conf conf;

    public AppBar(Runnable stateHandler) {
        this(stateHandler, new Conf(true, true, true, true, true, List.of()));
    }

    public AppBar(Runnable stateHandler, Conf conf) {
        super(new BorderLayout());

        this.conf = conf;
        this.controlledFontSize = 14;
        this.currentMode = ViewMode.VIEW;
        this.stateHandler = stateHandler;

        add(leftControls(), BorderLayout.WEST);
        add(rightControls(), BorderLayout.EAST);
    }

    private JPanel leftControls() {
        var leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        conf.customComponents().forEach(leftControls::add);

        if(conf.withModeSelect()) {
            var modeSelect = new JComboBox<>(ViewMode.values());
            modeSelect.setSelectedItem(currentMode);
            modeSelect.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            modeSelect.setCursor(new Cursor(Cursor.HAND_CURSOR));

            modeSelect.addActionListener(
                    e -> {
                        currentMode = (ViewMode) modeSelect.getSelectedItem();
                        stateHandler.run();
                    });

            leftControls.add(modeSelect);
        }

        if(conf.withSaveButton()) {
            var saveButton = new Button("Enregistrer");
            saveButton.addActionListener(e -> saveSelectedFile());

            leftControls.add(saveButton);
        }

        if(conf.withGraphicButton()) {
            var graphicButton = new Button("Graphiques");
            leftControls.add(graphicButton);
        }

        if(conf.withSyncButton()) {
            var syncButton = new Button("Synchroniser avec Drive");
            leftControls.add(syncButton);
            syncButton.addActionListener(e -> syncSelectedFileWithDrive());
        }

        if(conf.withRecoupementButton()){
            var recoupementButton = new NavigateButton("Recoupement", RecoupementPage.PAGE_NAME);
            leftControls.add(recoupementButton);
        }
        return leftControls;
    }

    private void saveSelectedFile() {
        if (currentMode != ViewMode.EDIT) {
            showMessageDialog(
                    this,
                    "Vous devez être en mode édition pour sauvegarder.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void syncSelectedFileWithDrive() {
    }

    private JPanel rightControls() {
        var increaseFontButton = new Button("+");
        var decreaseFontButton = new Button("-");

        var fontSizeField = new JTextField(String.valueOf(controlledFontSize), 3);
        fontSizeField.setBorder(
                BorderFactory.createCompoundBorder(
                        fontSizeField.getBorder(), BorderFactory.createEmptyBorder(8, 6, 8, 6)));

        increaseFontButton.addActionListener(e -> adjustControlledFontSize(1, fontSizeField));
        decreaseFontButton.addActionListener(e -> adjustControlledFontSize(-1, fontSizeField));
        fontSizeField.addActionListener(
                e -> {
                    try {
                        controlledFontSize = Integer.parseInt(fontSizeField.getText());
                        controlledFontSize = Math.max(8, controlledFontSize);
                    } catch (NumberFormatException ex) {
                        controlledFontSize = 14;
                    }

                    fontSizeField.setText(String.valueOf(controlledFontSize));
                    stateHandler.run();
                });

        var rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightControls.add(decreaseFontButton);
        rightControls.add(fontSizeField);
        rightControls.add(increaseFontButton);
        rightControls.add(createUserInfoPanel());

        return rightControls;
    }

    private JPanel createUserInfoPanel() {
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

    private void adjustControlledFontSize(int delta, JTextField fontSizeField) {
        controlledFontSize = Math.max(8, controlledFontSize + delta);

        fontSizeField.setText(String.valueOf(controlledFontSize));

        stateHandler.run();
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

    @Builder
    public record Conf(
            boolean withSyncButton,
            boolean withGraphicButton,
            boolean withSaveButton,
            boolean withRecoupementButton,
            boolean withModeSelect,
            List<Component> customComponents
    ){}
}
