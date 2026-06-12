package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

public class AddExecutionFieldsValidator {
  private AddExecutionFieldsValidator() {}

  static boolean hasLinkPJ(AddRecoupementExecutionForm form) {
    var linkPJ = form.getLinkPJ();
    return linkPJ != null && !linkPJ.isBlank();
  }

  static boolean hasReferencePJ(AddRecoupementExecutionForm form) {
    var referencePJ = form.getReferencePJ();
    return referencePJ != null && !referencePJ.isBlank();
  }

  static boolean hasPJ(AddRecoupementExecutionForm form) {
    return hasLinkPJ(form) && hasReferencePJ(form);
  }

  static boolean hasComment(AddRecoupementExecutionForm form) {
    var comment = form.getComment();
    return comment != null && !comment.isBlank();
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

    if (hasComment(form) && form.getComment().contains("\"")) {
      throw new IllegalArgumentException("Le commentaire ne doit pas contenir de guillemets (\").");
    }
  }
}
