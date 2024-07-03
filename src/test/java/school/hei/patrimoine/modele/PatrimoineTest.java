package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.MAY;
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
  void patrimoine_de_Zety () {

    // Zety étudie en 2023-2024

    Personne Zety = new Personne("Zety");

    LocalDate au3juillet2024 = LocalDate.of(2024, 7, 3);
    LocalDate au17septembre2024 = LocalDate.of(2024, 9, 3);


    Materiel ordinateur = new Materiel(
            "Ordinateur",
            au3juillet2024,
            1_200_000,
            au3juillet2024,
            0.10);

    Materiel vetements = new Materiel(
            "Vetements",
            au3juillet2024,
            1_500_000,
            au3juillet2024,
            0.50);

    Argent argentEspece = new Argent(
            "Argent Espece",
            au3juillet2024,
            800_000);

    Argent compteBancaire = new Argent(
            "compte bancaire"
            , au3juillet2024,
            100_000);

    FluxArgent fraisDeScolarite = new FluxArgent(
            "frais de scolarite"
            , argentEspece,
            LocalDate.of(2023, 11, 27),
            LocalDate.of(2024, 8,27),
            -200_000,
            27);

    FluxArgent fraisDeRetenueCompteBancaire = new FluxArgent(
            "frais de retenue compte bancaire",
            compteBancaire,
            au3juillet2024,
            LocalDate.of(2024, 8, 27),
            -20_000,
            25);

    Patrimoine patrimoineZetyAu3juillet2024 = new Patrimoine(
            "patrimoine Zety au 3 juillet 2024",
            Zety,
            au3juillet2024,
            Set.of(ordinateur, vetements, argentEspece, compteBancaire, fraisDeRetenueCompteBancaire, fraisDeScolarite)
    );

    Patrimoine patrimoineZetyAu17septembre2024 = patrimoineZetyAu3juillet2024.projectionFuture(au17septembre2024);

    assertEquals(2_978_000 , patrimoineZetyAu17septembre2024.getValeurComptable());





  }
}