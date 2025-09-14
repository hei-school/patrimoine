package school.hei.patrimoine.modele.recouppement.generateur;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;

public abstract class RecoupeurDePossessionBase<T extends Possession> implements RecoupeurDePossession<T> {
  protected boolean memeDate(T prévu, T réalisé) {
    return getDate(prévu).equals(getDate(réalisé));
  }

  protected boolean memeValeur(T prévu, T réalisé) {
    return getValeur(prévu).equals(getValeur(réalisé));
  }

  protected Argent getValeur(T possession) {
    return possession.valeurComptable();
  }

  protected LocalDate getDate(T possession){
      return possession.t();
  }

  protected String imprevuNom(T possession) {
    return "Imprévu – " + possession.nom();
  }

  protected String nonExecuteNom(T possession) {
    return "Non exécuté – " + possession.nom();
  }

  protected String enRetardNom(T possession) {
    return "En retard – " + possession.nom();
  }

  protected String enAvanceNom(T possession) {
    return "En avance – " + possession.nom();
  }

  protected String valeurDifferenteNom(T possession) {
    return "Valeur différente – " + possession.nom();
  }

  protected String dateDifferenteNom(T prévu, T réalisé){
      if(prévu.t().isAfter(réalisé.t())){
            return enAvanceNom(prévu) ;
      }

      return enRetardNom(prévu);
  }
}
