package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 = new Patrimoine(
            "patrimoineIloAu13mai24",
            ilo,
            LocalDate.of(2024, MAY, 13),
            Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 = new Patrimoine(
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
    var trainDeVie = new FluxArgent(
            "Vie courante",
            financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
            15);

    var patrimoineIloAu13mai24 = new Patrimoine(
            "patrimoineIloAu13mai24",
            ilo,
            au13mai24,
            Set.of(financeur, trainDeVie));

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_possede_groupe_de_train_de_vie_et_d_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
            "Vie courante",
            financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
            15);

    var patrimoineIloAu13mai24 = new Patrimoine(
            "patrimoineIloAu13mai24",
            ilo,
            au13mai24,
            Set.of(new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie))));

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_de_zety_le_17_septembre_2024() {
    var Zety = new Personne("Zety");

    var au03Juil24 = LocalDate.of(2024, JULY, 3);

    var espece = new  Argent("Argent en espèce", au03Juil24, 800_000);
    var compteBancaire = new Argent("Compte bancaire",  au03Juil24, 100_000);

    var ordinateur = new Materiel(
            "Ordinateur portable",
            au03Juil24,
            1_200_000,
            au03Juil24.minusDays(2),
            -0.1
    );

    var vetements = new Materiel(
            "Vêtements",
            au03Juil24,
            1_500_000,
            au03Juil24.minusDays(2),
            -0.5
    );

    var debutFraisDeScolarite = LocalDate.of(2023, NOVEMBER, 27);
    var finFraisDeScolarite = LocalDate.of(2024, AUGUST, 27);

    var vieEstudiantine = new FluxArgent(
            "La vie estudiantine de Zety",
            espece,
            debutFraisDeScolarite,
            finFraisDeScolarite,
            -200_000,
            27
    );

    var ponctionProche = LocalDate.of(2024, JULY, 25);

    var fraisDeTenueDuCompte = new FluxArgent(
            "Frais de tenue du coimpte",
            compteBancaire,
            ponctionProche,
            LocalDate.MAX,
            -20_000,
            25
    );

    var patrimoineZety = new Patrimoine("Patrimoine de Zety", Zety, au03Juil24,
            Set.of(new GroupePossession("Groupe de possessions", au03Juil24, Set.of(espece, compteBancaire, ordinateur, vetements)))
    );

    var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);

    assertEquals(3_600_000, patrimoineZety.getValeurComptable());
    assertEquals(2_978_848, patrimoineZety.projectionFuture(au17Sept2024).getValeurComptable());
  }

  @Test
  void patrimoine_de_Zety_a_diminué() {
    var Zety = new Personne("Zety");

    var au03Juil24 = LocalDate.of(2024, JULY, 3);

    var espece = new  Argent("Argent en espèce", au03Juil24, 800_000);
    var compteBancaire = new Argent("Compte bancaire",  au03Juil24, 100_000);

    var ordinateur = new Materiel(
            "Ordinateur portable",
            au03Juil24,
            1_200_000,
            au03Juil24.minusDays(2),
            -0.1
    );

    var vetements = new Materiel(
            "Vêtements",
            au03Juil24,
            1_500_000,
            au03Juil24.minusDays(2),
            -0.5
    );

    var debutFraisDeScolarite = LocalDate.of(2023, NOVEMBER, 27);
    var finFraisDeScolarite = LocalDate.of(2024, AUGUST, 27);

    var vieEstudiantine = new FluxArgent(
            "La vie estudiantine de Zety",
            espece,
            debutFraisDeScolarite,
            finFraisDeScolarite,
            -200_000,
            27
    );

    var ponctionProche = LocalDate.of(2024, JULY, 25);

    var fraisDeTenueDuCompte = new FluxArgent(
            "Frais de tenue du coimpte",
            compteBancaire,
            ponctionProche,
            LocalDate.MAX,
            -20_000,
            25
    );

    var au18Sept24 = LocalDate.of(2024, SEPTEMBER, 18);

    var detteAupresDeBanque = new Dette("Dette auprès de banque", au18Sept24, -11_000_000);

    var detteSurSonCompte  = new FluxArgent(
            "Dette sur son compte",
            compteBancaire,
            au18Sept24,
            au18Sept24,
            detteAupresDeBanque.getValeurComptable(),
            au18Sept24.getDayOfMonth()
    );

    var patrimoineZety = new Patrimoine("Patrimoine de Zety", Zety, au03Juil24,
            Set.of(new GroupePossession("Groupe de possessions", au03Juil24, Set.of(espece, compteBancaire, ordinateur, vetements)))
    );

    var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);

    var patrimoineZetyAu17 = patrimoineZety.projectionFuture(au17Sept2024);
    var patrimoineZetyAu18 = patrimoineZety.projectionFuture(au18Sept24);

    assertEquals(3_600_000, patrimoineZety.getValeurComptable());
    assertEquals(2_978_848, patrimoineZetyAu17.getValeurComptable());
    assertEquals(-8023536, patrimoineZetyAu18.getValeurComptable());

    assertEquals(11_002_384, patrimoineZetyAu17.getValeurComptable() - patrimoineZetyAu18.getValeurComptable());

  }
}