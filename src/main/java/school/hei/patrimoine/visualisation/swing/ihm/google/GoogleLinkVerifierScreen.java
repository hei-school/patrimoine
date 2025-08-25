package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.awt.Color.RED;
import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingUtilities.invokeLater;
import static school.hei.patrimoine.google.api.GoogleApi.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.*;
import school.hei.patrimoine.google.api.*;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Screen;
import school.hei.patrimoine.visualisation.swing.ihm.google.downloader.GoogleLinkListDownloader;
import school.hei.patrimoine.visualisation.swing.ihm.google.downloader.PatriLangGoogleLinkListDownloader;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.*;

@Slf4j
public class GoogleLinkVerifierScreen extends Screen {
  private static final String DRIVE_URL_PREFIX = "https://drive.google.com/";

  private final DriveApi driveApi;
  private final AuthDetails authDetails;
  private final GoogleDocsLinkIdParser docsLinkIdParser;
  private final GoogleDriveLinkIdParser driveLinkIdParser;
  private final GoogleLinkListDownloader googleLinkListDownloader;
  private final GoogleDocsLinkIdInputVerifier docsLinkIdInputVerifier;
  private final GoogleDriveLinkIdInputVerifier driveLinkIdInputVerifier;

  private int inputYPosition;
  private final JPanel inputPanel;
  private final JFrame previousScreenFrame;
  private final List<JTextField> inputFields;
  private final GoogleLinkList<NamedString> linksData;

  public GoogleLinkVerifierScreen(
      AuthDetails authDetails, GoogleLinkList<NamedString> linksData, JFrame jFrame) {
    super("Liens Google", 1200, 1000);

    this.authDetails = authDetails;
    this.driveApi = new DriveApi(authDetails);
    this.docsLinkIdParser = new GoogleDocsLinkIdParser();
    this.driveLinkIdParser = new GoogleDriveLinkIdParser();
    this.docsLinkIdInputVerifier = new GoogleDocsLinkIdInputVerifier();
    this.driveLinkIdInputVerifier = new GoogleDriveLinkIdInputVerifier();
    this.googleLinkListDownloader =
        new PatriLangGoogleLinkListDownloader(driveApi, new DocsApi(authDetails));

    this.inputYPosition = 0;
    this.linksData = linksData;
    this.previousScreenFrame = jFrame;
    this.inputFields = new ArrayList<>();
    this.inputPanel = new JPanel(new GridBagLayout());

    addInputFieldsFromData();
    buildMainLayout();
  }

  private void buildMainLayout() {
    var mainPanel = new JPanel(new BorderLayout());

    var titleLabel = new JLabel("Soumettre vos liens Google");
    titleLabel.setFont(new Font("Arial", BOLD, 24));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
    mainPanel.add(titleLabel, BorderLayout.NORTH);

    var scrollPane = new JScrollPane(inputPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
    mainPanel.add(scrollPane, BorderLayout.CENTER);

    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
    buttonPanel.add(newReturnButton());
    buttonPanel.add(newSubmitButton());
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().add(mainPanel);
    pack();
  }

  private Button newSubmitButton() {
    var submitButton = new Button("Envoyer");
    submitButton.setPreferredSize(new Dimension(200, 50));
    submitButton.setFont(new Font("Arial", BOLD, 18));
    submitButton.addActionListener(e -> loadDataInBackground());
    return submitButton;
  }

  private Button newReturnButton() {
    var returnButton = new Button("Retour");
    returnButton.setPreferredSize(new Dimension(200, 50));
    returnButton.setFont(new Font("Arial", BOLD, 18));
    returnButton.addActionListener(returnToPreviousScreen());
    return returnButton;
  }

  private void addInputFieldsFromData() {
    linksData.docsLinkList().forEach(this::addDocsLinkTextField);
    linksData.driveLinkList().forEach(this::addDriveLinkTextField);
  }

  private GridBagConstraints createGridBagConstraints() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.fill = HORIZONTAL;
    gbc.insets = new Insets(10, 0, 0, 0);
    return gbc;
  }

