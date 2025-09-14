package school.hei.patrimoine.modele.recouppement.generateur;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;

public abstract class RecoupeurDePossessionBase<T extends Possession>
    implements RecoupeurDePossession<T> {
  protected boolean memeDate(T prevu, T realise) {
    return getDate(prevu).equals(getDate(realise));
  }

  protected boolean memeValeur(T prevu, T realise) {
    return getValeur(prevu).equals(getValeur(realise));
  }

  protected Argent getValeur(T possession) {
    return possession.valeurComptable();
  }

  protected LocalDate getDate(T possession) {
    return possession.t();
  }

  protected String imprevuNom(T possession) {
    return "imprevu – " + possession.nom();
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

  protected String dateDifferenteNom(T prevu, T realise) {
    if (prevu.t().isAfter(realise.t())) {
      return enAvanceNom(prevu);
    }

    return enRetardNom(prevu);
  }
}
