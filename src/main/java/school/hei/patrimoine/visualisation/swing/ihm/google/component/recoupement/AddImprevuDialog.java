package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.visualisation.swing.ihm.google.utils.MessageDialog.*;

import java.awt.*;
import java.io.File;
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
import school.hei.patrimoine.patrilang.files.PatriLangFileQuerier;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter;
import school.hei.patrimoine.patrilang.generator.PatriLangGeneratorFactory;
import school.hei.patrimoine.patrilang.modele.PatriLangCas;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession.FluxArgentExecutionGenerator;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.AsyncTask;

public class AddImprevuDialog extends Dialog {
  private final State state;
  private final PatriLangFileQuerier querier;
  private final PatriLangFileWritter writter;

  private AddRecoupementExecutionForm form;
  private final JComboBox<NamedCompte> compteSelect;

  public AddImprevuDialog(State state) {
    super("Ajouter un imprévu", 800, 700, false);
    this.state = state;
    this.querier = new PatriLangFileQuerier();
    this.writter = new PatriLangFileWritter();
    this.compteSelect = compteSelect();

    setLayout(new BorderLayout());

    addTitle();
    addForm();
    addButtons();

    setVisible(true);
  }

  private void addForm() {
    var patrimoine = getPlannedPatrimoine();
    this.form =
        new AddRecoupementExecutionForm(
            "",
            patrimoine.getDevise(),
            new Argent(0, patrimoine.getDevise()),
            Set.of(Pair.of("Compte : ", compteSelect)));

    add(form, BorderLayout.CENTER);
  }

  private Patrimoine getPlannedPatrimoine() {
    PatriLangCas cas = state.get("plannedCas");
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

  private JComboBox<NamedCompte> compteSelect() {
    PatriLangCas plannedCas = state.get("plannedCas");

    var comptes =
        plannedCas.patrimoine().getPossessions().stream()
            .filter(p -> p instanceof Compte)
            .map(compte -> (Compte) compte)
            .map(NamedCompte::new)
            .toArray(NamedCompte[]::new);

    return new JComboBox<>(comptes);
  }

  private void addExecution() {
    var generator = new FluxArgentExecutionGenerator();
    var newPossession = generator.apply(getFluxArgentArgs());
    saveExecutions(newPossession);
  }

  private void saveExecutions(FluxArgent fluxArgent) {
    var lineGenerator = PatriLangGeneratorFactory.make(fluxArgent);
    var line = lineGenerator.apply(fluxArgent);
    File casSet = FileSideBar.getDoneCasSetFile();
    File selectedFile = state.get("selectedFile");

    AsyncTask.<Void>builder()
        .task(
            () -> {
              var sectionOperation =
                  querier.query(
                      selectedFile.getAbsolutePath(),
                      document -> document.cas().sectionOperations());
              if (sectionOperation.isEmpty()) {
                throw new RuntimeException("Section Operations introuvable dans le fichier");
              }

              writter.insertAtLine(
                  PatriLangFileWritter.FileWritterInput.builder()
                      .content(line)
                      .file(selectedFile)
                      .casSet(casSet)
                      .build(),
                  sectionOperation.get().endLine());
              return null;
            })
        .onError(
            error -> {
              if (showExceptionMessageIfRecognizedException(error)) {
                return;
              }

              showError("Erreur", "Une erreur est survenue lors de l'enregistrement");
            })
        .onSuccess(
            result -> {
              showInfo("Succès", "L'opération a été exécutée avec succès");
              AppContext.getDefault().globalState().update("newUpdate", true);
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
        "compte", named.compte());
  }

  private record NamedCompte(Compte compte) {
    @Override
    public @NotNull String toString() {
      return compte.nom();
    }
  }
}
