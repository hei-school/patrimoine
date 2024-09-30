package school.hei.patrimoine.visualisation.swing.ihm.google;

import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.compiler.ClassNameExtractor;
import school.hei.patrimoine.compiler.PatrimoineCompiler;
import school.hei.patrimoine.google.GoogleApi;
import school.hei.patrimoine.google.GoogleApi.GoogleAuthenticationDetails;
import school.hei.patrimoine.google.GoogleDocsLinkIdParser;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.swing.ihm.MainIHM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.SwingUtilities.invokeLater;

@Slf4j
public class GoogleDocsLinkVerfierScreen {
  private final JFrame inputFrame;
  private final JPanel inputPanel;
  private final List<JTextField> inputFields;
  private final GoogleDocsLinkIdInputVerifier linkIdInputVerifier = new GoogleDocsLinkIdInputVerifier();
  private final GoogleDocsLinkIdParser linkIdParser = new GoogleDocsLinkIdParser();
  private final GoogleApi googleApi;
  private final GoogleAuthenticationDetails authDetails;

  public GoogleDocsLinkVerfierScreen(GoogleApi googleApi, GoogleAuthenticationDetails authDetails, List<Map<String, String>> linksData) {
    this.googleApi = googleApi;
    this.authDetails = authDetails;
    inputFrame = newInputFrame();
    inputPanel = new JPanel(new GridBagLayout());
    inputFields = new ArrayList<>();

    addButtons();
    addInputFieldsFromData(linksData);
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
    JButton returnButton = newReturnButton();

    JLabel buttonTitle = new JLabel("Submit Your Google Docs Links:");
    buttonTitle.setFont(new Font("Arial", Font.BOLD, 24));
    buttonTitle.setHorizontalAlignment(SwingConstants.CENTER);

    JPanel buttonPanel = new JPanel();
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
    JButton submitButton = new JButton("Submit");
    submitButton.setPreferredSize(new Dimension(200, 50));
    submitButton.setFont(new Font("Arial", Font.BOLD, 18));
    submitButton.setFocusPainted(false);
    submitButton.addActionListener(e -> loadDataInBackground());
    return submitButton;
  }

  private JButton newReturnButton() {
    JButton returnButton = new JButton("Return");
    returnButton.setPreferredSize(new Dimension(200, 50));
    returnButton.setFont(new Font("Arial", Font.BOLD, 18));
    returnButton.setFocusPainted(false);
    returnButton.addActionListener(returnToPreviousScreen());
    return returnButton;
  }

  private void addInputFieldsFromData(List<Map<String, String>> linksData) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 50, 10, 50);

    int yPosition = 2;
    for (Map<String, String> linkData : linksData) {
      JLabel nameLabel = new JLabel(linkData.get("name"));
      nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
      nameLabel.setHorizontalAlignment(SwingConstants.LEFT);

      gbc.gridy = yPosition++;
      inputPanel.add(nameLabel, gbc);

      JTextField newField = newGoogleDocsLinkTextField(linkData.get("link"));
      inputFields.add(newField);

      JScrollPane scrollPane = new JScrollPane(newField);
      gbc.gridy = yPosition++;
      inputPanel.add(scrollPane, gbc);
    }
  }

  private JTextField newGoogleDocsLinkTextField(String initialValue) {
    var textField = new JTextField(70);
    textField.setInputVerifier(linkIdInputVerifier);
    textField.setFont(new Font("Arial", Font.PLAIN, 16));
    textField.setText(initialValue);
    linkIdInputVerifier.verify(textField);
    return textField;
  }

  private ActionListener returnToPreviousScreen() {
    return e -> {
      invokeLater(() -> new GoogleDocsSubmitScreen(googleApi));
      inputFrame.setVisible(false);
    };
  }

  private void loadDataInBackground() {
    JDialog loadingDialog = new JDialog(inputFrame, "Processing", true);
    JLabel loadingLabel = new JLabel("Processing, please wait...");
    loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
    loadingDialog.getContentPane().add(loadingLabel, CENTER);
    loadingDialog.setSize(300, 100);
    loadingDialog.setLocationRelativeTo(inputFrame);

    SwingWorker<List<Patrimoine>, Void> worker = new SwingWorker<>() {
      @Override
      protected List<Patrimoine> doInBackground() {
        var ids = extractInputIds();
        List<String> codePatrimoinesVisualisables = new ArrayList<>();
        for (var id : ids) {
          var code = googleApi.readDocsContent(authDetails, id);
          codePatrimoinesVisualisables.add(code);
        }
        List<Patrimoine> patrimoinesVisualisables = new ArrayList<>();
        PatrimoineCompiler patrimoineCompiler = new PatrimoineCompiler();
        for (String codePatrimoine : codePatrimoinesVisualisables) {
          String className = new ClassNameExtractor().apply(codePatrimoine);

          Patrimoine patrimoineVisualisable = patrimoineCompiler.apply(className, codePatrimoine);
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

  private List<String> extractInputIds() {
    List<String> ids = new ArrayList<>();

    for (JTextField field : inputFields) {
      String rawText = field.getText();
      var parsedId = linkIdParser.apply(rawText.trim());
      ids.add(parsedId);
    }

    return ids;
  }

  private void openResultFrame(List<Patrimoine> patrimoinesVisualisables) {
    invokeLater(() -> new MainIHM(patrimoinesVisualisables));
  }
}
