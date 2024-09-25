package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.compiler.ClassNameExtractor;
import school.hei.patrimoine.compiler.PatrimoineCompiler;
import school.hei.patrimoine.google.GoogleApi;
import school.hei.patrimoine.google.GoogleApi.GoogleAuthenticationDetails;
import school.hei.patrimoine.google.GoogleDocsLinkIdParser;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.swing.ihm.MainIHM;

@Slf4j
public class GoogleDocsSubmitScreen {
  private final JFrame inputFrame; // Original frame for input
  private final JPanel inputPanel;
  private final List<JTextField> inputFields;
  private static final int MAX_INPUTS = 9;
  private static final int MIN_INPUTS = 1;
  private final GoogleDocsLinkIdInputVerifier linkIdInputVerifier =
      new GoogleDocsLinkIdInputVerifier();
  private final GoogleDocsLinkIdParser linkIdParser = new GoogleDocsLinkIdParser();
  private final GoogleApi googleApi;
  private final GoogleAuthenticationDetails authDetails;

  public GoogleDocsSubmitScreen(GoogleApi googleApi, GoogleAuthenticationDetails authDetails) {
    this.googleApi = googleApi;
    inputFrame = newInputFrame();
    inputPanel = new JPanel();
    inputPanel.setLayout(new GridBagLayout());

    inputFields = new ArrayList<>();
    addButtons();
    addInitialInput();

    configureInputFrame();
    this.authDetails = authDetails;
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

    JLabel buttonTitle = new JLabel("Submit Your Google Docs Links:"); // Texte personnalisé
    buttonTitle.setFont(new Font("Arial", Font.BOLD, 24)); // Police plus grande et en gras
    buttonTitle.setHorizontalAlignment(SwingConstants.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(submitButton);
    buttonPanel.setOpaque(false); // Rendre le panneau transparent pour un look plus élégant

    // Utilisation de GridBagConstraints pour centrer les composants
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(10, 0, 10, 0); // Espacement autour du texte
    gbc.anchor = GridBagConstraints.CENTER; // Centrer le texte
    inputPanel.add(buttonTitle, gbc);

    gbc.gridy = 1; // Ajouter le panneau de boutons en dessous
    inputPanel.add(buttonPanel, gbc);
  }

  private JButton newSubmitButton() {
    JButton submitButton = new JButton("Submit");
    submitButton.setPreferredSize(new Dimension(200, 50)); // Bouton plus grand
    submitButton.setFont(new Font("Arial", Font.BOLD, 18)); // Police plus grande
    submitButton.setFocusPainted(false);
    submitButton.addActionListener(e -> loadDataInBackground());
    return submitButton;
  }

  private void addInitialInput() {
    JTextField initialField = newGoogleDocsLinkTextField();
    inputFields.add(initialField);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2; // Positionner sous les boutons
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 50, 10, 50); // Espacement latéral
    inputPanel.add(initialField, gbc);
  }

  private JTextField newGoogleDocsLinkTextField() {
    JTextField textField = new JTextField(30); // Agrandir le champ de texte
    textField.setInputVerifier(linkIdInputVerifier);
    textField.setFont(new Font("Arial", Font.PLAIN, 16)); // Police de texte plus grande
    return textField;
  }

  /*private void addInputField() {
    JTextField newField = newGoogleDocsLinkTextField();
    inputFields.add(newField);
    inputPanel.add(newField);
    inputFrame.pack();
  }*/

  /*private void removeInputField() {
    JTextField fieldToRemove = inputFields.getLast();
    inputPanel.remove(fieldToRemove);
    inputFields.remove(fieldToRemove);
    inputFrame.pack();
  }*/

  private void loadDataInBackground() {
    JDialog loadingDialog = new JDialog(inputFrame, "Processing", true);
    JLabel loadingLabel = new JLabel("Processing, please wait...");
    loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
    loadingDialog.getContentPane().add(loadingLabel, CENTER);
    loadingDialog.setSize(300, 100);
    loadingDialog.setLocationRelativeTo(inputFrame);

    SwingWorker<List<Patrimoine>, Void> worker =
        new SwingWorker<>() {
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

              Patrimoine patrimoineVisualisable =
                  patrimoineCompiler.apply(className, codePatrimoine);

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

    // Show the loading dialog and start the worker
    worker.execute();
    loadingDialog.setVisible(true); // This will block until loadingDialog is disposed
  }

  private List<String> extractInputIds() {
    List<String> ids = new ArrayList<>();
    for (JTextField field : inputFields) {
      String rawLink = field.getText();
      var parsedId = linkIdParser.apply(rawLink);
      ids.add(parsedId);
    }
    return ids;
  }

  private void openResultFrame(List<Patrimoine> patrimoinesVisualisables) {
    invokeLater(() -> new MainIHM(patrimoinesVisualisables));
  }
}
