package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static java.util.stream.Collectors.joining;
import static school.hei.patrimoine.modele.recouppement.RecoupementStatus.IMPREVU;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.app.ViewFactory.make;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.*;

import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee.Info;
import school.hei.patrimoine.patrilang.files.PatriLangFileQuerier;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter.FileWritterInput;
import school.hei.patrimoine.patrilang.generator.PatriLangGeneratorFactory;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.MultiViews;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.PossessionGeneratorFactory;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.DateFormatter;

public class PossessionRecoupeeRealisationsDialog extends Dialog {
  private final State state;
  private final Set<Info> pendingInfos;
  private final PatriLangFileWritter writter;
  private final PatriLangFileQuerier querier;
  private final PossessionRecoupee possessionRecoupee;

  private DefaultListModel<Info> realisesModel;
  private final JPanel contentPanel;
  private MultiViews pageManager;

  public PossessionRecoupeeRealisationsDialog(State state, PossessionRecoupee possessionRecoupee) {
    super("Exécutions d'opération", 700, 600, false);

    this.state = state;
    this.pendingInfos = new HashSet<>();
    this.writter = new PatriLangFileWritter();
    this.querier = new PatriLangFileQuerier();
    this.possessionRecoupee = possessionRecoupee;

    setLayout(new BorderLayout());
    setBackground(Color.WHITE);

    addTitle();

    contentPanel = new JPanel(new BorderLayout());
    add(contentPanel, BorderLayout.CENTER);

    initPageManager();

    setVisible(true);
  }

  private void initPageManager() {
    var listView = make("list-view", buildListView());
    var addFormView = make("add-form-view", buildAddFormView());

    pageManager = new MultiViews("list-view", Set.of(listView, addFormView));
    contentPanel.add(pageManager, BorderLayout.CENTER);
  }

  private JPanel buildListView() {
    realisesModel = new DefaultListModel<>();
    possessionRecoupee.realises().forEach(realisesModel::addElement);

    var realisesList = new JList<>(realisesModel);
    realisesList.setCellRenderer(
        (list, value, index, isSelected, cellHasFocus) -> {
          var label =
              new JLabel(
                  String.format(
                      "Date=%s, Valeur=%s, Nom=%s",
                      DateFormatter.format(value.t()),
                      ArgentFormatter.format(value.valeur()),
                      value.possession().nom()));
          label.setBorder(new EmptyBorder(10, 5, 10, 5));
          label.setFont(new Font("Arial", Font.PLAIN, 15));
          return label;
        });

    var panel = new JPanel(new BorderLayout());
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    panel.add(new JScrollPane(realisesList), BorderLayout.CENTER);

    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    if (!IMPREVU.equals(possessionRecoupee.status())) {
      buttonPanel.add(
          new Button("Ajouter une exécution", e -> pageManager.navigate("add-form-view")));
      buttonPanel.add(new Button("Sauvegarder", e -> saveExecutions()));
    }
    buttonPanel.add(new Button("Annuler", e -> dispose()));

    panel.add(buttonPanel, BorderLayout.SOUTH);
    return panel;
  }

  private JPanel buildAddFormView() {
    var form =
        new AddRecoupementExecutionForm(
            possessionRecoupee.possession().nom(),
            possessionRecoupee.possession().devise(),
            possessionRecoupee.info().valeur());

    var panel = new JPanel(new BorderLayout());
    panel.add(form, BorderLayout.CENTER);

    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(
        new Button(
            "Valider",
            e -> {
              try {
                var newInfo = buildInfoFromForm(form);
                register(newInfo);
                pageManager.navigate("list-view");
              } catch (IllegalArgumentException ex) {
                showError("Erreur", ex.getMessage());
              }
            }));
    buttonPanel.add(new Button("Annuler", e -> pageManager.navigate("list-view")));

    panel.add(buttonPanel, BorderLayout.SOUTH);
    return panel;
  }

  private Info buildInfoFromForm(AddRecoupementExecutionForm form) {
    var generator = PossessionGeneratorFactory.make(possessionRecoupee.possession());
    Map<String, Object> args =
        switch (possessionRecoupee.possession()) {
          case FluxArgent ignored ->
              Map.of(
                  "date", form.getDate(),
                  "valeur", form.getValeur(),
                  "nom", form.getNom(),
                  "compte", ((FluxArgent) possessionRecoupee.possession()).getCompte(),
                  "prevu", possessionRecoupee.prevu().possession());
          default -> throw new IllegalArgumentException("Type non supporté");
        };
    var newPossession = generator.apply(args);
    return new Info(form.getDate(), form.getValeur(), newPossession);
  }

  private void addTitle() {
    var titleString =
        String.format("Exécutions de l'opération : %s", possessionRecoupee.possession().nom());

    var title = new JLabel(titleString);
    title.setFont(new Font("Arial", Font.BOLD, 16));
    title.setBorder(new EmptyBorder(10, 10, 10, 10));
    add(title, BorderLayout.NORTH);
  }

  private void register(Info info) {
    if (alreadyExist(info)) {
      throw new IllegalArgumentException(
          String.format("L'exécution nom=%s est déjà utilisée", info.possession().nom()));
    }

    pendingInfos.add(info);
    realisesModel.addElement(info);
  }

  private boolean alreadyExist(Info candiate) {
    Set<Info> exists = new HashSet<>(possessionRecoupee.realises());
    exists.addAll(pendingInfos);

    return exists.stream()
        .anyMatch(info -> info.possession().nom().equals(candiate.possession().nom()));
  }

  private void saveExecutions() {
    var lines =
        pendingInfos.stream()
            .map(
                info -> {
                  var lineGenerator = PatriLangGeneratorFactory.make(info.possession());
                  return lineGenerator.apply(info.possession());
                })
            .collect(joining("\n"));

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
                  FileWritterInput.builder()
                      .content(lines)
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
              AppContext.getDefault().globalState().update("isAnyFileModified", true);
              dispose();
            })
        .build()
        .execute();
  }
}
