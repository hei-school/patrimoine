package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.getSelectedFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeRealisationsDialog.getSectionOperations;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeRealisationsDialog.getSectionPieceJustificatives;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.clearAllTempContents;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.stage;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getDoneCasSetFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getPJ;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.jetbrains.annotations.NotNull;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileWriter;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileWriter.FileWriterInput;
import school.hei.patrimoine.patrilang.generator.PatriLangGeneratorFactory;
import school.hei.patrimoine.patrilang.generator.possession.CommentPatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.possession.PieceJustificativePatriLangGenerator;
import school.hei.patrimoine.patrilang.modele.PatriLangCas;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession.ExecutionGenerator;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession.FluxArgentExecutionGenerator;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession.PieceJustificativeGenerator;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession.TransfertArgentExecutionGenerator;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher;

public class AddImprevuDialog extends Dialog {
  private final State state;

  private AddRecoupementExecutionForm form;
  private final JComboBox<OperationType> operationTypeSelect;

  private JComboBox<NamedCompte> compteSelect;
  private JComboBox<NamedCompte> depuisCompteSelect;
  private JComboBox<NamedCompte> versCompteSelect;

  public AddImprevuDialog(State state) {
    super("Ajouter un imprévu", 800, 900, false);
    this.state = state;
    this.operationTypeSelect = new JComboBox<>(OperationType.values());

    setLayout(new BorderLayout());

    addTitle();
    addForm();
    addButtons();

    setVisible(true);
  }

  private void initComptes() {
    this.compteSelect = getCompteSelect();
    this.depuisCompteSelect = getCompteSelect();
    this.versCompteSelect = getCompteSelect();
  }

  private List<Pair<String, JComponent>> buildInputs() {
    List<Pair<String, JComponent>> inputs = new ArrayList<>();

    switch (getSelectedType()) {
      case FLUX_ARGENT -> inputs.add(Pair.of("Compte :", compteSelect));
      case TRANSFERT_ARGENT -> {
        inputs.add(Pair.of("Depuis compte :", depuisCompteSelect));
        inputs.add(Pair.of("Vers compte :", versCompteSelect));
      }
    }

    return inputs;
  }

  private void addForm() {
    var patrimoine = getDonePatrimoine();

    initComptes();

    this.form =
        new AddRecoupementExecutionForm(
            "",
            patrimoine.getDevise(),
            new Argent(0, patrimoine.getDevise()),
            List.of(),
            buildInputs(),
            true);

    add(new JScrollPane(form), BorderLayout.CENTER);
    operationTypeSelect.addActionListener(e -> rebuildForm());
  }

  private void rebuildForm() {
    remove(form);
    addForm();

    revalidate();
    repaint();
  }

  private OperationType getSelectedType() {
    return (OperationType) operationTypeSelect.getSelectedItem();
  }

  private Patrimoine getDonePatrimoine() {
    PatriLangCas cas = state.get("doneCas");
    return cas.patrimoine();
  }

  private void addTitle() {
    var title = new JLabel("Ajout d'imprévu");
    title.setFont(new Font("Arial", Font.BOLD, 16));
    title.setBorder(new EmptyBorder(10, 10, 10, 10));
    title.setBackground(Color.WHITE);
    add(title, BorderLayout.NORTH);
  }

