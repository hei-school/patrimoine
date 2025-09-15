package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static java.lang.Double.parseDouble;
import static school.hei.patrimoine.patrilang.files.PatriLangFileWritter.FileWritterInput;
import static school.hei.patrimoine.patrilang.mapper.DeviseMapper.stringToDevise;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionReoupeeDetailDialog.makeInfoRow;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.jdatepicker.impl.*;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.patrilang.files.PatriLangFileQuerier;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter;
import school.hei.patrimoine.patrilang.generator.PatriLangGeneratorFactory;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.MessageDialog;
import school.hei.patrimoine.visualisation.swing.ihm.selecteur.jdatepicker.DateFormatter;

public class PossessionRecoupeeExecuteDialog extends Dialog {
  private final State state;
  private final PossessionRecoupee possessionRecoupee;
  private final PatriLangFileWritter patriLangFileWritter;
  private final PatriLangFileQuerier patriLangFileQuerier;

  private JDatePickerImpl datePicker;
  private JComboBox<String> deviseCombo;
  private JTextField valeurField;

  public PossessionRecoupeeExecuteDialog(State state, PossessionRecoupee possessionRecoupee) {
    super("Exécuter la possession", 600, 500, false);
    this.state = state;
    this.possessionRecoupee = possessionRecoupee;
    this.patriLangFileWritter = new PatriLangFileWritter();
    this.patriLangFileQuerier = new PatriLangFileQuerier();

    setLayout(new BorderLayout());
    setBackground(Color.WHITE);

    addTitle();
    addForm();
    addButtons();

    setVisible(true);
  }

  private void addTitle() {
    var titleString =
        String.format("Exécution de l'opération : %s", possessionRecoupee.possession().nom());
    var title = new JLabel(titleString);
    title.setFont(new Font("Arial", Font.BOLD, 16));
    title.setBorder(new EmptyBorder(10, 10, 10, 10));

    add(title, BorderLayout.NORTH);
  }

  private void addForm() {
    var panel = new JPanel(new GridLayout(0, 1, 10, 10));
    panel.setOpaque(true);
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    panel.setBackground(Color.WHITE);

    panel.add(makeInfoRow("Date d'exécution", null));
    datePicker = createDatePicker(LocalDate.now());
    datePicker.setOpaque(true);
    datePicker.setFont(datePicker.getFont().deriveFont(18f));
    datePicker.setBackground(Color.WHITE);
    panel.add(datePicker);

    panel.add(makeInfoRow("Valeur réalisée", null));
    valeurField = new JTextField(possessionRecoupee.valeurPrevu().ppMontant());
    valeurField.setFont(new Font("Arial", Font.PLAIN, 18));
    panel.add(valeurField);

    panel.add(makeInfoRow("Devise", null));
    deviseCombo = new JComboBox<>(new String[] {"Ar", "€", "$"});
    panel.add(deviseCombo);

    add(panel, BorderLayout.CENTER);
  }

  private static JDatePickerImpl createDatePicker(LocalDate defaultDate) {
    var model =
        new UtilDateModel(Date.from(defaultDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

    var i18n = new Properties();
    i18n.put("text.today", "ce jour");

    var datePanel = new JDatePanelImpl(model, i18n);
    return new JDatePickerImpl(datePanel, new DateFormatter());
  }

  private void addButtons() {
    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setOpaque(true);
    buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    var cancelButton = new Button("Annuler", e -> dispose());
    var saveButton = new Button("Sauvegarder", e -> executeOperation());

    buttonPanel.add(cancelButton);
    buttonPanel.add(saveButton);

    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void executeOperation() {
    File casSet = FileSideBar.getDoneCasSetFile();
    File selectedFile = state.get("selectedFile");

    AsyncTask.<Void>builder()
        .task(
            () -> {
              var sectionOperation =
                  patriLangFileQuerier.query(
                      selectedFile.getAbsolutePath(),
                      document -> document.cas().sectionOperations());
              if (sectionOperation.isEmpty()) {
                throw new RuntimeException("Section Operations introuvable dans le fichier");
              }

              patriLangFileWritter.insertAtLine(
                  FileWritterInput.builder()
                      .content(getNewLine())
                      .file(selectedFile)
                      .casSet(casSet)
                      .build(),
                  sectionOperation.get().endLine());
              return null;
            })
        .onError(
            error -> {
              if (error instanceof ParseCancellationException e) {
                MessageDialog.error("Erreur", e.getMessage());
                return;
              }

              if (error instanceof IllegalArgumentException e) {
                MessageDialog.error("Erreur", e.getMessage());
                return;
              }

              MessageDialog.error("Erreur", "Une erreur est survenue lors de l'enregistrement");
            })
        .onSuccess(
            result -> {
              MessageDialog.info("Succès", "L'opération a été exécutée avec succès");
              AppContext.getDefault().globalState().update("newUpdate", true);
              dispose();
            })
        .build()
        .execute();
  }

  // TODO: refactor
  @SuppressWarnings("all")
  private String getNewLine() {
    var date =
        LocalDate.of(
            datePicker.getModel().getYear(),
            datePicker.getModel().getMonth() + 1,
            datePicker.getModel().getDay());
    var valeur = parseDouble(valeurField.getText().trim());
    var devise = stringToDevise((String) deviseCombo.getSelectedItem());
    var possession = (FluxArgent) possessionRecoupee.possession();
    var newFluxArgent =
        new FluxArgent(
            possessionRecoupee.possession().nom(),
            possession.getCompte(),
            date,
            new Argent(valeur, devise));
    var patriLangGenerator = PatriLangGeneratorFactory.make(possession);

    return patriLangGenerator.apply(newFluxArgent);
  }
}
