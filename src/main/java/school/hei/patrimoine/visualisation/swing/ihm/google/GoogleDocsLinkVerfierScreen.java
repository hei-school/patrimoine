package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.awt.BorderLayout.CENTER;
import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.compiler.ClassNameExtractor;
import school.hei.patrimoine.compiler.PatrimoineCompiler;
import school.hei.patrimoine.google.GoogleApi;
import school.hei.patrimoine.google.GoogleApi.GoogleAuthenticationDetails;
import school.hei.patrimoine.google.GoogleDocsLinkIdParser;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.swing.ihm.MainIHM;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedSnippet;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedString;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedURL;

@Slf4j
public class GoogleDocsLinkVerfierScreen {
  private final JFrame inputFrame;
  private final JPanel inputPanel;
  private final List<JTextField> inputFields;
  private final GoogleDocsLinkIdInputVerifier linkIdInputVerifier =
      new GoogleDocsLinkIdInputVerifier();
  private final GoogleDocsLinkIdParser linkIdParser = new GoogleDocsLinkIdParser();
  private final GoogleApi googleApi;
  private final GoogleAuthenticationDetails authDetails;

  public GoogleDocsLinkVerfierScreen(
      GoogleApi googleApi, GoogleAuthenticationDetails authDetails, List<NamedString> linksData) {
    this.googleApi = googleApi;
    this.authDetails = authDetails;
    inputFrame = newInputFrame();
    inputPanel = new JPanel(new GridBagLayout());
    inputFields = new ArrayList<>();

    addButtons(linksData);
    addInputFieldsFromData(linksData);
    configureInputFrame();
  }

  private void configureInputFrame() {
    inputFrame.getContentPane().add(inputPanel);
    inputFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    inputFrame.pack();
    inputFrame.setLocationRelativeTo(null);
  }

  private JFrame newInputFrame() {
    var inputFrame = new JFrame("Google Docs Submission");
    inputFrame.setSize(1200, 1000);
    inputFrame.setResizable(true);
    inputFrame.setVisible(true);
    return inputFrame;
  }

  private void addButtons(List<NamedString> linksData) {
    var submitButton = newSubmitButton(linksData);
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

  private JButton newSubmitButton(List<NamedString> linksData) {
    var submitButton = new JButton("Submit");
    submitButton.setPreferredSize(new Dimension(200, 50));
    submitButton.setFont(new Font("Arial", BOLD, 18));
    submitButton.setFocusPainted(false);
    submitButton.addActionListener(e -> loadDataInBackground(linksData));
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

  private void addInputFieldsFromData(List<NamedString> linksData) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.fill = HORIZONTAL;
    gbc.insets = new Insets(10, 50, 10, 50);

    int yPosition = 2;
    for (NamedString linkData : linksData) {
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

  private void loadDataInBackground(List<NamedString> linksData) {
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
            var ids = extractInputIds(linksData);
            List<NamedSnippet> codePatrimoinesVisualisables = new ArrayList<>();
            for (var id : ids) {
              var code = googleApi.readDocsContent(authDetails, String.valueOf(id.url()));
              NamedSnippet namedSnippet = new NamedSnippet(id.name(), code);
              codePatrimoinesVisualisables.add(namedSnippet);
            }
            List<Patrimoine> patrimoinesVisualisables = new ArrayList<>();
            PatrimoineCompiler patrimoineCompiler = new PatrimoineCompiler();
            for (NamedSnippet codePatrimoine : codePatrimoinesVisualisables) {
              String className = new ClassNameExtractor().apply(codePatrimoine.snippet());

              Patrimoine patrimoineVisualisable =
                  patrimoineCompiler.apply(className, codePatrimoine.snippet());
              patrimoinesVisualisables.add(patrimoineVisualisable);
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
              throw new RuntimeException(e);
            }
          }
        };

    worker.execute();
    loadingDialog.setVisible(true);
  }

  private List<NamedURL> extractInputIds(List<NamedString> linksData) {
    List<NamedURL> ids = new ArrayList<>();

    for (JTextField field : inputFields) {
      var rawText = field.getText();
      var parsedId = linkIdParser.apply(rawText.trim());
      String urlName = linksData.get(inputFields.indexOf(field)).name();
      NamedURL namedURL = new NamedURL(urlName, parsedId);
      ids.add(namedURL);
    }

    return ids;
  }

  private void openResultFrame(List<Patrimoine> patrimoinesVisualisables) {
    invokeLater(() -> new MainIHM(patrimoinesVisualisables));
  }
}
