package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;
import static school.hei.patrimoine.google.DriveLinkIdParser.GOOGLE_DRIVE_ID_PATTERN;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.PageManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedLink;

public class SubmitLinkPage extends Page {
  public static final String PAGE_NAME = "submit-file-url";
  private final JTextArea inputField;

  public SubmitLinkPage() {
    super(PAGE_NAME);
    this.inputField = new JTextArea();

    setLayout(new BorderLayout());

    addTitle();
    addInputField();
    addSubmitButton();
  }

  private void addTitle() {
    var title = new JLabel("Saisir les liens Google :");
    title.setFont(new Font("Arial", BOLD, 24));
    title.setHorizontalAlignment(SwingConstants.CENTER);
    title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

    add(title, BorderLayout.NORTH);
  }

  private void addInputField() {
    inputField.setLineWrap(true);
    inputField.setWrapStyleWord(true);
    inputField.setFont(new Font("Arial", PLAIN, 16));

    var scrollPane = new JScrollPane(inputField);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());

    var centerWrapper = new JPanel(new BorderLayout());
    centerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50));
    centerWrapper.add(scrollPane, BorderLayout.CENTER);

    add(centerWrapper, BorderLayout.CENTER);
  }

  private void addSubmitButton() {
    var submitButton = new Button("Envoyer");
    submitButton.setFont(new Font("Arial", BOLD, 18));
    submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
    submitButton.setPreferredSize(new Dimension(200, 50));
    submitButton.addActionListener(e -> loadDataInBackground());

    var wrapper = new JPanel();
    wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
    wrapper.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
    wrapper.add(submitButton);

    add(wrapper, BorderLayout.SOUTH);
  }

  private void loadDataInBackground() {
    SwingWorker<List<NamedLink>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<NamedLink> doInBackground() {
            return extractDriveLinks();
          }

          @Override
          protected void done() {
            try {
              var ids = get();
              AppContext.getDefault().setData("named-links", ids);
              PageManager.navigateTo(LinkValidityPage.PAGE_NAME);
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }
        };

    worker.execute();
  }

  private Optional<NamedLink> extractDriveLink(String line) {
    var parts = line.split(":", 2);

    if (parts.length != 2) {
      return Optional.empty();
    }

    var linkName = parts[0].trim();
    var linkValue = parts[1].trim();
    return Optional.of(new NamedLink(linkName, linkValue));
  }

  private List<NamedLink> extractDriveLinks() {
    var rawText = inputField.getText();
    var lines = Arrays.asList(rawText.split("\n"));

    return lines.stream()
        .filter(line -> GOOGLE_DRIVE_ID_PATTERN.matcher(line).find())
        .map(this::extractDriveLink)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }
}
