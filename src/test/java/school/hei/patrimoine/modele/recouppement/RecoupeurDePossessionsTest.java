package school.hei.patrimoine.modele.recouppement;

import static java.time.Month.*;
import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.CompteCorrection;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

class RecoupeurDePossessionsTest {
  @Test
  void sans_correction_si_prevu_et_réel_sont_pareil() {
    var au01Janvier2025 = LocalDate.of(2025, JANUARY, 1);
    var au02Mars2025 = LocalDate.of(2025, MARCH, 2);

    var comptePersonnelPrevu = new Compte("comptePersonnel", au01Janvier2025, ariary(0));
    var comptePersonnelrealise = new Compte("comptePersonnel", au01Janvier2025, ariary(0));

    var prevu =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelPrevu,
                new FluxArgent("salaire", comptePersonnelPrevu, au02Mars2025, ariary(200))));

    var realise =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelrealise,
                new FluxArgent("salaire", comptePersonnelrealise, au02Mars2025, ariary(200))));

    var subject = RecoupeurDePossessions.of(prevu, realise);

    assertTrue(subject.getCorrections().isEmpty());
    assertTrue(subject.getPossessionsNonPrevus().isEmpty());
    assertTrue(subject.getPossessionsNonExecutes().isEmpty());
    assertTrue(subject.getPossessionsExecutesAvecCorrections().isEmpty());
    assertEquals(
        sortedWithoutCompteCorrectionsPossessions(prevu.getPossessions()),
        sortedWithoutCompteCorrectionsPossessions(subject.getPossessionsExecutesSansCorrections()));
    assertEquals(
        sortedWithoutCompteCorrectionsPossessions(prevu.getPossessions()),
        sortedWithoutCompteCorrectionsPossessions(subject.getPossessionsExecutes()));
  }

  @Test
  void generer_correction_si_valeur_comptable_de_possession_différent() {
    var au01Janvier2025 = LocalDate.of(2025, JANUARY, 1);
    var au02Mars2025 = LocalDate.of(2025, MARCH, 2);

    var comptePersonnelPrevu = new Compte("comptePersonnel", au01Janvier2025, ariary(200));
    var comptePersonnelrealise = new Compte("comptePersonnel", au01Janvier2025, ariary(200));

    var prevu =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelPrevu,
                new FluxArgent("salaire", comptePersonnelPrevu, au02Mars2025, ariary(300))));

    var realise =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelrealise,
                new FluxArgent("salaire", comptePersonnelrealise, au02Mars2025, ariary(200))));

    var subject = RecoupeurDePossessions.of(prevu, realise);

    assertTrue(subject.getPossessionsNonPrevus().isEmpty());
    assertTrue(subject.getPossessionsNonExecutes().isEmpty());
    assertEquals(
        sortedWithoutCompteCorrectionsPossessions(prevu.getPossessions()),
        sortedWithoutCompteCorrectionsPossessions(subject.getPossessionsExecutes()));

    assertEquals(1, subject.getCorrections().size());
    assertEquals(1, subject.getPossessionsExecutesAvecCorrections().size());
    assertEquals(1, subject.getPossessionsExecutesSansCorrections().size());
  }

  @Test
  void generer_correction_pour_flux_argent_decomposer() {
    var au01Janvier2025 = LocalDate.of(2025, JANUARY, 1);

    var au02Mars2025 = LocalDate.of(2025, MARCH, 2);
    var au02Mai2025 = LocalDate.of(2025, MAY, 2);
    var au02Juin2025 = LocalDate.of(2025, JUNE, 2);

    var comptePersonnelPrevu = new Compte("comptePersonnel", au01Janvier2025, ariary(0));
    var comptePersonnelrealise = new Compte("comptePersonnel", au01Janvier2025, ariary(0));

    var prevu =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelPrevu,
                new FluxArgent(
                    "salaire", comptePersonnelPrevu, au02Mars2025, au02Juin2025, 2, ariary(200))));

    var realise =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelrealise,
                new FluxArgent(
                    "salaire__du_" + au02Mars2025,
                    comptePersonnelrealise,
                    au02Mars2025,
                    ariary(200)),
                new FluxArgent(
                    "salaire__du_" + au02Mai2025,
                    comptePersonnelrealise,
                    au02Mai2025,
                    ariary(200))));

    var subject = RecoupeurDePossessions.of(prevu, realise);

    assertEquals(2, subject.getCorrections().size());
    assertEquals(2, subject.getPossessionsNonExecutes().size());
    assertTrue(subject.getPossessionsExecutesAvecCorrections().isEmpty());
    assertEquals(3, subject.getPossessionsExecutesSansCorrections().size());
  }

  private static List<Possession> sortedWithoutCompteCorrectionsPossessions(
      Set<Possession> possessions) {
    return possessions.stream()
        .filter(not(p -> p instanceof CompteCorrection))
        .sorted(Comparator.comparing(Possession::nom))
        .toList();
  }
}
