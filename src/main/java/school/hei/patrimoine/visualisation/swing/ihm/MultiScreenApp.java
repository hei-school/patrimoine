package school.hei.patrimoine.visualisation.swing.ihm;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.Font.BOLD;
import static java.awt.Toolkit.getDefaultToolkit;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.compiler.PatrimoineCompiler;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.utils.GoogleApi;
import school.hei.patrimoine.visualisation.utils.GoogleApi.GoogleAuthenticationDetails;
import school.hei.patrimoine.visualisation.utils.GoogleDocsLinkIdInputVerifier;
import school.hei.patrimoine.visualisation.utils.GoogleDocsLinkIdParser;

@Slf4j
public class MultiScreenApp {
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

  public MultiScreenApp(GoogleApi googleApi, GoogleAuthenticationDetails authDetails) {
    this.googleApi = googleApi;
    inputFrame = newInputFrame();
    inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, Y_AXIS));

    inputFields = new ArrayList<>();
    addButtons();
    addInitialInput();

    configureInputFrame();
    this.authDetails = authDetails;
  }

  private void configureInputFrame() {
    inputFrame.getContentPane().add(inputPanel, NORTH);
    inputFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    inputFrame.pack();
  }

  private JFrame newInputFrame() {
    final JFrame inputFrame = new JFrame("Input Screen");
    inputFrame.setSize(getDefaultToolkit().getScreenSize());
    inputFrame.setResizable(true);
    inputFrame.setVisible(true);
    return inputFrame;
  }

  private void addButtons() {
    JButton addButton = newAddButton();
    JButton removeButton = newRemoveButton();
    JButton submitButton = newSubmitButton();

    JLabel buttonTitle = new JLabel("Input Controls:");
    buttonTitle.setFont(new Font("Arial", BOLD, 14));
    buttonTitle.setHorizontalAlignment(SwingConstants.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(addButton);
    buttonPanel.add(removeButton);
    buttonPanel.add(submitButton);

    inputPanel.add(buttonTitle);
    inputPanel.add(buttonPanel);
  }

  private JButton newSubmitButton() {
    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(e -> loadDataInBackground());
    return submitButton;
  }

  private JButton newRemoveButton() {
    JButton removeButton = new JButton("Remove Input");
    removeButton.addActionListener(
        e -> {
          if (inputFields.size() > MIN_INPUTS) {
            removeInputField();
          } else {
            showMessageDialog(inputFrame, "At least 1 input is required.");
          }
        });
    return removeButton;
  }

  private JButton newAddButton() {
    JButton addButton = new JButton("Add Input");
    addButton.addActionListener(
        e -> {
          if (inputFields.size() < MAX_INPUTS) {
            addInputField();
          } else {
            showMessageDialog(inputFrame, "Maximum of 9 inputs allowed.");
          }
        });
    return addButton;
  }

  private void addInitialInput() {
    JTextField initialField = newGoogleDocsLinkTextField();
    inputFields.add(initialField);
    inputPanel.add(initialField);
  }

  private JTextField newGoogleDocsLinkTextField() {
    var textField = new JTextField(20);
    textField.setInputVerifier(linkIdInputVerifier);
    return textField;
  }

  private void addInputField() {
    JTextField newField = newGoogleDocsLinkTextField();
    inputFields.add(newField);
    inputPanel.add(newField);
    inputFrame.pack();
  }

  private void removeInputField() {
    JTextField fieldToRemove = inputFields.getLast();
    inputPanel.remove(fieldToRemove);
    inputFields.remove(fieldToRemove);
    inputFrame.pack();
  }

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
          protected List<Patrimoine> doInBackground() throws Exception {
            var ids = extractInputIds();
            List<String> codePatrimoinesVisualisables = new ArrayList<>();
            for (var id : ids) {
              var code = googleApi.readDocsContent(authDetails, id);
              codePatrimoinesVisualisables.add(code);
            }
            List<Patrimoine> patrimoinesVisualisables = new ArrayList<>();
            Pattern pattern = Pattern.compile("public class (\\w+)");
            for (String codePatrimoine : codePatrimoinesVisualisables) {
              Matcher matcher = pattern.matcher(codePatrimoine);

              if (matcher.find()) {
                String className = matcher.group(1);

                Patrimoine patrimoineVisualisable =
                    PatrimoineCompiler.stringCompiler(className, codePatrimoine);
                patrimoinesVisualisables.add(patrimoineVisualisable);
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
