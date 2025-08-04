package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.awt.Color.RED;
import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static javax.swing.SwingConstants.LEFT;
import static school.hei.patrimoine.google.GoogleApi.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.GoogleApi;
import school.hei.patrimoine.google.GoogleApi.GoogleAuthenticationDetails;
import school.hei.patrimoine.google.GoogleDocsLinkIdParser;
import school.hei.patrimoine.google.GoogleDriveLinkIdParser;
import school.hei.patrimoine.visualisation.swing.ihm.google.compiler.GoogleLinkListDownloader;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Screen;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.*;

@Slf4j
public class GoogleLinkVerifierScreen extends Screen {
  private final JFrame previousScreenFrame;
  private final JPanel inputPanel;
  private final List<JTextField> inputFields;
  private final GoogleDocsLinkIdInputVerifier docslinkIdInputVerifier =
      new GoogleDocsLinkIdInputVerifier();
  private final GoogleDriveLinkIdInputVerifier drivelinkIdInputVerifier =
      new GoogleDriveLinkIdInputVerifier();
  private final GoogleDocsLinkIdParser docsLinkIdParser = new GoogleDocsLinkIdParser();
  private final GoogleDriveLinkIdParser driveLinkIdParser = new GoogleDriveLinkIdParser();
  private final GoogleApi googleApi;
  private final GoogleAuthenticationDetails authDetails;
  private final GoogleLinkList<NamedString> linksData;
  private final GoogleLinkListDownloader googleLinkListDownloader;
  private int inputYPosition;

  public GoogleLinkVerifierScreen(
      GoogleApi googleApi,
      GoogleAuthenticationDetails authDetails,
      GoogleLinkListDownloader googleLinkListDownloader,
      GoogleLinkList<NamedString> linksData,
      JFrame jFrame) {

    super("Liens Google", 1200, 1000);

    this.inputYPosition = 0;
    this.googleApi = googleApi;
    this.linksData = linksData;
    this.authDetails = authDetails;
    this.previousScreenFrame = jFrame;
    this.googleLinkListDownloader = googleLinkListDownloader;

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
    addNewGoogleDocsTextFields(linksData.docsLinkList());
    addNewGoogleDriveTextFields(linksData.driveLinkList());
  }

  private void addNewGoogleDocsTextFields(List<NamedString> docsLinks) {
    docsLinks.forEach(this::addDocsLinkTextField);
  }

  private void addNewGoogleDriveTextFields(List<NamedString> driveLinks) {
    driveLinks.forEach(this::addDriveLinkTextField);
  }

  private GridBagConstraints createGridBagConstraints() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.fill = HORIZONTAL;
    gbc.insets = new Insets(10, 0, 0, 0);
    return gbc;
  }

  private void addDocsLinkTextField(NamedString linkData) {
    GridBagConstraints gbc = createGridBagConstraints();

    JLabel nameLabel = new JLabel(linkData.name());
    nameLabel.setFont(new Font("Arial", BOLD, 18));
    nameLabel.setHorizontalAlignment(LEFT);
    gbc.gridy = nextYPosition();
    inputPanel.add(nameLabel, gbc);

    JTextField newField = newGoogleDocsLinkTextField(linkData.value());
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

    JTextField newField = newGoogleDriveLinkTextField(linkData.value());
    inputFields.add(newField);

    gbc.gridy = nextYPosition();
    inputPanel.add(new JScrollPane(newField), gbc);
  }

  private int nextYPosition() {
    return inputYPosition++;
  }

  private JTextField newGoogleDocsLinkTextField(String initialValue) {
    var textField = new JTextField(70);
    textField.setInputVerifier(docslinkIdInputVerifier);
    textField.setFont(new Font("Arial", PLAIN, 16));
    textField.setText(initialValue);
    docslinkIdInputVerifier.verify(textField);
    return textField;
  }

  private JTextField newGoogleDriveLinkTextField(String initialValue) {
    var textField = new JTextField(70);
    textField.setInputVerifier(drivelinkIdInputVerifier);
    textField.setFont(new Font("Arial", PLAIN, 16));
    textField.setText(initialValue);
    drivelinkIdInputVerifier.verify(textField);
    return textField;
  }

  private ActionListener returnToPreviousScreen() {
    return e -> {
      previousScreenFrame.setVisible(true);
      setVisible(false);
    };
  }

  private void loadDataInBackground() {
    var loadingDialog = new Dialog(this, "Traitement", 300, 100);

    SwingWorker<GoogleLinkList<NamedID>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected GoogleLinkList<NamedID> doInBackground() {
            var ids = extractInputIds();

            resetIfExist(DOWNLOADS_DIRECTORY_PATH);
            var patrimoineJarId = driveLinkIdParser.apply(PATRIMOINE_JAR_URL);

            googleApi.downloadJarDependencyFile(authDetails, patrimoineJarId);
            return googleLinkListDownloader.apply(ids);
          }

          @Override
          protected void done() {
            loadingDialog.dispose();
            try {
              openResultFrame(get());
            } catch (InterruptedException | ExecutionException e) {
              Throwable cause = e.getCause();
              if (cause instanceof RuntimeException
                  && cause.getMessage().contains("Objectifs non atteints")) {
                showErrorPage("Objectifs non atteints");
              } else {
                showErrorPage("Veuillez vérifier le contenu de vos documents");
              }
              throw new RuntimeException(e);
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

      if (rawText.startsWith("https://drive.google.com/")) {
        var parsedId = driveLinkIdParser.apply(rawText.trim());
        String urlName = linksData.driveLinkList().get(inputFields.indexOf(field)).name();
        driveIds.add(new NamedID(urlName, parsedId));
      } else {
        var parsedId = docsLinkIdParser.apply(rawText.trim());
        String urlName = linksData.docsLinkList().get(inputFields.indexOf(field)).name();
        docsIds.add(new NamedID(urlName, parsedId));
      }
    }

    return new GoogleLinkList<>(docsIds, driveIds);
  }

  private void showErrorPage(String errorMessage) {
    var errorFrame = new JFrame("Erreur");
    errorFrame.setSize(400, 200);
    errorFrame.setLocationRelativeTo(null);
    errorFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    var panel = new JPanel(new BorderLayout());
    var errorLabel = new JLabel(errorMessage, SwingConstants.CENTER);
    errorLabel.setFont(new Font("Arial", BOLD, 16));
    errorLabel.setForeground(RED);
    panel.add(errorLabel, BorderLayout.CENTER);

    errorFrame.getContentPane().add(panel);
    errorFrame.setVisible(true);
  }

  private void openResultFrame(GoogleLinkList<NamedID> ids) {
    // TODO: show patrilang viewer
  }
}
