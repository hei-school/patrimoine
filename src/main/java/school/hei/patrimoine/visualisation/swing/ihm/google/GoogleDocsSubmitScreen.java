package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.GoogleApi;

@Slf4j
public class GoogleDocsSubmitScreen {
  private final JFrame inputFrame;
  private final JPanel inputPanel;
  private final List<JTextArea> inputFields;
  private static final int MAX_INPUTS = 9;
  private static final int MIN_INPUTS = 1;
  private final GoogleDocsLinkIdInputVerifier linkIdInputVerifier =
      new GoogleDocsLinkIdInputVerifier();
  private final GoogleApi googleApi;

  public GoogleDocsSubmitScreen(GoogleApi googleApi) {
    this.googleApi = googleApi;
    inputFrame = newInputFrame();
    inputPanel = new JPanel();
    inputPanel.setLayout(new GridBagLayout());

    inputFields = new ArrayList<>();
    addButtons();
    addInitialInput();

    configureInputFrame();
  }

  private void configureInputFrame() {
    inputFrame.getContentPane().add(inputPanel);
    inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    inputFrame.pack();
    inputFrame.setLocationRelativeTo(null);
  }

  private JFrame newInputFrame() {
    JFrame inputFrame = new JFrame("Google Docs Submission");
    inputFrame.setSize(1200, 1000);
    inputFrame.setResizable(true);
    inputFrame.setVisible(true);
    return inputFrame;
  }

  private void addButtons() {
    JButton submitButton = newSubmitButton();

    JLabel buttonTitle = new JLabel("Enter Your Google Docs Links:");
    buttonTitle.setFont(new Font("Arial", Font.BOLD, 24));
    buttonTitle.setHorizontalAlignment(SwingConstants.CENTER);

    JPanel buttonPanel = new JPanel();
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
    JButton submitButton = new JButton("Verify");
    submitButton.setPreferredSize(new Dimension(200, 50));
    submitButton.setFont(new Font("Arial", Font.BOLD, 18));
    submitButton.setFocusPainted(false);
    submitButton.addActionListener(e -> loadDataInBackground());
    return submitButton;
  }

  private void addInitialInput() {
    JTextArea initialField = newGoogleDocsLinkTextArea();
    inputFields.add(initialField);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 50, 10, 50);

    JScrollPane scrollPane = new JScrollPane(initialField);
    inputPanel.add(scrollPane, gbc);
  }

  private JTextArea newGoogleDocsLinkTextArea() {
    JTextArea textArea = new JTextArea(3, 70);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setInputVerifier(linkIdInputVerifier);
    textArea.setFont(new Font("Arial", Font.PLAIN, 16));
    return textArea;
  }

  private void loadDataInBackground() {
    JDialog loadingDialog = new JDialog(inputFrame, "Processing", true);
    JLabel loadingLabel = new JLabel("Processing, please wait...");
    loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
    loadingDialog.getContentPane().add(loadingLabel, CENTER);
    loadingDialog.setSize(300, 100);
    loadingDialog.setLocationRelativeTo(inputFrame);

    SwingWorker<Object, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Map<String, String>> doInBackground() {
            return extractInputData();
          }

          @Override
          protected void done() {
            loadingDialog.dispose();
            try {
              final List<Map<String, String>> docsLink = (List<Map<String, String>>) get();
              openResultFrame(docsLink);
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }
        };

    worker.execute();
    loadingDialog.setVisible(true);
  }

  private List<Map<String, String>> extractInputData() {
    List<Map<String, String>> linkDataList = new ArrayList<>();

      String rawText = inputFields.getFirst().getText();
      String[] lines = rawText.split("\n");

      for (String line : lines) {
        String[] parts = line.split(":", 2);

        if (parts.length == 2) {
          String linkName = parts[0].trim();
          String linkValue = parts[1].trim();

          Map<String, String> linkData = new HashMap<>();
          linkData.put("name", linkName);
          linkData.put("link", linkValue);

          linkDataList.add(linkData);
        }
      }

    return linkDataList;
  }

  @SneakyThrows
  private GoogleApi.GoogleAuthenticationDetails handleGoogleSignIn() {
    return googleApi.requestAuthentication();
  }

  private void openResultFrame(List<Map<String, String>> docsLink) {
    var authReqRes = handleGoogleSignIn();
    invokeLater(() -> new GoogleDocsLinkVerfierScreen(googleApi, authReqRes, docsLink));
    inputFrame.setVisible(false);
  }
}
