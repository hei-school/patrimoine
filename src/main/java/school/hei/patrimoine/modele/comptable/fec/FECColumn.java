package school.hei.patrimoine.modele.comptable.fec;

public enum FECColumn {
  JOURNAL_CODE("JournalCode"),
  JOURNAL_LIB("JournalLib"),
  ECRITURE_NUM("EcritureNum"),
  ECRITURE_DATE("EcritureDate"),
  COMPTE_NUM("CompteNum"),
  COMPTE_LIB("CompteLib"),
  COMP_AUX_NUM("CompAuxNum"),
  COMP_AUX_LIB("CompAuxLib"),
  PIECE_REF("PieceRef"),
  PIECE_DATE("PieceDate"),
  ECRITURE_LIB("EcritureLib"),
  DEBIT("Debit"),
  CREDIT("Credit"),
  ECRITURE_LET("EcritureLet"),
  DATE_LET("DateLet"),
  VALID_DATE("ValidDate"),
  MONTANT_DEVISE("Montantdevise"),
  IDEVISE("Idevise");

  private final String label;

  FECColumn(String label) {
    this.label = label;
  }

  public String label() {
    return label;
  }
}
