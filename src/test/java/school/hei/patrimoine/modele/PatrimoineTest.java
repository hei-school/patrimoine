package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.MAY;
import static java.util.Calendar.*;
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
  void patrimoine_zety_le_17_septembre_2024() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

    var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
    var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
    var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);

    var fraisDeScolarite = new FluxArgent(
            "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
            LocalDate.of(2024, AUGUST, 27), -200_000, 27);

    var compteBancaire = new Argent("Compte Bancaire", au3juillet24, 100_000);
    var fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25),
            LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

    var patrimoineDeZetyAu3juillet24 = new Patrimoine(
            "patrimoineDeZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argentEspeces, fraisDeScolarite, compteBancaire, fraisTenueCompte));

    assertEquals(2978848,patrimoineDeZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable());
  }

  @Test
  void patrimoine_de_zety_entre_17_18_septembre_2024() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
    var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

    var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
    var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
    var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);

    var fraisScolarite = new FluxArgent(
            "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
            LocalDate.of(2024, AUGUST, 27), -200_000, 27);

    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
    var fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25),
            LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

    var patrimoineDeZetyAu17septembre24 = new Patrimoine(
            "patrimoineDeZetyAu17septembre24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));

    var fluxArgentPret = new FluxArgent(
            "Prêt bancaire", compteBancaire, au18septembre24, au18septembre24.plusYears(1), 10_000_000, 18);
    var dette = new Dette("Dette bancaire", au18septembre24, -11_000_000);

    var patrimoineDeZetyAu18septembre24 = new Patrimoine(
            "patrimoineDeZetyAu18septembre24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte, fluxArgentPret, dette));

    int valeurPatrimoine17septembre = patrimoineDeZetyAu17septembre24.projectionFuture(au17septembre24).getValeurComptable();
    int valeurPatrimoine18septembre = patrimoineDeZetyAu18septembre24.projectionFuture(au18septembre24).getValeurComptable();
    int diminutionValeur = valeurPatrimoine18septembre - valeurPatrimoine17septembre;

    assertEquals(-1002384, diminutionValeur);
  }

  @Test
  void epuisement_de_l_espece_zety() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);
    var au1octobre24 = LocalDate.of(2024, OCTOBER, 1);
    var au13fevrier25 = LocalDate.of(2025, FEBRUARY, 13);

    var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
    var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
    var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);

    var fraisScolarite = new FluxArgent(
            "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
            LocalDate.of(2024, AUGUST, 27), -200_000, 27);

    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
    var fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25),
            LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

    var donParents = new FluxArgent(
            "Don des parents", argentEspeces, LocalDate.of(2024, 1, 15),
            LocalDate.of(2024, 12, 15), 100_000, 15);

    var trainDeVie = new FluxArgent(
            "Train de vie", argentEspeces, au1octobre24,
            au13fevrier25, -250_000, 1);

    var paiementScolarite = new FluxArgent(
            "Paiement scolarité", compteBancaire, au21septembre24, au21septembre24, -2_500_000, 21);

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte, donParents, trainDeVie, paiementScolarite));

    LocalDate dateEpuisementEspeces = au3juillet24;
    while (patrimoineZetyAu3juillet24.projectionFuture(dateEpuisementEspeces).getValeurComptable() >= 0) {
      dateEpuisementEspeces = dateEpuisementEspeces.plusDays(1);
    }

    assertEquals(LocalDate.of(2024, 12, 1), dateEpuisementEspeces);
  }
  @Test
  void valeur_patrimoine_zety_le_14_fevrier_2025() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au14fevrier25 = LocalDate.of(2025, FEBRUARY, 14);

    var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
    var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
    var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);

    var fraisScolarite = new FluxArgent(
            "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
            LocalDate.of(2024, AUGUST, 27), -200_000, 27);

    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
    var fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25),
            LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

    var donParents = new FluxArgent(
            "Don des parents", argentEspeces, LocalDate.of(2024, 1, 15),
            LocalDate.of(2024, DECEMBER, 15), 100_000, 15);

    var trainDeVie = new FluxArgent(
            "Train de vie", argentEspeces, LocalDate.of(2024, OCTOBER, 1),
            LocalDate.of(2025, FEBRUARY, 13), -250_000, 1);

    var paiementScolarite = new FluxArgent(
            "Paiement scolarité", compteBancaire, LocalDate.of(2024, SEPTEMBER, 21), LocalDate.of(2024, SEPTEMBER, 21), -2_500_000, 21);

    var patrimoineDeZetyAu3juillet24 = new Patrimoine(
            "patrimoineDeZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte, donParents, trainDeVie, paiementScolarite));

    assertEquals(-608686, patrimoineDeZetyAu3juillet24.projectionFuture(au14fevrier25).getValeurComptable());
  }
  @Test
  void Etudier_En_Allemange() {
    var patrimoineZetyAu3Juillet2024 = patrimoineDeZety();
    var au21septembre2024 = LocalDate.of(2024, 9, 21);
    var au1Janvier2024 = LocalDate.of(2024, 1, 1);
    var au1ctobre2024 = LocalDate.of(2024, 1, 1);
    var au13Fevrier2025 = LocalDate.of(2025, 2, 13);

    var fraisScolariteEnUnMois = new FluxArgent("fraisScolariteEnMois", (Argent) patrimoineZetyAu3Juillet2024.possessionParNom("zetyCompteBancaire"), au21septembre2024, au21septembre2024.plusDays(1), 2_500_000, 21);
    var argentDepuisParent = new FluxArgent(
            "argentDepuisParent",
            (Argent) patrimoineZetyAu3Juillet2024.possessionParNom("argentEspeces"),
            au1Janvier2024,
            LocalDate.MAX,
            100_000,
            15
    );
    var trainDeVie = new FluxArgent(
            "trainDeVie",
            (Argent) patrimoineZetyAu3Juillet2024.possessionParNom("argentEspeces"),
            au1ctobre2024,
            au13Fevrier2025,
            250_000,
            1
    );
  }


  public Patrimoine patrimoineDeZety() {
    var zety = new Personne("Zety");
    var au3juillet2024 = LocalDate.of(2024, 6, 3);
    var debutFraisScolarite = LocalDate.of(2023, 11, 1);
    var finFraisScolarite = LocalDate.of(2024, 8, 31);

    var ordinateur = new Materiel("ordinateur", au3juillet2024, 1_200_000, au3juillet2024, -0.10);
    var vetements = new Materiel("vetements", au3juillet2024, 1_500_000, au3juillet2024, -0.50);
    var argentEspeces = new Argent("argentEspeces", au3juillet2024, 800_000);
    var fraisDeScolariteParMois = new FluxArgent(
            "zetyFluxDArgentSurScolarite",
            argentEspeces,
            debutFraisScolarite,
            finFraisScolarite,
            -200_000,
            27
    );

    var compteBancaire = new Argent("zetyCompteBancaire", au3juillet2024, 100_000);
    var tenueDeCompteBancaire = new FluxArgent("zetyFluxDArgentBancaire", compteBancaire, au3juillet2024, LocalDate.MAX, -20_000, 25);

    return new Patrimoine(
            "patrimoineZetyAu3juillet2024",
            zety,
            au3juillet2024,
            new HashSet<>(Set.of(
                    ordinateur,
                    vetements,
                    argentEspeces,
                    compteBancaire
            ))
    );
  }
}
