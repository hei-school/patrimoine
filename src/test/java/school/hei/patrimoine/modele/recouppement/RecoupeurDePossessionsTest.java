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
  void sans_correction_si_prévu_et_réel_sont_pareil() {
    var au01Janvier2025 = LocalDate.of(2025, JANUARY, 1);
    var au02Mars2025 = LocalDate.of(2025, MARCH, 2);

    var comptePersonnelPrévu = new Compte("comptePersonnel", au01Janvier2025, ariary(0));
    var comptePersonnelRéalité = new Compte("comptePersonnel", au01Janvier2025, ariary(0));

    var prévu =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelPrévu,
                new FluxArgent("salaire", comptePersonnelPrévu, au02Mars2025, ariary(200))));

    var réalité =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelRéalité,
                new FluxArgent("salaire", comptePersonnelRéalité, au02Mars2025, ariary(200))));

    var subject = RecoupeurDePossessions.of(prévu, réalité);

    assertTrue(subject.getCorrections().isEmpty());
    assertTrue(subject.getPossessionsNonPrévus().isEmpty());
    assertTrue(subject.getPossessionsNonExecutés().isEmpty());
    assertTrue(subject.getPossessionsÉxecutésAvecCorrections().isEmpty());
    assertEquals(
        sortedWithoutCompteCorrectionsPossessions(prévu.getPossessions()),
        sortedWithoutCompteCorrectionsPossessions(subject.getPossessionsÉxecutésSansCorrections()));
    assertEquals(
        sortedWithoutCompteCorrectionsPossessions(prévu.getPossessions()),
        sortedWithoutCompteCorrectionsPossessions(subject.getPossessionsÉxecutés()));
  }

  @Test
  void generer_correction_si_valeur_comptable_de_possession_différent() {
    var au01Janvier2025 = LocalDate.of(2025, JANUARY, 1);
    var au02Mars2025 = LocalDate.of(2025, MARCH, 2);

    var comptePersonnelPrévu = new Compte("comptePersonnel", au01Janvier2025, ariary(200));
    var comptePersonnelRéalité = new Compte("comptePersonnel", au01Janvier2025, ariary(200));

    var prévu =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelPrévu,
                new FluxArgent("salaire", comptePersonnelPrévu, au02Mars2025, ariary(300))));

    var réalité =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelRéalité,
                new FluxArgent("salaire", comptePersonnelRéalité, au02Mars2025, ariary(200))));

    var subject = RecoupeurDePossessions.of(prévu, réalité);

    assertTrue(subject.getPossessionsNonPrévus().isEmpty());
    assertTrue(subject.getPossessionsNonExecutés().isEmpty());
    assertEquals(
        sortedWithoutCompteCorrectionsPossessions(prévu.getPossessions()),
        sortedWithoutCompteCorrectionsPossessions(subject.getPossessionsÉxecutés()));

    assertEquals(1, subject.getCorrections().size());
    assertEquals(1, subject.getPossessionsÉxecutésAvecCorrections().size());
    assertEquals(1, subject.getPossessionsÉxecutésSansCorrections().size());
  }

  @Test
  void generer_correction_pour_flux_argent_decomposer() {
    var au01Janvier2025 = LocalDate.of(2025, JANUARY, 1);

    var au02Mars2025 = LocalDate.of(2025, MARCH, 2);
    var au02Mai2025 = LocalDate.of(2025, MAY, 2);
    var au02Juin2025 = LocalDate.of(2025, JUNE, 2);

    var comptePersonnelPrévu = new Compte("comptePersonnel", au01Janvier2025, ariary(0));
    var comptePersonnelRéalité = new Compte("comptePersonnel", au01Janvier2025, ariary(0));

    var prévu =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelPrévu,
                new FluxArgent(
                    "salaire", comptePersonnelPrévu, au02Mars2025, au02Juin2025, 2, ariary(200))));

    var réalité =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelRéalité,
                new FluxArgent(
                    "salaire__du_" + au02Mars2025,
                    comptePersonnelRéalité,
                    au02Mars2025,
                    ariary(200)),
                new FluxArgent(
                    "salaire__du_" + au02Mai2025,
                    comptePersonnelRéalité,
                    au02Mai2025,
                    ariary(200))));

    var subject = RecoupeurDePossessions.of(prévu, réalité);

    assertEquals(2, subject.getCorrections().size());
    assertEquals(2, subject.getPossessionsNonExecutés().size());
    assertTrue(subject.getPossessionsÉxecutésAvecCorrections().isEmpty());
    assertEquals(3, subject.getPossessionsÉxecutésSansCorrections().size());
  }

  private static List<Possession> sortedWithoutCompteCorrectionsPossessions(
      Set<Possession> possessions) {
    return possessions.stream()
        .filter(not(p -> p instanceof CompteCorrection))
        .sorted(Comparator.comparing(Possession::nom))
        .toList();
  }
}
