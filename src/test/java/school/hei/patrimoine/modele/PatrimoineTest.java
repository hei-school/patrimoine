package school.hei.patrimoine.modele;

import static java.time.LocalDate.now;
import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.objectif.Objectif;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 =
        Patrimoine.of("patrimoineIloAu13mai24", MGA, LocalDate.of(2024, MAY, 13), ilo, Set.of());

    assertEquals(ariary(0), patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 =
        Patrimoine.of(
            "patrimoineIloAu13mai24",
            MGA,
            au13mai24,
            ilo,
            Set.of(
                new Compte("Espèces", au13mai24, ariary(400_000)),
                new Compte("Compte epargne", au13mai24, ariary(200_000)),
                new Compte("Compte courant", au13mai24, ariary(600_000))));

    assertEquals(ariary(1_200_000), patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_possede_un_train_de_vie_financé_par_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Compte("Espèces", au13mai24, ariary(600_000));
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            15,
            ariary(-100_000));

    var patrimoineIloAu13mai24 =
        Patrimoine.of("patrimoineIloAu13mai24", MGA, au13mai24, ilo, Set.of(financeur, trainDeVie));

    assertEquals(
        ariary(500_000),
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(
        ariary(200_000),
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    new Objectif(patrimoineIloAu13mai24, au13mai24.plusDays(10), ariary(500_000)).verifier();
    new Objectif(patrimoineIloAu13mai24, au13mai24.plusDays(100), ariary(200_000)).verifier();
    new Objectif(financeur, au13mai24.plusDays(10), ariary(500_000)).verifier();
    assertEquals(
        ariary(200_000),
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
    assertFalse(
        new Objectif(patrimoineIloAu13mai24, au13mai24.plusDays(1_000), ariary(200_001))
            .verifier()
            .isEmpty());
    assertFalse(
        new Objectif(patrimoineIloAu13mai24, au13mai24.plusDays(1_000), ariary(199_999))
            .verifier()
            .isEmpty());
  }

  @Test
  void patrimoine_possede_un_train_de_vie_financé_par_argent_puis_corrigé() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Compte("Espèces", au13mai24, ariary(600_000));
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            15,
            ariary(-100_000));

    new Correction(
        new FluxArgent(
            "Correction à la baisse", financeur, au13mai24.plusDays(99), ariary(-10_000)));
    var patrimoineIloAu13mai24 =
        Patrimoine.of("patrimoineIloAu13mai24", MGA, au13mai24, ilo, Set.of(financeur, trainDeVie));

    assertEquals(
        ariary(500_000),
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(
        ariary(190_000),
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(
        ariary(190_000),
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_possede_groupe_de_train_de_vie_et_d_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Compte("Espèces", au13mai24, ariary(600_000));
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            15,
            ariary(-100_000));

    var patrimoineIloAu13mai24 =
        Patrimoine.of(
            "patrimoineIloAu13mai24",
            MGA,
            au13mai24,
            ilo,
            Set.of(
                new GroupePossession("Le groupe", MGA, au13mai24, Set.of(financeur, trainDeVie))));

    assertEquals(
        ariary(500_000),
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(
        ariary(200_000),
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(
        ariary(200_000),
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void notre_compte_joint_est_partage() {
    var moi = new Personne("Ilo");
    var lui = new Personne("Matthieu");
    var joint = new Compte("Compte joint", now(), now(), ariary(10), Set.of());

    var notrePatrimoine =
        Patrimoine.of("Notre patrimoine", MGA, now(), Map.of(lui, 0.6, moi, 0.4), Set.of(joint));
    var monPatrimoine = moi.patrimoine(MGA, now());
    var sonPatrimoine = lui.patrimoine(MGA, now());

    assertEquals(ariary(4), monPatrimoine.getValeurComptable());
    assertEquals(ariary(6), sonPatrimoine.getValeurComptable());
    assertEquals(ariary(10), notrePatrimoine.getValeurComptable());
    var dans1an = now().plusYears(1);
    assertEquals(ariary(4), monPatrimoine.projectionFuture(dans1an).getValeurComptable());
    assertEquals(ariary(6), sonPatrimoine.projectionFuture(dans1an).getValeurComptable());
    assertEquals(ariary(10), notrePatrimoine.projectionFuture(dans1an).getValeurComptable());
  }
}
