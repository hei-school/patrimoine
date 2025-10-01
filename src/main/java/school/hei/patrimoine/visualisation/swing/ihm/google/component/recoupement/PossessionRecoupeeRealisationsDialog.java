package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static java.util.stream.Collectors.joining;
import static school.hei.patrimoine.modele.recouppement.RecoupementStatus.IMPREVU;
import static school.hei.patrimoine.visualisation.swing.ihm.google.utils.MessageDialog.*;

import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee.Info;
import school.hei.patrimoine.patrilang.files.PatriLangFileQuerier;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter.FileWritterInput;
import school.hei.patrimoine.patrilang.generator.PatriLangGeneratorFactory;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.formatter.DateFormatter;

public class PossessionRecoupeeRealisationsDialog extends Dialog {
  private final State state;
  private final Set<Info> pendingInfos;
  private final PatriLangFileWritter writter;
  private final PatriLangFileQuerier querier;
  private final PossessionRecoupee possessionRecoupee;

  private DefaultListModel<Info> realisesModel;

  public PossessionRecoupeeRealisationsDialog(State state, PossessionRecoupee possessionRecoupee) {
    super("Exécutions d'opération", 700, 500, false);

    this.state = state;
    this.pendingInfos = new HashSet<>();
    this.writter = new PatriLangFileWritter();
    this.querier = new PatriLangFileQuerier();
    this.possessionRecoupee = possessionRecoupee;

    setLayout(new BorderLayout());
    setBackground(Color.WHITE);

    addTitle();
    addRealisesPanel();
    addButtons();

    setVisible(true);
  }

  private void addTitle() {
    var titleString =
        String.format("Exécutions de l'opération : %s", possessionRecoupee.possession().nom());

    var title = new JLabel(titleString);
    title.setFont(new Font("Arial", Font.BOLD, 16));
    title.setBorder(new EmptyBorder(10, 10, 10, 10));
    add(title, BorderLayout.NORTH);
  }

  private void addRealisesPanel() {
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

    add(panel, BorderLayout.CENTER);
  }

  private void addButtons() {
    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    if (!IMPREVU.equals(possessionRecoupee.status())) {
      buttonPanel.add(
          new Button(
              "Ajouter une exécution",
              e -> new AddRecoupementExecutionDialog(possessionRecoupee, this::register)),
          BorderLayout.SOUTH);
      buttonPanel.add(new Button("Sauvegarder", e -> saveExecutions()));
    }

    buttonPanel.add(new Button("Annuler", e -> dispose()));
    add(buttonPanel, BorderLayout.SOUTH);
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
              AppContext.getDefault().globalState().update("newUpdate", true);
              dispose();
            })
        .build()
        .execute();
  }
}
