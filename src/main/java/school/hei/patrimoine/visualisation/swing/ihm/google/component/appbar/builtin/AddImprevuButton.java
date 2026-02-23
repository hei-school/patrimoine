package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;

import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.AddImprevuDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class AddImprevuButton extends Button {
  public AddImprevuButton(State state) {
    super(
        "Ajouter un imprévu",
        e -> {
          if (state.get("selectedFile") == null) {
            showError("Erreur", "Veuillez sélectionner un fichier avant d'ajouter un imprévu");
            return;
          }
          new AddImprevuDialog(state);
        });
  }
}
