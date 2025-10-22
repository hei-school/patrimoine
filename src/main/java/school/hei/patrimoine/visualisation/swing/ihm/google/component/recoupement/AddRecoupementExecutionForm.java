package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static java.lang.Double.parseDouble;
import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.modele.Devise.*;

import java.awt.*;
import java.time.LocalDate;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.patrilang.mapper.DeviseMapper;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.DatePicker;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.validator.NombreValidator;

public class AddRecoupementExecutionForm extends JPanel {
  private JTextField nomField;
  private JTextField valeurField;
  private JComboBox<String> deviseCombo;
  private DatePicker executionDatePicker;

  public AddRecoupementExecutionForm(
      String defaultNom,
      Devise defaultDevise,
      Argent defaultValeur,
      Set<Pair<String, JComponent>> components) {

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(true);
    setBorder(new EmptyBorder(15, 15, 15, 15));
    setBackground(Color.WHITE);

    addNomField(defaultNom);
    addDateField(LocalDate.now());
    addValeurField(defaultValeur);
    addDeviseField(defaultDevise);

    components.forEach(component -> addField(component.first(), component.second()));
  }

  public AddRecoupementExecutionForm(
      String defaultNom, Devise defaultDevise, Argent defaultValeur) {
    this(defaultNom, defaultDevise, defaultValeur, Set.of());
  }

  public Argent getValeur() {
    var textFieldValue = valeurField.getText();
    NombreValidator.validate(textFieldValue);

    var montant = parseDouble(textFieldValue.trim().replaceAll(" ", "").replaceAll("_", ""));
    return new Argent(
        montant,
        DeviseMapper.stringToDevise(requireNonNull(deviseCombo.getSelectedItem()).toString()));
  }

  public LocalDate getDate() {
    return LocalDate.of(
        executionDatePicker.getModel().getYear(),
        executionDatePicker.getModel().getMonth() + 1,
        executionDatePicker.getModel().getDay());
  }

  public String getNom() {
    if (nomField.getText().trim().isBlank()) {
      throw new IllegalArgumentException("Le champ 'Nom' est obligatoire.");
    }

    return nomField.getText().trim().replaceAll(" ", "_");
  }

  private void addNomField(String defaultNom) {
    nomField = new JTextField(defaultNom);
    addField("Nom : ", nomField);
  }

  private void addDateField(LocalDate defaultDate) {
    executionDatePicker = new DatePicker(defaultDate);

    addField("Date de l'exécution : ", executionDatePicker);
  }

  private void addField(String label, JComponent input) {
    add(styledLabel(label));
    add(Box.createVerticalStrut(5));
    add(styledInput(input));
    add(Box.createVerticalStrut(15));
  }

  private void addValeurField(Argent defaultValeur) {
    valeurField = new JTextField(ArgentFormatter.montant(defaultValeur));
    addField("Valeur : ", valeurField);
  }

  private void addDeviseField(Devise defaultDevise) {
    deviseCombo = new JComboBox<>(new String[] {EUR.symbole(), MGA.symbole(), CAD.symbole()});
    deviseCombo.setSelectedItem(defaultDevise.symbole());

    addField("Devise : ", deviseCombo);
  }

  private static JLabel styledLabel(String text) {
    var label = new JLabel(text);
    label.setFont(new Font("Arial", Font.BOLD, 18));
    label.setBorder(new EmptyBorder(5, 8, 5, 8));
    label.setAlignmentX(Component.LEFT_ALIGNMENT);

    return label;
  }

  private static JComponent styledInput(JComponent input) {
    input.setFont(new Font("Arial", Font.PLAIN, 20));
    input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    input.setAlignmentX(Component.LEFT_ALIGNMENT);

    return input;
  }
}
