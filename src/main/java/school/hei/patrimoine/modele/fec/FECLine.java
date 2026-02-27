package school.hei.patrimoine.modele.fec;

import lombok.Builder;

@Builder
public record FECLine(
    String journalCode,
    String journalLib,
    String ecritureNum,
    String ecritureDate,
    String compteNum,
    String compteLib,
    String compAuxNum,
    String compAuxLib,
    String pieceRef,
    String pieceDate,
    String ecritureLib,
    String debit,
    String credit,
    String ecritureLet,
    String dateLet,
    String validDate,
    String montantDevise,
    String idevise) {
  public String[] toArray() {
    return new String[] {
      journalCode,
      journalLib,
      ecritureNum,
      ecritureDate,
      compteNum,
      compteLib,
      compAuxNum,
      compAuxLib,
      pieceRef,
      pieceDate,
      ecritureLib,
      debit,
      credit,
      ecritureLet,
      dateLet,
      validDate,
      montantDevise,
      idevise
    };
  }
}
