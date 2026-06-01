package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.getSelectedFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.clearAllTempContents;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher.getDoneCasSetComptes;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.stage;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getDoneCasSetFile;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.jetbrains.annotations.NotNull;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileQuerier;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileWriter;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileWriter.FileWriterInput;
import school.hei.patrimoine.patrilang.generator.PatriLangGeneratorFactory;
import school.hei.patrimoine.patrilang.modele.PatriLangCas;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession.FluxArgentExecutionGenerator;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher;

public class AddImprevuDialog extends Dialog {
  private final State state;
  private final PatriLangFileWriter writer;
  private final PatriLangFileQuerier querier;

  private AddRecoupementExecutionForm form;
  private final JComboBox<NamedCompte> compteSelect;

  public AddImprevuDialog(State state) {
    super("Ajouter un imprévu", 800, 900, false);
    this.state = state;
    this.querier = new PatriLangFileQuerier();
    this.writer = new PatriLangFileWriter();
    this.compteSelect = getCompteSelect();

    setLayout(new BorderLayout());

    addTitle();
    addForm();
    addButtons();

    setVisible(true);
  }

  private void addForm() {
    var patrimoine = getDonePatrimoine();
    this.form =
        new AddRecoupementExecutionForm(
            "",
            patrimoine.getDevise(),
            new Argent(0, patrimoine.getDevise()),
            Set.of(Pair.of("Compte : ", compteSelect)),
            true);

    add(form, BorderLayout.CENTER);
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

  private void addExecution() {
    var generator = new FluxArgentExecutionGenerator();
    var newPossession = generator.apply(getFluxArgentArgs());
    saveExecutions(newPossession);
  }

  private void saveExecutions(FluxArgent fluxArgent) {
    var lineGenerator = PatriLangGeneratorFactory.make(fluxArgent);
    var line = lineGenerator.apply(fluxArgent);

    var selectedFile = getSelectedFile(state).orElseThrow();

    AsyncTask.<Void>builder()
        .task(
            () -> {
              var sectionOperation =
                  querier.query(selectedFile, document -> document.cas().sectionOperations());
              if (sectionOperation.isEmpty()) {
                throw new RuntimeException("Section Operations introuvable dans le fichier");
              }

              var input =
                  FileWriterInput.builder()
                      .content(line)
                      .file(selectedFile)
                      .casSet(getDoneCasSetFile())
                      .build();

              writer.insertAtLine(input, sectionOperation.get().endLine());
              clearAllTempContents();
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

  private Map<String, Object> getFluxArgentArgs() {
    var named = (NamedCompte) requireNonNull(compteSelect.getSelectedItem());
    return Map.of(
        "date", form.getDate(),
        "valeur", form.getValeur(),
        "nom", form.getNom(),
        "compte", named.compte(),
        "linkPJ", form.getLinkPJ(),
        "referencePJ", form.getReferencePJ(),
        "comment", form.getComment());
  }

  private record NamedCompte(Compte compte) {
    @Override
    public @NotNull String toString() {
      return compte.nom();
    }
  }
}
