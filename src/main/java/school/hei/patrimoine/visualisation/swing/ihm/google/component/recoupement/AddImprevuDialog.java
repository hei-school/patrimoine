package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.getSelectedFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeRealisationsDialog.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.clearAllTempContents;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.stage;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getDoneCasSetFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getSupportingInfoFile;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.OperationComment;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileWriter;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileWriter.FileWriterInput;
import school.hei.patrimoine.patrilang.generator.PatriLangGeneratorFactory;
import school.hei.patrimoine.patrilang.generator.possession.CommentPatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.possession.OperationCommentGenerator;
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

  private JScrollPane scrollPane;
  private AddRecoupementExecutionForm form;
  private final JComboBox<OperationType> operationTypeSelect;

  private JComboBox<NamedCompte> compteSelect;
  private JComboBox<NamedCompte> depuisCompteSelect;
  private JComboBox<NamedCompte> versCompteSelect;

  public AddImprevuDialog(State state) {
    super("Ajouter un imprévu", 800, 900, false);
    this.state = state;
    this.operationTypeSelect = new JComboBox<>(OperationType.values());
    operationTypeSelect.addActionListener(e -> rebuildForm());

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
        inputs.add(Pair.of("Compte à débiter :", depuisCompteSelect));
        inputs.add(Pair.of("Compte à créditer :", versCompteSelect));
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
            List.of(Pair.of("Type d'opération financière : ", operationTypeSelect)),
            buildInputs(),
            true);

    this.scrollPane = new JScrollPane(form);

    scrollPane.setBorder(null);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    add(scrollPane, BorderLayout.CENTER);
  }

  private void rebuildForm() {
    remove(scrollPane);
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
    if (!AddExecutionFieldsValidator.hasPJ(form)) {
      return "";
    }

    var pj = new PieceJustificativeGenerator().apply(getPjArgs(possession));
    var generator = new PieceJustificativePatriLangGenerator();
    return pj == null ? "" : generator.apply(pj);
  }

  private String getCommentLine(Possession possession) {
    var rawComment = form.getComment();
    if (rawComment.isBlank()) {
      return "";
    }
    var comment = new OperationComment(possession.nom(), form.getDate(), rawComment);
    return new OperationCommentGenerator().apply(comment);
  }

  private void addExecution() {
    AddExecutionFieldsValidator.validatePJ(form);

    var selectedFile = getSelectedFile(state).orElseThrow();
    var generator = getExecutionGenerator();
    var newPossession = generator.apply(getPossessionArgs());
    var casSet = getDoneCasSetFile();
    var optionalPjFile = getSupportingInfoFile(selectedFile);

    AsyncTask.<Void>builder()
        .task(
            () -> {
              var writer = new PatriLangFileWriter();
              var operations = getSectionOperations(selectedFile);

              writer.insertAtLine(
                  FileWriterInput.builder()
                      .content(getCasLine(newPossession))
                      .file(selectedFile)
                      .casSet(casSet)
                      .build(),
                  operations.endLine());

              if (optionalPjFile.isEmpty()) {
                clearAllTempContents();
                stage(selectedFile);
                return null;
              }

              var pjFile = optionalPjFile.get();

              var pjLine = getPjLine(newPossession);
              if (!pjLine.isBlank()) {
                var pjs = getSectionPieceJustificatives(pjFile);
                writer.insertAtLine(
                    FileWriterInput.builder().content(pjLine).file(pjFile).casSet(casSet).build(),
                    pjs.endLine());
              }

              var commentLine = getCommentLine(newPossession);
              if (!commentLine.isBlank()) {
                var sectionComments = getSectionComments(pjFile);
                writer.insertAtLine(
                    FileWriterInput.builder()
                        .content(commentLine)
                        .file(pjFile)
                        .casSet(casSet)
                        .build(),
                    sectionComments.endLine());
              }

              clearAllTempContents();
              stage(selectedFile);
              stage(pjFile);
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

  @Getter
  public enum OperationType {
    FLUX_ARGENT("Flux d'argent"),
    TRANSFERT_ARGENT("Transfert d'argent");

    private final String label;

    OperationType(String label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return label;
    }
  }
}
