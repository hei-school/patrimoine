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
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.formatter.ArgentFormatter;
import school.hei.patrimoine.visualisation.swing.ihm.google.validator.NombreValidator;

public class AddRecoupementExecutionForm extends JPanel {
  private final Devise defaultDevise;
  private final Argent defaultValeur;
  private final String defaultNom;

  private JTextField nomField;
  private JTextField valeurField;
  private JComboBox<String> deviseCombo;
  private DatePicker executionDatePicker;

  public AddRecoupementExecutionForm(
      Set<Pair<String, JComponent>> components,
      String defaultNom,
      Devise defaultDevise,
      Argent defaultValeur) {
    this(defaultNom, defaultDevise, defaultValeur);

    components.forEach(
        pair -> {
          add(styledLabel(pair.first()));
          styleInput(pair.second());
          add(Box.createVerticalStrut(5));
          add(pair.second());
          add(Box.createVerticalStrut(15));
        });
  }

  public AddRecoupementExecutionForm(
      String defaultNom, Devise defaultDevise, Argent defaultValeur) {
    super();
    this.defaultNom = defaultNom;
    this.defaultDevise = defaultDevise;
    this.defaultValeur = defaultValeur;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(true);
    setBorder(new EmptyBorder(15, 15, 15, 15));
    setBackground(Color.WHITE);

    addNomField();
    addDateField();
    addValeurField();
    addDeviseField();
  }

  private JLabel styledLabel(String text) {
    var label = new JLabel(text);
    label.setFont(new Font("Arial", Font.BOLD, 18));
    label.setBorder(new EmptyBorder(5, 8, 5, 8));
    label.setAlignmentX(Component.LEFT_ALIGNMENT);

    return label;
  }

  private void styleInput(JComponent input) {
    input.setFont(new Font("Arial", Font.PLAIN, 20));
    input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    input.setAlignmentX(Component.LEFT_ALIGNMENT);
  }

  private void addNomField() {
    add(styledLabel("Nom :"));

    nomField = new JTextField(defaultNom);
    styleInput(nomField);
    add(Box.createVerticalStrut(5));
    add(nomField);
    add(Box.createVerticalStrut(15));
  }

  private void addDateField() {
    add(styledLabel("Date de l'exécution :"));

    executionDatePicker = new DatePicker(LocalDate.now());
    styleInput(executionDatePicker);
    add(Box.createVerticalStrut(5));
    add(executionDatePicker);
    add(Box.createVerticalStrut(15));
  }

  private void addValeurField() {
    add(styledLabel("Valeur :"));

    valeurField = new JTextField(ArgentFormatter.montant(defaultValeur));
    styleInput(valeurField);
    add(Box.createVerticalStrut(5));
    add(valeurField);
    add(Box.createVerticalStrut(15));
  }

  private void addDeviseField() {
    add(styledLabel("Devise :"));

    deviseCombo = new JComboBox<>(new String[] {EUR.symbole(), MGA.symbole(), CAD.symbole()});
    styleInput(deviseCombo);
    deviseCombo.setSelectedItem(defaultDevise);
    add(Box.createVerticalStrut(5));
    add(deviseCombo);
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
    if (nomField.getText().isBlank()) {
      throw new IllegalArgumentException("Le champ 'Nom' est obligatoire.");
    }

    return nomField.getText().trim().replaceAll(" ", "_");
  }
}
