package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.awt.Font.*;
import static javax.swing.SwingUtilities.invokeLater;
import static school.hei.patrimoine.google.GoogleDocsLinkIdParser.GOOGLE_DOCS_ID_PATTERN;
import static school.hei.patrimoine.google.GoogleDriveLinkIdParser.GOOGLE_DRIVE_ID_PATTERN;
import static school.hei.patrimoine.google.api.GoogleApi.AuthDetails;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Screen;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleDocsLinkIdInputVerifier;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedString;

@Slf4j
public class GoogleSubmitApp extends Screen {
  private final AuthDetails authDetails;
  private final JPanel inputPanel;
  private final JTextArea inputField;
  private final GoogleDocsLinkIdInputVerifier linkIdInputVerifier;

  public GoogleSubmitApp(AuthDetails authDetails) {
    super("Soumission des liens Google", 1200, 1000);

    this.authDetails = authDetails;
    this.inputField = new JTextArea();
    this.inputPanel = new JPanel(new BorderLayout());
    this.linkIdInputVerifier = new GoogleDocsLinkIdInputVerifier();

    addTitle();
    setResizable(true);
    addInputField();
    addSubmitButton();

    getContentPane().add(inputPanel);
    pack();
  }

  private void addTitle() {
    var title = new JLabel("Saisir les liens Google :");
    title.setFont(new Font("Arial", BOLD, 24));
    title.setHorizontalAlignment(SwingConstants.CENTER);
    title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
    inputPanel.add(title, BorderLayout.NORTH);
  }

  private void addInputField() {
    inputField.setLineWrap(true);
    inputField.setWrapStyleWord(true);
    inputField.setInputVerifier(linkIdInputVerifier);
    inputField.setFont(new Font("Arial", PLAIN, 16));

    var scrollPane = new JScrollPane(inputField);
    scrollPane.setPreferredSize(new Dimension(1000, 250));

    var centerWrapper = new JPanel(new BorderLayout());
    centerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
    centerWrapper.add(scrollPane, BorderLayout.CENTER);

    inputPanel.add(centerWrapper, BorderLayout.CENTER);
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

    inputPanel.add(wrapper, BorderLayout.SOUTH);
  }

  private void loadDataInBackground() {
    var loadingDialog = new Dialog(this, "Traitement...", 300, 100);
    var owner = this;

    SwingWorker<GoogleLinkList<NamedString>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected GoogleLinkList<NamedString> doInBackground() {
            return extractGoogleLinks();
          }

          @Override
          protected void done() {
            loadingDialog.dispose();
            try {
              var ids = get();
              invokeLater(() -> new GoogleLinkVerifierApp(authDetails, ids, owner));
              setVisible(false);
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }
        };

    worker.execute();
    loadingDialog.setVisible(true);
  }

  private List<NamedString> extractInputData(List<String> lines) {
    List<NamedString> linkDataList = new ArrayList<>();

    for (String line : lines) {
      String[] parts = line.split(":", 2);

      if (parts.length == 2) {
        String linkName = parts[0].trim();
        String linkValue = parts[1].trim();

        NamedString linkData = new NamedString(linkName, linkValue);
        linkDataList.add(linkData);
      }
    }

    return linkDataList;
  }

  private GoogleLinkList<NamedString> extractGoogleLinks() {
    String rawText = inputField.getText();
    ArrayList<String> docsLines = new ArrayList<>();
    ArrayList<String> driveLines = new ArrayList<>();

    List<String> lines = Arrays.asList(rawText.split("\n"));

    lines.forEach(
        line -> {
          if (GOOGLE_DOCS_ID_PATTERN.matcher(line).find()) {
            docsLines.add(line);
          } else if (GOOGLE_DRIVE_ID_PATTERN.matcher(line).find()) {
            driveLines.add(line);
          }
        });

    List<NamedString> docsLink = extractInputData(docsLines);
    List<NamedString> driveLink = extractInputData(driveLines);

    return new GoogleLinkList<>(docsLink, driveLink);
  }
}