  private void addDocsLinkTextField(NamedString linkData) {
    var gbc = createGridBagConstraints();

    var nameLabel = new JLabel(linkData.name());
    nameLabel.setFont(new Font("Arial", BOLD, 18));
    nameLabel.setHorizontalAlignment(LEFT);
    gbc.gridy = nextYPosition();
    inputPanel.add(nameLabel, gbc);

    var newField = newGoogleDocsLinkTextField(linkData.value());
    newField.setMargin(new Insets(6, 2, 2, 2));

    inputFields.add(newField);

    gbc.gridy = nextYPosition();
    inputPanel.add(new JScrollPane(newField), gbc);
  }

  private void addDriveLinkTextField(NamedString linkData) {
    GridBagConstraints gbc = createGridBagConstraints();

    JLabel nameLabel = new JLabel(linkData.name());
    nameLabel.setFont(new Font("Arial", BOLD, 18));
    nameLabel.setHorizontalAlignment(LEFT);
    gbc.gridy = nextYPosition();
    inputPanel.add(nameLabel, gbc);

    var newField = newGoogleDriveLinkTextField(linkData.value());
    newField.setMargin(new Insets(6, 2, 2, 2));
    inputFields.add(newField);

    gbc.gridy = nextYPosition();
    inputPanel.add(new JScrollPane(newField), gbc);
  }

  private int nextYPosition() {
    return inputYPosition++;
  }

  private JTextField newGoogleDocsLinkTextField(String initialValue) {
    var textField = new JTextField(70);
    textField.setInputVerifier(docsLinkIdInputVerifier);
    textField.setFont(new Font("Arial", PLAIN, 16));
    textField.setText(initialValue);
    docsLinkIdInputVerifier.verify(textField);
    return textField;
  }

  private JTextField newGoogleDriveLinkTextField(String initialValue) {
    var textField = new JTextField(70);
    textField.setInputVerifier(driveLinkIdInputVerifier);
    textField.setFont(new Font("Arial", PLAIN, 16));
    textField.setText(initialValue);
    driveLinkIdInputVerifier.verify(textField);
    return textField;
  }

  private ActionListener returnToPreviousScreen() {
    return e -> {
      previousScreenFrame.setVisible(true);
      setVisible(false);
    };
  }

  private void loadDataInBackground() {
    var loadingDialog = new Dialog(this, "Traitement...", 300, 100);

    SwingWorker<GoogleLinkList<NamedID>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected GoogleLinkList<NamedID> doInBackground() throws GoogleIntegrationException {
            var ids = extractInputIds();
            // resetIfExist(DOWNLOADS_DIRECTORY_PATH);

            // googleLinkListDownloader.apply(ids);
            return ids;
          }

          @Override
          protected void done() {
            loadingDialog.dispose();
            try {
              var ids = get();
              invokeLater(() -> new PatriLangViewerScreen(ids, driveApi, authDetails.user()));
              dispose();
            } catch (Exception e) {
              showErrorPage(e.getMessage());
            }
          }
        };

    worker.execute();
    loadingDialog.setVisible(true);
  }

  private GoogleLinkList<NamedID> extractInputIds() {
    List<NamedID> docsIds = new ArrayList<>();
    List<NamedID> driveIds = new ArrayList<>();

    for (JTextField field : inputFields) {
      var rawText = field.getText();

      if (rawText.startsWith(DRIVE_URL_PREFIX)) {
        var parsedId = driveLinkIdParser.apply(rawText.trim());
        var urlName = linksData.driveLinkList().get(inputFields.indexOf(field)).name();
        driveIds.add(new NamedID(urlName, parsedId));
      } else {
        var parsedId = docsLinkIdParser.apply(rawText.trim());
        var urlName = linksData.docsLinkList().get(inputFields.indexOf(field)).name();
        docsIds.add(new NamedID(urlName, parsedId));
      }
    }

    return new GoogleLinkList<>(docsIds, driveIds);
  }

  private static void showErrorPage(String message) {
    var errorFrame = new JFrame("Erreur");
    errorFrame.setSize(400, 200);
    errorFrame.setLocationRelativeTo(null);
    errorFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    var panel = new JPanel(new BorderLayout());
    var errorLabel = new JLabel(message, SwingConstants.CENTER);
    errorLabel.setFont(new Font("Arial", BOLD, 16));
    errorLabel.setForeground(RED);
    panel.add(errorLabel, BorderLayout.CENTER);

    errorFrame.getContentPane().add(panel);
    errorFrame.setVisible(true);
  }
}
