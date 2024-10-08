package school.hei.patrimoine.modele;

import static java.time.LocalDate.now;
import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 =
        Patrimoine.of("patrimoineIloAu13mai24", ilo, LocalDate.of(2024, MAY, 13), Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 =
        Patrimoine.of(
            "patrimoineIloAu13mai24",
            ilo,
            au13mai24,
            Set.of(
                new Argent("Espèces", au13mai24, 400_000),
                new Argent("Compte epargne", au13mai24, 200_000),
                new Argent("Compte courant", au13mai24, 600_000)));

    assertEquals(1_200_000, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_possede_un_train_de_vie_financé_par_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            -100_000,
            15);

    var patrimoineIloAu13mai24 =
        Patrimoine.of("patrimoineIloAu13mai24", ilo, au13mai24, Set.of(financeur, trainDeVie));

    assertEquals(
        500_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_possede_groupe_de_train_de_vie_et_d_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            -100_000,
            15);

    var patrimoineIloAu13mai24 =
        Patrimoine.of(
            "patrimoineIloAu13mai24",
            ilo,
            au13mai24,
            Set.of(new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie))));

    assertEquals(
        500_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_devise_mixte_non_nommee_ko() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            -100_000,
            15);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            Patrimoine.of(
                "patrimoineIloAu13mai24",
                ilo,
                au13mai24,
                Set.of(
                    new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie)),
                    new GroupePossession(
                        "un autre groupe", au13mai24, Set.of(financeur, trainDeVie), MGA))));
  }

  @Test
  void notre_compte_joint_est_partage() {
    var moi = new Personne("Ilo");
    var lui = new Personne("Matthieu");
    var joint = new Argent("Compte joint", now(), 10, Map.of(moi, 0.4, lui, 0.6));

    var monPatrimoine = Patrimoine.of("Mon patrimoine", moi, now(), Set.of(joint));
    var sonPatrimoine = Patrimoine.of("Son patrimoine", lui, now(), Set.of(joint));
    var notrePatrimoine =
        new Patrimoine("Notre patrimoine", Set.of(lui, moi), now(), Set.of(joint));

    assertEquals(4, monPatrimoine.getValeurComptable());
    assertEquals(6, sonPatrimoine.getValeurComptable());
    assertEquals(10, notrePatrimoine.getValeurComptable());
    var dans1an = now().plusYears(1);
    assertEquals(4, monPatrimoine.projectionFuture(dans1an).getValeurComptable());
    assertEquals(6, sonPatrimoine.projectionFuture(dans1an).getValeurComptable());
    assertEquals(10, notrePatrimoine.projectionFuture(dans1an).getValeurComptable());
  }
}
