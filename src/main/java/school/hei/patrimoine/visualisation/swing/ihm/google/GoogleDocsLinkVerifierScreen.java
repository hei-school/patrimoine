package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.awt.BorderLayout.CENTER;
import static java.awt.Color.RED;
import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.compiler.ClassNameExtractor;
import school.hei.patrimoine.compiler.PatrimoineCompiler;
import school.hei.patrimoine.compiler.PossessionExtractor;
import school.hei.patrimoine.google.GoogleApi;
import school.hei.patrimoine.google.GoogleApi.GoogleAuthenticationDetails;
import school.hei.patrimoine.google.GoogleDocsLinkIdParser;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.swing.ihm.MainIHM;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.LinkedPatrimoine;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedSnippet;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedString;

@Slf4j
public class GoogleDocsLinkVerifierScreen {
  private final JFrame inputFrame;
  private final JPanel inputPanel;
  private final List<JTextField> inputFields;
  private final GoogleDocsLinkIdInputVerifier linkIdInputVerifier =
      new GoogleDocsLinkIdInputVerifier();
  private final GoogleDocsLinkIdParser linkIdParser = new GoogleDocsLinkIdParser();
  private final GoogleApi googleApi;
  private final GoogleAuthenticationDetails authDetails;
  private final LinkedPatrimoine<NamedString> linksData;

  public GoogleDocsLinkVerifierScreen(
      GoogleApi googleApi,
      GoogleAuthenticationDetails authDetails,
      LinkedPatrimoine<NamedString> linksData) {
    this.googleApi = googleApi;
    this.authDetails = authDetails;
    this.linksData = linksData;
    inputFrame = newInputFrame();
    inputPanel = new JPanel(new GridBagLayout());
    inputFields = new ArrayList<>();

    addButtons();
    addInputFieldsFromData();
    configureInputFrame();
  }

  private void configureInputFrame() {
    inputFrame.getContentPane().add(inputPanel);
    inputFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    inputFrame.pack();
    inputFrame.setLocationRelativeTo(null);
  }

  private JFrame newInputFrame() {
    var inputFrame = new JFrame("Google Docs Verifier");
    inputFrame.setSize(1200, 1000);
    inputFrame.setResizable(true);
    inputFrame.setVisible(true);
    return inputFrame;
  }

  private void addButtons() {
    var submitButton = newSubmitButton();
    var returnButton = newReturnButton();

    var buttonTitle = new JLabel("Submit Your Google Docs Links:");
    buttonTitle.setFont(new Font("Arial", BOLD, 24));
    buttonTitle.setHorizontalAlignment(SwingConstants.CENTER);

    var buttonPanel = new JPanel();
    buttonPanel.add(returnButton);
    buttonPanel.add(submitButton);
    buttonPanel.setOpaque(false);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(10, 0, 10, 0);
    gbc.anchor = GridBagConstraints.CENTER;
    inputPanel.add(buttonTitle, gbc);

    gbc.gridy = 1;
    inputPanel.add(buttonPanel, gbc);
  }

  private JButton newSubmitButton() {
    var submitButton = new JButton("Submit");
    submitButton.setPreferredSize(new Dimension(200, 50));
    submitButton.setFont(new Font("Arial", BOLD, 18));
    submitButton.setFocusPainted(false);
    submitButton.addActionListener(e -> loadDataInBackground());
    return submitButton;
  }

  private JButton newReturnButton() {
    var returnButton = new JButton("Return");
    returnButton.setPreferredSize(new Dimension(200, 50));
    returnButton.setFont(new Font("Arial", BOLD, 18));
    returnButton.setFocusPainted(false);
    returnButton.addActionListener(returnToPreviousScreen());
    return returnButton;
  }

  private void addInputFieldsFromData() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.fill = HORIZONTAL;
    gbc.insets = new Insets(10, 50, 10, 50);

