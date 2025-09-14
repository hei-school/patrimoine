package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static school.hei.patrimoine.patrilang.mapper.DeviseMapper.stringToDevise;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionReoupeeDetailDialog.makeInfoRow;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.jdatepicker.impl.*;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.patrilang.generator.PatriLangGeneratorFactory;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.selecteur.jdatepicker.DateFormatter;

public class PossessionRecoupeeExecuteDialog extends Dialog {
  private final Runnable refresh;
  private final PossessionRecoupee possessionRecoupee;

  private JDatePickerImpl datePicker;
  private JComboBox<String> deviseCombo;
  private JTextField valeurField;

  public PossessionRecoupeeExecuteDialog(Runnable refresh, PossessionRecoupee possessionRecoupee) {
    super("Exécuter la possession", 500, 300, false);
    this.refresh = refresh;
    this.possessionRecoupee = possessionRecoupee;

    setLayout(new BorderLayout());
    setBackground(Color.WHITE);

    addForm();
    addButtons();

    setVisible(true);
  }

  private void addForm() {
    var panel = new JPanel(new GridLayout(0, 1, 10, 10));
    panel.setOpaque(true);
    panel.setBorder(new EmptyBorder(15, 15, 15, 15));
    panel.setBackground(Color.WHITE);

    panel.add(makeInfoRow("Date d'exécution", null));
    datePicker = createDatePicker(LocalDate.now());
    panel.add(datePicker);

    panel.add(makeInfoRow("Valeur réalisée", null));
    valeurField = new JTextField(possessionRecoupee.valeurPrevu().ppMontant());
    panel.add(valeurField);

    panel.add(makeInfoRow("Devise", null));
    deviseCombo = new JComboBox<>(new String[] {"Ar", "€", "$"});
    panel.add(deviseCombo);

    add(panel, BorderLayout.CENTER);
  }

  private static JDatePickerImpl createDatePicker(LocalDate defaultDate) {
    var model =
        new UtilDateModel(Date.from(defaultDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    var datePanel = new JDatePanelImpl(model, new Properties());
    return new JDatePickerImpl(datePanel, new DateFormatter());
  }

  private void addButtons() {
    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    buttonPanel.setBackground(Color.WHITE);

    var cancelButton = new Button("Annuler", e -> dispose());
    var saveButton = new Button("Sauvegarder", e -> saveExecute());

    buttonPanel.add(cancelButton);
    buttonPanel.add(saveButton);

    add(buttonPanel, BorderLayout.SOUTH);
  }

  @SuppressWarnings("all")
  private void saveExecute() {
    var date =
        LocalDate.of(
            datePicker.getModel().getYear(),
            datePicker.getModel().getMonth() + 1,
            datePicker.getModel().getDay());
    var valeur = Double.parseDouble(valeurField.getText().trim());
    var devise = stringToDevise((String) deviseCombo.getSelectedItem());
    var possession = (FluxArgent) possessionRecoupee.possession();
    var newFluxArgent =
        new FluxArgent(
            possessionRecoupee.possession().nom(),
            possession.getCompte(),
            date,
            new Argent(valeur, devise));
    var patriLangGenerator = PatriLangGeneratorFactory.make(possession);
    System.out.println(patriLangGenerator.apply(possession));
  }
}
