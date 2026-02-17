package school.hei.patrimoine.modele.recouppement.generateur.correction;

import static school.hei.patrimoine.modele.normalizer.ArgentNormalizer.normalize;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.model.Info;

class CorrectionNomGenerateur {
  private CorrectionNomGenerateur() {}

  static String make(CorrectionNomType type, LocalDate t, String nom, Argent valeur) {
    return String.format(
        "%s[date=%s, nom=%s, valeur=%s]", type.getNom(), t, nom, normalize(valeur));
  }

  static String make(CorrectionNomType type, Info<? extends Possession> info) {
    return make(type, info.t(), info.nom(), info.valeur());
  }

  @RequiredArgsConstructor
  @Getter(AccessLevel.PACKAGE)
  enum CorrectionNomType {
    EN_AVANCE("EnAvance"),
    EN_RETARD("EnRetard"),
    IMPREVU("Imprévu"),
    VALEUR_DIFFERENTES("ValeurDifférentes"),
    NON_EXECUTE("NonExecuté");

    private final String nom;
  }
}