    int yPosition = 2;
    for (NamedString linkData : linksData.patrimoineLinkList()) {
      var nameLabel = new JLabel(linkData.name());
      nameLabel.setFont(new Font("Arial", BOLD, 18));
      nameLabel.setHorizontalAlignment(LEFT);

      gbc.gridy = yPosition++;
      inputPanel.add(nameLabel, gbc);

      JTextField newField = newGoogleDocsLinkTextField(linkData.value());
      inputFields.add(newField);

      JScrollPane scrollPane = new JScrollPane(newField);
      gbc.gridy = yPosition++;
      inputPanel.add(scrollPane, gbc);
    }
  }

  private JTextField newGoogleDocsLinkTextField(String initialValue) {
    var textField = new JTextField(70);
    textField.setInputVerifier(linkIdInputVerifier);
    textField.setFont(new Font("Arial", PLAIN, 16));
    textField.setText(initialValue);
    linkIdInputVerifier.verify(textField);
    return textField;
  }

  private ActionListener returnToPreviousScreen() {
    return e -> {
      invokeLater(() -> new GoogleDocsSubmitScreen(googleApi, authDetails));
      inputFrame.setVisible(false);
    };
  }

  private void loadDataInBackground() {
    var loadingDialog = new JDialog(inputFrame, "Processing", true);
    var loadingLabel = new JLabel("Processing, please wait...");
    loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
    loadingDialog.getContentPane().add(loadingLabel, CENTER);
    loadingDialog.setSize(300, 100);
    loadingDialog.setLocationRelativeTo(inputFrame);

    SwingWorker<List<Patrimoine>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Patrimoine> doInBackground() {
            var ids = extractInputIds();
            List<NamedSnippet> codePatrimoinesVisualisables = new ArrayList<>();
            List<Patrimoine> patrimoinesVisualisables = new ArrayList<>();

            for (var id : ids.patrimoineLinkList()) {
              codePatrimoinesVisualisables.add(extractSnippet(id));
            }

            if (!Objects.equals(ids.possessionLink(), "")) {
              var parsedVariable = linkIdParser.apply(ids.possessionLink().trim());
              var possessionContent =
                  googleApi.readDocsContent(authDetails, String.valueOf(parsedVariable));
              PossessionExtractor possessionExtractor = new PossessionExtractor();
              var possessionsData = possessionExtractor.apply(possessionContent);

              for (NamedSnippet codePatrimoine : codePatrimoinesVisualisables) {
                patrimoinesVisualisables.add(
                    compilePatrimoine(
                        possessionsData.imports(), possessionsData.possessions(), codePatrimoine));
              }
            } else {
              for (NamedSnippet codePatrimoine : codePatrimoinesVisualisables) {
                patrimoinesVisualisables.add(compilePatrimoine(codePatrimoine));
              }
            }

            return patrimoinesVisualisables;
          }

          @Override
          protected void done() {
            loadingDialog.dispose();
            try {
              final List<Patrimoine> patrimoinesVisualisables = get();
              openResultFrame(patrimoinesVisualisables);
            } catch (InterruptedException | ExecutionException e) {
              showErrorPage("Veuillez vérifier le contenu de vos documents");
              throw new RuntimeException(e);
            }
          }
        };

    worker.execute();
    loadingDialog.setVisible(true);
  }

  private LinkedPatrimoine<NamedID> extractInputIds() {
    List<NamedID> ids = new ArrayList<>();

    for (JTextField field : inputFields) {
      var rawText = field.getText();
      var parsedId = linkIdParser.apply(rawText.trim());
      String urlName = linksData.patrimoineLinkList().get(inputFields.indexOf(field)).name();
      NamedID namedURL = new NamedID(urlName, parsedId);
      ids.add(namedURL);
    }

    return new LinkedPatrimoine<>(linksData.possessionLink(), ids);
  }

  private NamedSnippet extractSnippet(NamedID namedID) {
    var code = googleApi.readDocsContent(authDetails, String.valueOf(namedID.id()));
    return new NamedSnippet(namedID.name(), code);
  }

  private void showErrorPage(String errorMessage) {
    JFrame errorFrame = new JFrame("Erreur");
    errorFrame.setSize(400, 200);
    errorFrame.setLocationRelativeTo(null);
    errorFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JLabel errorLabel = new JLabel(errorMessage, SwingConstants.CENTER);
    errorLabel.setFont(new Font("Arial", BOLD, 16));
    errorLabel.setForeground(RED);

    panel.add(errorLabel, BorderLayout.CENTER);

    errorFrame.getContentPane().add(panel);
    errorFrame.setVisible(true);
  }

  private Patrimoine compilePatrimoine(
      String imports, String possessions, NamedSnippet namedSnippet) {
    PatrimoineCompiler patrimoineCompiler = new PatrimoineCompiler();
    String className = new ClassNameExtractor().apply(namedSnippet.snippet());

    String snippet = imports + "\n" + namedSnippet.snippet();

    int getMethodIndex = snippet.indexOf("get() {");
    if (getMethodIndex != -1) {
      int insertPosition = getMethodIndex + "get() {".length();
      snippet =
          snippet.substring(0, insertPosition)
              + "\n"
              + possessions
              + "\n"
              + snippet.substring(insertPosition);
    }

    return (patrimoineCompiler.apply(className, snippet));
  }

  private Patrimoine compilePatrimoine(NamedSnippet namedSnippet) {
    PatrimoineCompiler patrimoineCompiler = new PatrimoineCompiler();
    String className = new ClassNameExtractor().apply(namedSnippet.snippet());

    return (patrimoineCompiler.apply(className, namedSnippet.snippet()));
  }

  private void openResultFrame(List<Patrimoine> patrimoinesVisualisables) {
    invokeLater(() -> new MainIHM(patrimoinesVisualisables));
  }
}
