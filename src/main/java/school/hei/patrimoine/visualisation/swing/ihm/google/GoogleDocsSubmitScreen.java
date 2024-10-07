package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.awt.BorderLayout.CENTER;
import static java.awt.Font.BOLD;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.GoogleApi;
import school.hei.patrimoine.google.GoogleApi.GoogleAuthenticationDetails;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.ExtractedData;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedString;

@Slf4j
public class GoogleDocsSubmitScreen {
  private final JFrame inputFrame;
  private final JPanel inputPanel;
  private final JTextArea inputField;
  private final JTextField variableField;
  private final GoogleDocsLinkIdInputVerifier linkIdInputVerifier =
      new GoogleDocsLinkIdInputVerifier();
  private final GoogleApi googleApi;
  private final GoogleAuthenticationDetails authDetails;

  public GoogleDocsSubmitScreen(GoogleApi googleApi, GoogleAuthenticationDetails authDetails) {
    this.googleApi = googleApi;
    this.authDetails = authDetails;
    inputFrame = newInputFrame();
    inputPanel = new JPanel();
    inputPanel.setLayout(new GridBagLayout());

    inputField = new JTextArea(5, 70);
    variableField = new JTextField();
    addButtons();
    addInitialInput();

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

  private void addButtons() {
    JButton submitButton = newSubmitButton();

    JLabel buttonTitle = new JLabel("Enter Your Google Docs Links:");
    buttonTitle.setFont(new Font("Arial", BOLD, 24));
    buttonTitle.setHorizontalAlignment(SwingConstants.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(submitButton);
    buttonPanel.setOpaque(false);

    var gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(10, 0, 10, 0);
    gbc.anchor = GridBagConstraints.CENTER;
    inputPanel.add(buttonTitle, gbc);

    gbc.gridy = 1;
    inputPanel.add(buttonPanel, gbc);
  }

  private JButton newSubmitButton() {
    var submitButton = new JButton("Verify");
    submitButton.setPreferredSize(new Dimension(200, 50));
    submitButton.setFont(new Font("Arial", BOLD, 18));
    submitButton.setFocusPainted(false);
    submitButton.addActionListener(e -> loadDataInBackground());
    return submitButton;
  }

  private void addInitialInput() {

    var variableLabel = new JLabel("Variables partagées");
    var patrimoinesLabel = new JLabel("Patrimoines");

    variableLabel.setFont(new Font("Arial", Font.BOLD, 18));
    variableLabel.setHorizontalAlignment(SwingConstants.LEFT);
    patrimoinesLabel.setFont(new Font("Arial", Font.BOLD, 18));
    patrimoinesLabel.setHorizontalAlignment(SwingConstants.LEFT);

    inputField.setLineWrap(true);
    inputField.setWrapStyleWord(true);
    inputField.setInputVerifier(linkIdInputVerifier);
    inputField.setFont(new Font("Arial", Font.PLAIN, 16));

    variableField.setInputVerifier(linkIdInputVerifier);
    variableField.setFont(new Font("Arial", Font.PLAIN, 16));

    var gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 50, 10, 50);

    gbc.gridy = 2;
    inputPanel.add(variableLabel, gbc);

    gbc.gridy = 3;
    inputPanel.add(variableField, gbc);

    gbc.gridy = 6;
    inputPanel.add(patrimoinesLabel, gbc);

    gbc.gridy = 7;
    JScrollPane scrollPane = new JScrollPane(inputField);
    inputPanel.add(scrollPane, gbc);
  }


  private void loadDataInBackground() {
    JDialog loadingDialog = new JDialog(inputFrame, "Processing", true);
    JLabel loadingLabel = new JLabel("Processing, please wait...");
    loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
    loadingDialog.getContentPane().add(loadingLabel, CENTER);
    loadingDialog.setSize(300, 100);
    loadingDialog.setLocationRelativeTo(inputFrame);

    SwingWorker<ExtractedData<NamedString>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected ExtractedData<NamedString> doInBackground() {
            return extractInputData();
          }

          @Override
          protected void done() {
            loadingDialog.dispose();
            try {
              final ExtractedData<NamedString> inputData = get();
              openResultFrame(inputData, googleApi, authDetails);
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }
        };

    worker.execute();
    loadingDialog.setVisible(true);
  }

  private ExtractedData<NamedString> extractInputData() {
    List<NamedString> linkDataList = new ArrayList<>();

    String variableText = variableField.getText();
    String rawText = inputField.getText();
    String[] lines = rawText.split("\n");

    for (String line : lines) {
      String[] parts = line.split(":", 2);

      if (parts.length == 2) {
        String linkName = parts[0].trim();
        String linkValue = parts[1].trim();

        NamedString linkData = new NamedString(linkName, linkValue);
        linkDataList.add(linkData);
      }
    }

    return new ExtractedData<>(variableText, linkDataList);
  }

  private void openResultFrame(
      ExtractedData<NamedString> docsLink, GoogleApi googleApi, GoogleAuthenticationDetails authReqRes) {
    invokeLater(() -> new GoogleDocsLinkVerfierScreen(googleApi, authReqRes, docsLink));
    inputFrame.dispose();
  }
}
