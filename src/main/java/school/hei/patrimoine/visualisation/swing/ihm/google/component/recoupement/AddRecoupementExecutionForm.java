package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static java.lang.Double.parseDouble;
import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.modele.Devise.*;

import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
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

  private JTextField linkPJField;
  private JTextField referencePJField;
  private JTextField commentField;
  private final boolean authorizePj;

  public AddRecoupementExecutionForm(
      String defaultNom,
      Devise defaultDevise,
      Argent defaultValeur,
      List<Pair<String, JComponent>> startComponents,
      List<Pair<String, JComponent>> endComponents,
      String defaultLinkPJ,
      String defaultComment,
      boolean authorizePj) {

    this.authorizePj = authorizePj;
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(true);
    setBorder(new EmptyBorder(15, 15, 15, 15));
    setBackground(Color.WHITE);

    startComponents.forEach(component -> addField(component.first(), component.second()));
    addNomField(defaultNom);
    addDateField(LocalDate.now());
    addValeurField(defaultValeur);
    addDeviseField(defaultDevise);

    endComponents.forEach(component -> addField(component.first(), component.second()));

    addLinkPJField(defaultLinkPJ);
    addReferencePJField("");
    addCommentField(defaultComment);
  }

  public AddRecoupementExecutionForm(
      String defaultNom,
      Devise defaultDevise,
      Argent defaultValeur,
      List<Pair<String, JComponent>> startComponents,
      List<Pair<String, JComponent>> endComponents,
      boolean authorizePj) {
    this(
        defaultNom,
        defaultDevise,
        defaultValeur,
        startComponents,
        endComponents,
        "",
        "",
        authorizePj);
  }

  public AddRecoupementExecutionForm(
      String defaultNom, Devise defaultDevise, Argent defaultValeur, boolean authorizePj) {
    this(defaultNom, defaultDevise, defaultValeur, List.of(), List.of(), "", "", authorizePj);
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
    var model = executionDatePicker.getModel();
    if (model.getValue() == null) {
      throw new IllegalArgumentException("La date est obligatoire.");
    }
    try {
      return LocalDate.of(model.getYear(), model.getMonth() + 1, model.getDay());
    } catch (DateTimeException e) {
      throw new IllegalArgumentException("La date saisie est invalide : " + e.getMessage());
    }
  }

  public String getNom() {
    var normalized = nomField.getText().trim().replaceAll("[- ]+", "_");
    if (normalized.isBlank()) {
      throw new IllegalArgumentException("Le champ 'Nom' est obligatoire.");
    }
    return normalized;
  }

  public String getLinkPJ() {
    return linkPJField == null ? "" : linkPJField.getText().trim();
  }

  public String getReferencePJ() {
    return referencePJField == null ? "" : referencePJField.getText().trim();
  }

  public String getComment() {
    return commentField == null ? "" : commentField.getText().trim();
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

  private void addLinkPJField(String defaultLinkPJ) {
    linkPJField = new JTextField(defaultLinkPJ == null ? "" : defaultLinkPJ);
    if (authorizePj) {
      addField("Lien de la pièce justificative : ", linkPJField);
    }
  }

  private void addReferencePJField(String defaultReferencePJ) {
    referencePJField = new JTextField(defaultReferencePJ == null ? "" : defaultReferencePJ);
    if (authorizePj) {
      addField("Référence de la pièce justificative : ", referencePJField);
    }
  }

  private void addCommentField(String defaultComment) {
    commentField = new JTextField(defaultComment == null ? "" : defaultComment);
    addField("Commentaire : ", commentField);
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
