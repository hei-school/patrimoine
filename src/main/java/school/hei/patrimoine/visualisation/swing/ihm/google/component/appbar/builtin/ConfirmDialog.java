package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.CustomBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

public class ConfirmDialog extends Dialog {
  @Getter private boolean confirmed = false;

  public ConfirmDialog(
      List<DialogSection> sections, String title, String question, String confirmLabel) {

    super(title, 800, 500, false);
    setLayout(new BorderLayout());
    setResizable(true);
    setMinimumSize(new Dimension(500, 400));

    addContentPanel(sections, question);
    addButtons(confirmLabel);

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void addContentPanel(List<DialogSection> sections, String question) {
    var messagePanel = new JPanel(new BorderLayout(15, 0));
    messagePanel.setBorder(CustomBorder.builder().thickness(0).padding(10, 15).build());

    var contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    sections.stream().filter(s -> !s.isEmpty()).forEach(s -> s.addTo(contentPanel));

    var scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(CustomBorder.builder().thickness(0).padding(0, 0).build());
    scrollPane.setMinimumSize(new Dimension(500, 400));
    scrollPane.setPreferredSize(new Dimension(500, 400));
    messagePanel.add(scrollPane, BorderLayout.CENTER);

    var questionLabel = new JLabel(question);
    questionLabel.setBorder(CustomBorder.builder().thickness(0).padding(10, 0, 0, 0).build());
    messagePanel.add(questionLabel, BorderLayout.SOUTH);

    add(messagePanel, BorderLayout.CENTER);
  }

  private void addButtons(String confirmLabel) {
    var buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setBorder(CustomBorder.builder().thickness(0).padding(10, 10).build());

    var cancelButton =
        new Button(
            "Annuler",
            e -> {
              confirmed = false;
              dispose();
            });
    var confirmButton =
        new Button(
            confirmLabel,
            e -> {
              confirmed = true;
              dispose();
            });

    var rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    rightPanel.add(cancelButton);
    rightPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    rightPanel.add(confirmButton);
    buttonPanel.add(rightPanel, BorderLayout.EAST);

    add(buttonPanel, BorderLayout.SOUTH);
  }
}
