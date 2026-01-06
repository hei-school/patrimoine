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
                new FluxArgent("salaire", comptePersonnelPrevu, au02Mars2025, ariary(200), null)));

    var realise =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelrealise,
                new FluxArgent(
                    "salaire", comptePersonnelrealise, au02Mars2025, ariary(200), null)));

    var subject = RecoupeurDePossessions.of(LocalDate.MAX, prevu, realise);

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
                new FluxArgent("salaire", comptePersonnelPrevu, au02Mars2025, ariary(300), null)));

    var realise =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelrealise,
                new FluxArgent(
                    "salaire", comptePersonnelrealise, au02Mars2025, ariary(200), null)));

    var subject = RecoupeurDePossessions.of(LocalDate.MAX, prevu, realise);

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
                    "salaire",
                    comptePersonnelPrevu,
                    au02Mars2025,
                    au02Juin2025,
                    2,
                    ariary(200),
                    null)));

    var realise =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelrealise,
                new FluxArgent(
                    String.format("salaire__du_%s", au02Mars2025).replaceAll("-", "_"),
                    comptePersonnelrealise,
                    au02Mars2025,
                    ariary(200),
                    null),
                new FluxArgent(
                    String.format("salaire__du_%s", au02Mai2025).replaceAll("-", "_"),
                    comptePersonnelrealise,
                    au02Mai2025,
                    ariary(200),
                    null)));

    var subject = RecoupeurDePossessions.of(LocalDate.MAX, prevu, realise);

    assertEquals(2, subject.getCorrections().size());
    assertEquals(2, subject.getPossessionsNonExecutes().size());
    assertTrue(subject.getPossessionsExecutesAvecCorrections().isEmpty());
    assertEquals(3, subject.getPossessionsExecutesSansCorrections().size());
  }

  @Test
  void detecte_possession_en_trop_dans_reel() {
    var date = LocalDate.of(2025, JANUARY, 1);
    var compte = new Compte("comptePersonnel", date, ariary(0));
    var fluxPrevu = new FluxArgent("prime", compte, date, ariary(100), null);
    var fluxDeBonus = new FluxArgent("bonus", compte, date, ariary(50), null);
    var fluxEnTrop = new FluxArgent("cadeau", compte, date, ariary(20), null);

    var prevu = Patrimoine.of("zety", MGA, date, Map.of(), Set.of(compte, fluxPrevu));
    var realise =
        Patrimoine.of(
            "zety", MGA, date, Map.of(), Set.of(compte, fluxPrevu, fluxDeBonus, fluxEnTrop));

    var subject = RecoupeurDePossessions.of(LocalDate.MAX, prevu, realise);

    assertEquals(2, subject.getPossessionsNonPrevus().size());
    assertTrue(
        subject.getPossessionsNonPrevus().stream()
            .anyMatch(p -> p.nom().equals("bonus") || p.nom().equals("cadeau")));
  }

  @Test
  void detecte_possession_manquante_dans_reel() {
    var date = LocalDate.of(2025, JANUARY, 1);
    var compte = new Compte("comptePersonnel", date, ariary(0));
    var fluxPrevuPrime = new FluxArgent("prime", compte, date, ariary(100), null);
    var fluxPrevuBonus = new FluxArgent("bonus", compte, date, ariary(50), null);

    var prevu =
        Patrimoine.of("zety", MGA, date, Map.of(), Set.of(compte, fluxPrevuPrime, fluxPrevuBonus));
    var realise = Patrimoine.of("zety", MGA, date, Map.of(), Set.of(compte, fluxPrevuPrime));

    var subject = RecoupeurDePossessions.of(LocalDate.MAX, prevu, realise);

    assertEquals(1, subject.getPossessionsNonExecutes().size());
    assertTrue(subject.getPossessionsNonExecutes().stream().anyMatch(p -> p.nom().equals("bonus")));
  }

  @Test
  void detecte_nom_de_possession_different() {
    var date = LocalDate.of(2025, JANUARY, 1);
    var comptePrevu = new Compte("comptePersonnel", date, ariary(0));
    var compteRealise = new Compte("comptePerso", date, ariary(0));

    var prevu = Patrimoine.of("zety", MGA, date, Map.of(), Set.of(comptePrevu));
    var realise = Patrimoine.of("zety", MGA, date, Map.of(), Set.of(compteRealise));

    var subject = RecoupeurDePossessions.of(LocalDate.MAX, prevu, realise);

    assertEquals(1, subject.getPossessionsNonExecutes().size());
    assertEquals(1, subject.getPossessionsNonPrevus().size());
    assertTrue(
        subject.getPossessionsNonExecutes().stream()
            .anyMatch(p -> p.nom().equals("comptePersonnel")));
    assertTrue(
        subject.getPossessionsNonPrevus().stream().anyMatch(p -> p.nom().equals("comptePerso")));
  }

  @Test
  void detecte_type_de_possession_different() {
    var date = LocalDate.of(2025, JANUARY, 1);
    var comptePrevu = new Compte("comptePersonnel", date, ariary(0));
    var compteOwner = new Compte("owner", date, ariary(0));

    var prevu = Patrimoine.of("zety", MGA, date, Map.of(), Set.of(comptePrevu));
    var realise =
        Patrimoine.of(
            "zety",
            MGA,
            date,
            Map.of(),
            Set.of(
                compteOwner,
                new FluxArgent("comptePersonnel", compteOwner, date, ariary(100), null)));

    var subject = RecoupeurDePossessions.of(LocalDate.MAX, prevu, realise);

    assertEquals(1, subject.getPossessionsNonExecutes().size());
    assertEquals(2, subject.getPossessionsNonPrevus().size());
    assertTrue(
        subject.getPossessionsNonExecutes().stream()
            .anyMatch(p -> p.nom().equals("comptePersonnel")));
    assertTrue(
        subject.getPossessionsNonPrevus().stream()
            .anyMatch(p -> p.nom().equals("comptePersonnel")));
  }

  @Test
  void detecte_date_de_possession_different() {
    var datePrevu = LocalDate.of(2025, JANUARY, 1);
    var dateRealise = LocalDate.of(2025, FEBRUARY, 1);

    var compte = new Compte("comptePersonnel", datePrevu, ariary(0));

    var fluxPrevu = new FluxArgent("salaire", compte, datePrevu, ariary(100), null);
    var fluxRealise = new FluxArgent("salaire", compte, dateRealise, ariary(100), null);

    var prevu = Patrimoine.of("zety", MGA, datePrevu, Map.of(), Set.of(compte, fluxPrevu));
    var realise = Patrimoine.of("zety", MGA, datePrevu, Map.of(), Set.of(compte, fluxRealise));

    var subject = RecoupeurDePossessions.of(LocalDate.MAX, prevu, realise);

    assertEquals(1, subject.getPossessionsExecutesAvecCorrections().size());
    assertTrue(
        subject.getPossessionsExecutesAvecCorrections().stream()
            .anyMatch(p -> p.nom().equals("salaire")));
  }

  @Test
  void generer_correction_si_plusieurs_ecarts_existent() {
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
                new FluxArgent("salaire", comptePersonnelPrevu, au02Mars2025, ariary(300), null)));

    var realise =
        Patrimoine.of(
            "zety",
            MGA,
            au01Janvier2025,
            Map.of(),
            Set.of(
                comptePersonnelrealise,
                new FluxArgent(
                    "salaire", comptePersonnelrealise, au02Mars2025, ariary(100), null)));

    var subject = RecoupeurDePossessions.of(LocalDate.MAX, prevu, realise);

    assertEquals(
        sortedWithoutCompteCorrectionsPossessions(prevu.getPossessions()),
        sortedWithoutCompteCorrectionsPossessions(subject.getPossessionsExecutes()));
    assertEquals(1, subject.getCorrections().size());
    assertEquals(1, subject.getPossessionsExecutesAvecCorrections().size());
    assertEquals(1, subject.getPossessionsExecutesSansCorrections().size());
  }

  private static List<Possession> sortedWithoutCompteCorrectionsPossessions(
      Set<Possession> possessions) {
    return possessions.stream()
        .filter(not(p -> p instanceof CompteCorrection))
        .sorted(Comparator.comparing(Possession::nom))
        .toList();
  }
}
