package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.recouppement.model.RecoupementStatus.IMPREVU;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.app.ViewFactory.make;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.getSelectedFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.clearAllTempContents;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.stage;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getDoneCasSetFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getPJ;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.model.Info;
import school.hei.patrimoine.modele.recouppement.model.PossessionRecoupee;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.SectionOperationsContext;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.SectionPiecesJustificativesContext;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileQuerier;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileQuerier.QueryResult;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileWriter;
import school.hei.patrimoine.patrilang.files.io.PatriLangFileWriter.FileWriterInput;
import school.hei.patrimoine.patrilang.generator.PatriLangGeneratorFactory;
import school.hei.patrimoine.patrilang.generator.possession.CommentPatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.possession.PieceJustificativePatriLangGenerator;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.MultiViews;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.PossessionGeneratorFactory;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession.PieceJustificativeGenerator;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.DateFormatter;

public class PossessionRecoupeeRealisationsDialog extends Dialog {
  private final State state;
  private final Set<PendingData> pendingData;
  private final PossessionRecoupee<Possession> possessionRecoupee;

  private DefaultListModel<Info<Possession>> realisesModel;
  private final JPanel contentPanel;
  private MultiViews pageManager;

  private final boolean isExecuteMode;

  public PossessionRecoupeeRealisationsDialog(
      State state, PossessionRecoupee<Possession> possessionRecoupee, boolean isExecuteMode) {
    super("Exécutions d'opération", 700, 800, false);

    this.state = state;
    this.isExecuteMode = isExecuteMode;
    this.pendingData = new HashSet<>();
    this.possessionRecoupee = possessionRecoupee;

    setLayout(new BorderLayout());
    setBackground(Color.WHITE);

    addTitle();

    contentPanel = new JPanel(new BorderLayout());
    add(contentPanel, BorderLayout.CENTER);

    initPageManager();

    if (isExecuteMode) {
      pageManager.navigate("add-form-view");
    }

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

    var realisesList = getInfoJList();

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

  private @NonNull JList<Info<Possession>> getInfoJList() {
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
    return realisesList;
  }

  private JPanel buildAddFormView() {
    var form =
        new AddRecoupementExecutionForm(
            possessionRecoupee.possession().nom(),
            possessionRecoupee.possession().devise(),
            possessionRecoupee.info().valeur(),
            possessionRecoupee.realises().isEmpty());

    var panel = new JPanel(new BorderLayout());
    panel.add(form, BorderLayout.CENTER);

    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    if (isExecuteMode) {
      buttonPanel.add(
          new Button(
              "Valider",
              e -> {
                try {
                  var newInfo = buildInfoFromForm(form);
                  register(newInfo);
                  saveExecutions();
                } catch (IllegalArgumentException ex) {
                  showError("Erreur", ex.getMessage());
                }
              }));
      buttonPanel.add(new Button("Annuler", e -> dispose()));
    } else {
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
    }

    panel.add(buttonPanel, BorderLayout.SOUTH);
    return panel;
  }

  private Map<String, Object> getPossessionArgs(AddRecoupementExecutionForm form) {
    Map<String, Object> args =
        new HashMap<>(
            Map.of(
                "nom", form.getNom(),
                "date", form.getDate(),
                "valeur", form.getValeur(),
                "prévu", possessionRecoupee.prevu().possession()));
    switch (possessionRecoupee.possession()) {
      case FluxArgent ignored ->
          args.put("compte", ((FluxArgent) possessionRecoupee.possession()).getCompte());
      case TransfertArgent ignored -> {
        var transfert = (TransfertArgent) possessionRecoupee.possession();
        args.put("depuisCompte", transfert.getDepuisCompte());
        args.put("versCompte", transfert.getVersCompte());
      }
      default -> throw new IllegalArgumentException("Type non supporté");
    }
    return args;
  }

  private Map<String, Object> getPjArgs(Possession possession, AddRecoupementExecutionForm form) {
    var prevu = possessionRecoupee.prevu();
    return Map.of(
        "id", prevu.isEmpty() ? possession.nom() : prevu.nom(),
        "date", form.getDate(),
        "link", form.getLinkPJ(),
        "ref", form.getReferencePJ());
  }

  private PendingData buildInfoFromForm(AddRecoupementExecutionForm form) {
    PJFieldsValidator.validatePJ(form);

    var pjGenerator = new PieceJustificativeGenerator();
    var possessionGenerator = PossessionGeneratorFactory.make(possessionRecoupee.possession());

    var comment = form.getComment();
    var newPossession = possessionGenerator.apply(getPossessionArgs(form));
    var newPj =
        PJFieldsValidator.hasPJ(form) ? pjGenerator.apply(getPjArgs(newPossession, form)) : null;

    var info =
        Info.builder()
            .t(form.getDate())
            .nom(newPossession.nom())
            .valeur(form.getValeur())
            .possession(newPossession)
            .possessionACorriger(newPossession)
            .build();

    return new PendingData(comment, info, newPj);
  }

  private void addTitle() {
    var titleString =
        String.format("Exécutions de l'opération : %s", possessionRecoupee.possession().nom());

    var title = new JLabel(titleString);
    title.setFont(new Font("Arial", Font.BOLD, 16));
    title.setBorder(new EmptyBorder(10, 10, 10, 10));
    add(title, BorderLayout.NORTH);
  }

  private void register(PendingData data) {
    if (alreadyExist(data.info())) {
      throw new IllegalArgumentException(
          String.format("L'exécution nom=%s est déjà utilisée", data.info().possession().nom()));
    }

    pendingData.add(data);
    realisesModel.addElement(data.info());
  }

  private Set<Info<Possession>> getPendingData() {
    return pendingData.stream().map(PendingData::info).collect(toSet());
  }

  private boolean alreadyExist(Info<Possession> candidate) {
    Set<Info<Possession>> exists = new HashSet<>(possessionRecoupee.realises());
    exists.addAll(getPendingData());

    return exists.stream()
        .anyMatch(info -> info.possession().nom().equals(candidate.possession().nom()));
  }

  private String getPjLines() {
    return pendingData.stream()
        .filter(data -> data.pj() != null)
        .map(
            data -> {
              var generator = new PieceJustificativePatriLangGenerator();
              return generator.apply(data.pj());
            })
        .collect(joining("\n"));
  }

  private String getCasLines() {
    var commentGenerator = new CommentPatriLangGenerator();
    return pendingData.stream()
        .map(
            data -> {
              var generator = PatriLangGeneratorFactory.make(data.info().possession());
              var line = generator.apply(data.info().possession());
              var rawComment = data.comment();
              if (!rawComment.isBlank()) {
                line += " " + commentGenerator.apply(rawComment);
              }
              return line;
            })
        .collect(joining("\n"));
  }

  static QueryResult<SectionOperationsContext> getSectionOperations(PatriLangFileContext file) {
    var querier = new PatriLangFileQuerier();
    var sectionOperation = querier.query(file, document -> document.cas().sectionOperations());
    if (sectionOperation.isEmpty()) {
      throw new RuntimeException("Section Operations introuvable dans le fichier");
    }
    return sectionOperation.get();
  }

  static QueryResult<SectionPiecesJustificativesContext> getSectionPieceJustificatives(
      PatriLangFileContext pjFile) {
    var querier = new PatriLangFileQuerier();
    var sectionOperation =
        querier.query(
            pjFile, document -> document.piecesJustificatives().sectionPiecesJustificatives());
    if (sectionOperation.isEmpty()) {
      throw new RuntimeException("Section Pièces Justificatives introuvable dans le fichier");
    }
    return sectionOperation.get();
  }

  private void saveExecutions() {
    var optionalSelectedFile = getSelectedFile(state);
    if (optionalSelectedFile.isEmpty()) {
      return;
    }

    var casSet = getDoneCasSetFile();
    var selectedFile = optionalSelectedFile.get();
    var optionalPjFile = getPJ(selectedFile);

    AsyncTask.<Void>builder()
        .task(
            () -> {
              var casLines = getCasLines();
              var writer = new PatriLangFileWriter();
              var operations = getSectionOperations(selectedFile);

              writer.insertAtLine(
                  FileWriterInput.builder()
                      .content(casLines)
                      .file(selectedFile)
                      .casSet(casSet)
                      .build(),
                  operations.endLine());

              if (optionalPjFile.isEmpty()) {
                clearAllTempContents();
                stage(selectedFile);
                return null;
              }

              var pjLines = getPjLines();
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

  record PendingData(String comment, Info<Possession> info, PieceJustificative pj) {}
}
