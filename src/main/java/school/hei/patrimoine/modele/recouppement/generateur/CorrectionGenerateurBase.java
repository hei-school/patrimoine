package school.hei.patrimoine.modele.recouppement.generateur;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;

public abstract class CorrectionGenerateurBase<T extends Possession>
    implements CorrectionGenerateur<T> {
  protected boolean memeDate(T prévu, T réalité) {
    return prévu.t().equals(réalité.t());
  }

  protected boolean memeValeur(T prévu, T réalité) {
    return getValeur(prévu).equals(getValeur(réalité));
  }

  protected Argent getValeur(T possession) {
    return possession.valeurComptable();
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

  protected String dateDifferenteNom(T prévu, T réalité){
      if(prévu.t().isAfter(réalité.t())){
            return enAvanceNom(prévu) ;
      }

      return enRetardNom(prévu);
  }
}