  private void addButtons() {
    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setOpaque(true);
    buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    buttonPanel.add(
        new Button(
            "Ajouter",
            e -> {
              try {
                addExecution();
                dispose();
              } catch (IllegalArgumentException error) {
                showError("Erreur", error.getMessage());
              }
            }));

    buttonPanel.add(new Button("Annuler", e -> dispose()));
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private JComboBox<NamedCompte> getCompteSelect() {
    PatriLangCas plannedCas = state.get("plannedCas");

    var comptes = new ArrayList<>(getDoneCasSetComptes());
    var casComptes =
        plannedCas.patrimoine().getPossessions().stream()
            .filter(p -> p instanceof Compte)
            .map(compte -> (Compte) compte)
            .toList();

    comptes.addAll(casComptes);

    var array = new HashSet<>(comptes).stream().map(NamedCompte::new).toArray(NamedCompte[]::new);
    return new JComboBox<>(array);
  }

  private ExecutionGenerator<? extends Possession> getExecutionGenerator() {
    return switch (getSelectedType()) {
      case FLUX_ARGENT -> new FluxArgentExecutionGenerator();
      case TRANSFERT_ARGENT -> new TransfertArgentExecutionGenerator();
    };
  }

  private String getCasLine(Possession possession) {
    var lineGenerator = PatriLangGeneratorFactory.make(possession);
    var line = lineGenerator.apply(possession);
    var rawComment = form.getComment();

    if (!rawComment.isBlank()) {
      var commentGenerator = new CommentPatriLangGenerator();
      line += " " + commentGenerator.apply(rawComment);
    }

    return line;
  }

  private String getPjLine(Possession possession) {
    if (!PJFieldsValidator.hasPJ(form)) {
      return "";
    }

    var pj = new PieceJustificativeGenerator().apply(getPjArgs(possession));
    var generator = new PieceJustificativePatriLangGenerator();
    return pj == null ? "" : generator.apply(pj);
  }

  private void addExecution() {
    PJFieldsValidator.validatePJ(form);

    var selectedFile = getSelectedFile(state).orElseThrow();
    var generator = getExecutionGenerator();
    var newPossession = generator.apply(getPossessionArgs());
    var casLine = getCasLine(newPossession);
    var casSet = getDoneCasSetFile();
    var optionalPjFile = getPJ(selectedFile);

    AsyncTask.<Void>builder()
        .task(
            () -> {
              var writer = new PatriLangFileWriter();
              var operations = getSectionOperations(selectedFile);

              writer.insertAtLine(
                  FileWriterInput.builder()
                      .content(casLine)
                      .file(selectedFile)
                      .casSet(casSet)
                      .build(),
                  operations.endLine());

              if (optionalPjFile.isEmpty()) {
                clearAllTempContents();
                stage(selectedFile);
                return null;
              }

              var pjLines = getPjLine(newPossession);
              if (pjLines.isBlank()) {
                clearAllTempContents();
                stage(selectedFile);
                return null;
              }

              var pjFile = optionalPjFile.get();
              var pjs = getSectionPieceJustificatives(pjFile);
              writer.insertAtLine(
                  FileWriterInput.builder().content(pjLines).file(pjFile).casSet(casSet).build(),
                  pjs.endLine());

              clearAllTempContents();
              stage(pjFile);
              stage(selectedFile);
              return null;
            })
        .onError(MessageDialog::showError)
        .onSuccess(
            result -> {
              showInfo("Succès", "L'opération a été exécutée avec succès");
              PatriLangFilesWatcher.dispatch();
              dispose();
            })
        .build()
        .execute();
  }

  private Map<String, Object> getPjArgs(Possession possession) {
    return Map.of(
        "id", possession.nom(),
        "date", form.getDate(),
        "link", form.getLinkPJ(),
        "ref", form.getReferencePJ());
  }

  private Map<String, Object> getPossessionArgs() {
    Map<String, Object> args =
        new HashMap<>(
            Map.of(
                "nom", form.getNom(),
                "date", form.getDate(),
                "valeur", form.getValeur()));

    switch (getSelectedType()) {
      case FLUX_ARGENT -> args.put("compte", getCompteSelected(compteSelect));
      case TRANSFERT_ARGENT -> {
        args.put("depuisCompte", getCompteSelected(depuisCompteSelect));
        args.put("versCompte", getCompteSelected(versCompteSelect));
      }
      default -> throw new IllegalArgumentException("Type non supporté");
    }
    return args;
  }

  private Compte getCompteSelected(JComboBox<NamedCompte> select) {
    var item = (NamedCompte) select.getSelectedItem();
    if (item == null) {
      throw new IllegalArgumentException(
          "Aucun compte disponible. Veuillez créer un compte dans le patrimoine.");
    }

    return item.compte();
  }

  private record NamedCompte(Compte compte) {
    @Override
    public @NotNull String toString() {
      return compte.nom();
    }
  }

  public enum OperationType {
    FLUX_ARGENT,
    TRANSFERT_ARGENT
  }
}
