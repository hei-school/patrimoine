package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

public class PJFieldsValidator {
  private PJFieldsValidator() {}

  public static boolean hasLinkPJ(AddRecoupementExecutionForm form) {
    var linkPJ = form.getLinkPJ();
    return linkPJ != null && !linkPJ.isBlank();
  }

  public static boolean hasReferencePJ(AddRecoupementExecutionForm form) {
    var referencePJ = form.getReferencePJ();
    return referencePJ != null && !referencePJ.isBlank();
  }

  public static boolean hasPJ(AddRecoupementExecutionForm form) {
    return hasLinkPJ(form) && hasReferencePJ(form);
  }

  public static void validatePJ(AddRecoupementExecutionForm form) {
    if (hasLinkPJ(form) && !hasReferencePJ(form)) {
      throw new IllegalArgumentException(
          "La référence de la pièce justificative est manquante."
              + " Veuillez renseigner les deux champs ou laisser les deux vides.");
    }
    if (!hasLinkPJ(form) && hasReferencePJ(form)) {
      throw new IllegalArgumentException(
          "Le lien de la pièce justificative est manquant."
              + " Veuillez renseigner les deux champs ou laisser les deux vides.");
    }

    if (hasLinkPJ(form) && form.getLinkPJ().contains("\"")) {
      throw new IllegalArgumentException(
          "Le lien de la pièce justificative ne doit pas contenir de guillemets (\").");
    }
  }
}
