package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  void zety_etudie_2023_2024(){
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);

    var ordinateur = new Materiel(
            "Ordinateur",
            au3juillet24,
            1_200_000,
            au3juillet24,
            -0.10);

    var vetements = new Materiel(
            "Vêtements",
            au3juillet24,
            1_500_000,
            au3juillet24,
            -0.50);

    var espece = new Argent("espèce", au3juillet24, 800_000);

    var debutScolarite = LocalDate.of(2023, NOVEMBER, 1);
    var finScolarite = LocalDate.of(2024, AUGUST, 30);
    var fraisDeScolarite = new FluxArgent(
            "Frais de Scolarité",
            espece,
            debutScolarite,
            finScolarite,
            -200_000,
            27);

    var compteBancaire = new Argent("CompteBancaire", au3juillet24, 100_000);
    var fraisDuCompte = new FluxArgent(
            "Frais de compte",
            compteBancaire,
            au3juillet24,
            LocalDate.MAX,
            -20_000,
            25);

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur,
                    vetements,
                    espece,
                    fraisDeScolarite,
                    compteBancaire,
                    fraisDuCompte
            )
    );

    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
    assertTrue(patrimoineZetyAu3juillet24.getValeurComptable() > patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable());
    assertEquals(2_978_848, patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable());
  }

  @Test
  void zety_endette(){
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);

    var ordinateur = new Materiel(
            "Ordinateur",
            au3juillet24,
            1_200_000,
            au3juillet24,
            -0.10);

    var vetements = new Materiel(
            "Vêtements",
            au3juillet24,
            1_500_000,
            au3juillet24,
            -0.50);

    var espece = new Argent("espèce", au3juillet24, 800_000);

    var debutScolarite = LocalDate.of(2023, NOVEMBER, 1);
    var finScolarite = LocalDate.of(2024, AUGUST, 30);
    var fraisDeScolarite = new FluxArgent(
            "Frais de Scolarité",
            espece,
            debutScolarite,
            finScolarite,
            -200_000,
            27);


    var compteBancaire = new Argent("CompteBancaire", au3juillet24, 100_000);
    var fraisDuCompte = new FluxArgent(
            "Frais de compte",
            compteBancaire,
            au3juillet24,
            LocalDate.MAX,
            -20_000,
            25);

    var dateEmprunt = LocalDate.of(2024, SEPTEMBER, 18);
    var dateRemb = dateEmprunt.plusYears(1);
    var dette = new Dette("Dette Scolarité", au3juillet24,0);

    var pret = new FluxArgent("Frais De Scolarité Prêt", compteBancaire, dateEmprunt, dateEmprunt, 10_000_000, dateEmprunt.getDayOfMonth());
    var detteAjout = new FluxArgent("Frais De Scolarité Dette", dette, dateEmprunt, dateEmprunt, -11_000_000, dateEmprunt.getDayOfMonth());
    var remboursement = new FluxArgent("Frais De Scolarité Rem", compteBancaire, dateRemb, dateRemb, -11_000_000, dateRemb.getDayOfMonth());
    var detteAnnulation = new FluxArgent("Frais De Scolarité annulation", dette, dateRemb, dateRemb, 11_000_000, dateRemb.getDayOfMonth());

    var detteCompteBancaire = new GroupePossession(
            "Compte Bancaire",
            au3juillet24,
            Set.of(pret, detteAjout, remboursement, detteAnnulation)
    );

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur,
                    vetements,
                    espece,
                    fraisDeScolarite,
                    compteBancaire,
                    fraisDuCompte,
                    dette,
                    detteCompteBancaire
            )
    );

    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
    var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);
    assertTrue(patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable() > patrimoineZetyAu3juillet24.projectionFuture(au18septembre24).getValeurComptable());
    assertEquals(1_002_384, patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable() - patrimoineZetyAu3juillet24.projectionFuture(au18septembre24).getValeurComptable());
  }

  @Test
  void zety_etudie_2024_2025(){
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);

    var ordinateur = new Materiel(
            "Ordinateur",
            au3juillet24,
            1_200_000,
            au3juillet24,
            -0.10);

    var vetements = new Materiel(
            "Vêtements",
            au3juillet24,
            1_500_000,
            au3juillet24,
            -0.50);

    var espece = new Argent("espèce", au3juillet24, 800_000);

    var debutScolarite = LocalDate.of(2023, NOVEMBER, 1);
    var finScolarite = LocalDate.of(2024, AUGUST, 30);
    var fraisDeScolarite2324 = new FluxArgent(
            "Frais de Scolarité 2023-2024",
            espece,
            debutScolarite,
            finScolarite,
            -200_000,
            27);


    var compteBancaire = new Argent("CompteBancaire", au3juillet24, 100_000);
    var fraisDuCompte = new FluxArgent(
            "Frais de compte",
            compteBancaire,
            au3juillet24,
            LocalDate.MAX,
            -20_000,
            25);

    var dateEmprunt = LocalDate.of(2024, SEPTEMBER, 18);
    var dateRemb = dateEmprunt.plusYears(1);
    var dette = new Dette("Dette Scolarité", au3juillet24,0);

    var pret = new FluxArgent("Frais De Scolarité Prêt", compteBancaire, dateEmprunt, dateEmprunt, 10_000_000, dateEmprunt.getDayOfMonth());
    var detteAjout = new FluxArgent("Frais De Scolarité Dette", dette, dateEmprunt, dateEmprunt, -11_000_000, dateEmprunt.getDayOfMonth());
    var remboursement = new FluxArgent("Frais De Scolarité Rem", compteBancaire, dateRemb, dateRemb, -11_000_000, dateRemb.getDayOfMonth());
    var detteAnnulation = new FluxArgent("Frais De Scolarité annulation", dette, dateRemb, dateRemb, 11_000_000, dateRemb.getDayOfMonth());

    var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);
    var fraisDeScolarite2425 = new FluxArgent(
            "Frais de Scolarité 2024-2025",
            compteBancaire,
            au21septembre24,
            au21septembre24,
            -2_500_000,
            au21septembre24.getDayOfMonth()
    );

    var transfertParent = new FluxArgent(
            "Transfert Parent",
            espece,
            LocalDate.of(2024, JANUARY, 1),
            LocalDate.MAX,
            100_000,
            15
    );

    var trainDeVie = new FluxArgent(
            "Train de vie",
            espece,
            LocalDate.of(2024, OCTOBER, 1),
            LocalDate.of(2025, FEBRUARY, 13),
            -250_000,
            1
    );

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur,
                    vetements,
                    espece,
                    compteBancaire,
                    fraisDuCompte,
                    dette,
                    new GroupePossession("Dette sur le compte bancaire", au3juillet24, Set.of(pret, detteAjout, remboursement, detteAnnulation)),
                    new GroupePossession("Espece Flux", au3juillet24, Set.of(fraisDeScolarite2324,fraisDeScolarite2425, transfertParent, trainDeVie))
            )
    );

    var especeValeurComptable = patrimoineZetyAu3juillet24.possessionParNom("espèce").getValeurComptable();
    var nombreDeJour = 0;
    for (int i = 1; especeValeurComptable > 0 ; i++) {
      especeValeurComptable = patrimoineZetyAu3juillet24.possessionParNom("espèce").projectionFuture(au3juillet24.plusDays(i)).getValeurComptable();
      nombreDeJour = i;
    }

    assertTrue(patrimoineZetyAu3juillet24.possessionParNom("espèce").projectionFuture(au3juillet24.plusDays(nombreDeJour - 1)).getValeurComptable() > 0);
    assertTrue(patrimoineZetyAu3juillet24.possessionParNom("espèce").projectionFuture(au3juillet24.plusDays(nombreDeJour)).getValeurComptable() <= 0);
    assertEquals(LocalDate.of(2025, JANUARY, 1), au3juillet24.plusDays(nombreDeJour));
  }

  @Test
  void patrimoine_zety_en_Allemagne_14fevrier25(){
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);

    var ordinateur = new Materiel(
            "Ordinateur",
            au3juillet24,
            1_200_000,
            au3juillet24,
            -0.10);

    var vetements = new Materiel(
            "Vêtements",
            au3juillet24,
            1_500_000,
            au3juillet24,
            -0.50);

    var espece = new Argent("espèce", au3juillet24, 800_000);

    var debutScolarite = LocalDate.of(2023, NOVEMBER, 1);
    var finScolarite = LocalDate.of(2024, AUGUST, 30);
    var fraisDeScolarite2324 = new FluxArgent(
            "Frais de Scolarité 2023-2024",
            espece,
            debutScolarite,
            finScolarite,
            -200_000,
            27);


    var compteBancaire = new Argent("CompteBancaire", au3juillet24, 100_000);
    var fraisDuCompte = new FluxArgent(
            "Frais de compte",
            compteBancaire,
            au3juillet24,
            LocalDate.MAX,
            -20_000,
            25);

    var dateEmprunt = LocalDate.of(2024, SEPTEMBER, 18);
    var dateRemb = dateEmprunt.plusYears(1);
    var dette = new Dette("Dette Scolarité", au3juillet24,0);

    var pret = new FluxArgent("Frais De Scolarité Prêt", compteBancaire, dateEmprunt, dateEmprunt, 10_000_000, dateEmprunt.getDayOfMonth());
    var detteAjout = new FluxArgent("Frais De Scolarité Dette", dette, dateEmprunt, dateEmprunt, -11_000_000, dateEmprunt.getDayOfMonth());
    var remboursement = new FluxArgent("Frais De Scolarité Rem", compteBancaire, dateRemb, dateRemb, -11_000_000, dateRemb.getDayOfMonth());
    var detteAnnulation = new FluxArgent("Frais De Scolarité annulation", dette, dateRemb, dateRemb, 11_000_000, dateRemb.getDayOfMonth());

    var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);
    var fraisDeScolarite2425 = new FluxArgent(
            "Frais de Scolarité 2024-2025",
            compteBancaire,
            au21septembre24,
            au21septembre24,
            -2_500_000,
            au21septembre24.getDayOfMonth()
    );

    var transfertParent = new FluxArgent(
            "Transfert Parent",
            espece,
            LocalDate.of(2024, JANUARY, 1),
            LocalDate.MAX,
            100_000,
            15
    );

    var trainDeVie = new FluxArgent(
            "Train de vie",
            espece,
            LocalDate.of(2024, OCTOBER, 1),
            LocalDate.of(2025, FEBRUARY, 13),
            -250_000,
            1
    );

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur,
                    vetements,
                    espece,
                    compteBancaire,
                    fraisDuCompte,
                    dette,
                    new GroupePossession("Dette sur le compte bancaire", au3juillet24, Set.of(pret, detteAjout, remboursement, detteAnnulation)),
                    new GroupePossession("Espece Flux", au3juillet24, Set.of(fraisDeScolarite2324,fraisDeScolarite2425, transfertParent, trainDeVie))
            )
    );

    var au14fevrier25 = LocalDate.of(2025, FEBRUARY, 14);
    assertEquals(-1_528_686,patrimoineZetyAu3juillet24.projectionFuture(au14fevrier25).getValeurComptable());
  }
}