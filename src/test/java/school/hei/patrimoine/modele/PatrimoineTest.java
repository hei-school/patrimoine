package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static java.time.Month.SEPTEMBER;
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
  void date_où_zety_n_a_plus_d_argent_en_espece() {
    var zety = new Personne("Zety");
    var le21Sept2021 = LocalDate.of(2024, SEPTEMBER, 21);
    var financer = new Argent("Compte bancaire", le21Sept2021, -10_200_000);
  }

  @Test
  void patrimoine_de_zety_avec_des_groupes_de_possesion_et_flux_tresories() {
    var zety = new Personne("Zety");

    var au3Juillet24 = LocalDate.of(2024, JULY, 03);
    var FluxAu27Nov23 = LocalDate.of(2023, NOVEMBER, 27);
    var FluxAu27Aug24 = LocalDate.of(2024, AUGUST, 27);
    var FluxCompteAu25Jul24 = LocalDate.of(2024, JULY, 25);
    var FluxCompteAu31Dec25 = LocalDate.of(2025, DECEMBER, 31);
    var Au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);

    var argentEspece = new Argent("Espèce", au3Juillet24, 800_000);
    var compteBancaire = new Argent("Compte bancaire", au3Juillet24, 100_000);

    var ordinateur = new Materiel("ordinateur", au3Juillet24, 1_200_000, au3Juillet24.minusDays(1), -0.10);
    var vetements = new Materiel("vêtement", au3Juillet24, 1_500_000, au3Juillet24.minusDays(1), -0.50);

    var fraisScolaire = new FluxArgent("frais de scolarité", argentEspece, FluxAu27Nov23, FluxAu27Aug24, -200_000, 27);
    var fraiCompte = new FluxArgent("frais de compte", compteBancaire, FluxCompteAu25Jul24, FluxCompteAu31Dec25, -20_000, 25);

    //var financer = new Argent("Espèce", au3Juillet24, 900_000);

    var patrimoineZetyAu3Juillet24 = new Patrimoine(
            "patrimoineZetyAu3Juillet24",
            zety,
            au3Juillet24,
            Set.of(new GroupePossession("Le groupe", au3Juillet24, Set.of(
                    ordinateur, vetements, argentEspece, compteBancaire
            )))
    );

    var patrimoineZetyAu17Sep24 = patrimoineZetyAu3Juillet24.projectionFuture(Au17Sept2024).getValeurComptable();
    assertEquals(2_978_848, patrimoineZetyAu17Sep24);
  }

  @Test
  void patrimoine_de_zety_avec_dette() {
    var zety = new Personne("Zety");

    var au3Juillet24 = LocalDate.of(2024, JULY, 03);
    var FluxAu27Nov23 = LocalDate.of(2023, NOVEMBER, 27);
    var FluxAu27Aug24 = LocalDate.of(2024, AUGUST, 27);
    var FluxCompteAu25Jul24 = LocalDate.of(2024, JULY, 25);
    var FluxCompteAu31Dec25 = LocalDate.of(2025, DECEMBER, 31);
    var Au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);

    var argentEspece = new Argent("Espèce", au3Juillet24, 800_000);
    var compteBancaire = new Argent("Compte bancaire", au3Juillet24, 100_000);

    var ordinateur = new Materiel("ordinateur", au3Juillet24, 1_200_000, au3Juillet24.minusDays(1), -0.10);
    var vetements = new Materiel("vêtement", au3Juillet24, 1_500_000, au3Juillet24.minusDays(1), -0.50);

    var fraiCompte = new FluxArgent("frais de compte", compteBancaire, FluxCompteAu25Jul24, FluxCompteAu31Dec25, -20_000, 25);

    var dateDette = LocalDate.of(2024, SEPTEMBER, 18);
    var dette = new Dette("dette de scolarité 2024-2025", dateDette, -11_000_000);
    var fraisScolaire = new FluxArgent("frais de scolarité", dette , FluxAu27Nov23, FluxAu27Aug24, -200_000, 27);

    //var financer = new Argent("Compte bancaire", au3Juillet24, 100_000);

    var patrimoineZetyAu3Juillet24 = new Patrimoine(
            "patrimoineZetyAu3Juillet24",
            zety,
            au3Juillet24,
            Set.of(new GroupePossession("Le groupe", Au17Sept2024, Set.of(
                    ordinateur, vetements, argentEspece, compteBancaire))
            )
    );

    var patrimoineZetyAu17Sep24 = patrimoineZetyAu3Juillet24.projectionFuture(Au17Sept2024).getValeurComptable();
    var patrimoineZetyAu18Sep24 = patrimoineZetyAu3Juillet24.projectionFuture(dateDette).getValeurComptable();
    var valeurPatrimoineEntre17Et18 = patrimoineZetyAu17Sep24 - patrimoineZetyAu18Sep24;
    assertEquals(2_384, valeurPatrimoineEntre17Et18);
  }
}