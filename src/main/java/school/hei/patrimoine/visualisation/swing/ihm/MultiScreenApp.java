package school.hei.patrimoine.visualisation.swing.ihm;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.Font.BOLD;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import school.hei.patrimoine.cas.PatrimoineCresusCas;
import school.hei.patrimoine.cas.PatrimoineEtudiantPireCas;
import school.hei.patrimoine.cas.PatrimoineRicheCas;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.utils.GoogleDocsLinkIdInputVerifier;
import school.hei.patrimoine.visualisation.utils.GoogleDocsLinkIdParser;

public class MultiScreenApp {
  private final JFrame inputFrame; // Original frame for input
  private final JPanel inputPanel;
  private final List<JTextField> inputFields;
  private static final int MAX_INPUTS = 9;
  private static final int MIN_INPUTS = 1;
  private final GoogleDocsLinkIdInputVerifier linkIdInputVerifier =
      new GoogleDocsLinkIdInputVerifier();
  private final GoogleDocsLinkIdParser linkIdParser = new GoogleDocsLinkIdParser();

  public MultiScreenApp() {
    inputFrame = new JFrame("Input Screen");
    inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, Y_AXIS));

    inputFields = new ArrayList<>();
    addButtons();
    addInitialInput();

    inputFrame.getContentPane().add(inputPanel, NORTH);
    inputFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    inputFrame.pack();
    inputFrame.setVisible(true);
  }

  private void addButtons() {
    JButton addButton = getAddButton();
    JButton removeButton = getRemoveButton();
    JButton submitButton = getSubmitButton();

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

  private JButton getSubmitButton() {
    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(e -> showLoadingScreen());
    return submitButton;
  }

  private JButton getRemoveButton() {
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

  private JButton getAddButton() {
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

  private void showLoadingScreen() {
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
            List<Patrimoine> patrimoinesVisualisables = new ArrayList<>();
            for (var id : ids) {
              if (id.equals("1")) {
                patrimoinesVisualisables.add(new PatrimoineEtudiantPireCas().get());
              }
              if (id.equals("2")) {
                patrimoinesVisualisables.add(new PatrimoineRicheCas().get());
              }
              if (id.equals("3")) {
                patrimoinesVisualisables.add(new PatrimoineCresusCas().get());
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

  public static void main(String[] args) {
    invokeLater(MultiScreenApp::new);
  }
}
