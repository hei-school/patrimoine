package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;

import java.awt.*;
import java.util.*;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee.Info;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.PossessionGeneratorFactory;

public class AddRecoupementExecutionDialog extends Dialog {
  private final AddRecoupementExecutionForm form;
  private final PossessionRecoupee possessionRecoupee;
  private final Consumer<Info> onAddConsumer;

  public AddRecoupementExecutionDialog(
      PossessionRecoupee possessionRecoupee, Consumer<Info> onAddConsumer) {
    super("Exécuter la possession", 700, 600, false);
    this.onAddConsumer = onAddConsumer;
    this.possessionRecoupee = possessionRecoupee;
    this.form =
        new AddRecoupementExecutionForm(
            possessionRecoupee.possession().nom(),
            possessionRecoupee.possession().devise(),
            possessionRecoupee.info().valeur());

    setLayout(new BorderLayout());
    setBackground(Color.WHITE);

    addTitle();
    add(form, BorderLayout.CENTER);
    addButtons();

    setVisible(true);
  }

  private void addTitle() {
    var titleString =
        String.format(
            "Ajout d'exécution de l'opération : %s", possessionRecoupee.possession().nom());
    var title = new JLabel(titleString);
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

  private void addExecution() {
    var args = getArgsFacade();
    var generator = PossessionGeneratorFactory.make(possessionRecoupee.possession());
    var newPossession = generator.apply(args);

    onAddConsumer.accept(new Info(form.getDate(), form.getValeur(), newPossession));
  }

  private Map<String, Object> getArgsFacade() {
    return switch (possessionRecoupee.possession()) {
      case FluxArgent ignored -> getFluxArgentArgs();
      default -> throw new IllegalArgumentException("Not supported yet");
    };
  }

  private Map<String, Object> getFluxArgentArgs() {
    var possession = (FluxArgent) possessionRecoupee.possession();

    return Map.of(
        "date", form.getDate(),
        "valeur", form.getValeur(),
        "nom", form.getNom(),
        "compte", possession.getCompte(),
        "prevu", possessionRecoupee.prevu().possession());
  }
}
