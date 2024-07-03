package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;

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
  void patrimoine_a_de_l_argent_zety(){
    var Zety = new Personne("Zety");

    var au3Juillet2024 = LocalDate.of(2024,JULY,3);

    var patrimoineZetyau3Juillet2024 = new Patrimoine(
            "patrimoine zety au 3 july",
            Zety,
            au3Juillet2024,
            Set.of(
                new Argent("especes",au3Juillet2024,800_000),
                    new Argent("compte bancaire",au3Juillet2024,100_000)
            )
    );
    assertEquals(900_000,patrimoineZetyau3Juillet2024.getValeurComptable());

  }
  @Test
  void patrimoine_possede_un_train_de_vie_financé_par_argent_zety(){
    var Zety = new Personne("Zety");
    var au3Juillet2024 = LocalDate.of(2024,JULY,3);

    var financeur = new Argent("especes",au3Juillet2024,800_000);

    var trainDeVie = new FluxArgent(
            "frais de scolarite",
            financeur,LocalDate.of(2023,NOVEMBER,27),
            LocalDate.of(2024,AUGUST,27),
            -200_000,
            27
    );

   var patrimoineZetyAu17Septembre2024 = new Patrimoine(
           "patrimoine Zety au 3 july",
           Zety,
           au3Juillet2024,
           Set.of(financeur,trainDeVie)
   );

   assertEquals(800000,patrimoineZetyAu17Septembre2024.projectionFuture(au3Juillet2024.plusDays(20)).getValeurComptable());
    assertEquals(400000,patrimoineZetyAu17Septembre2024.projectionFuture(au3Juillet2024.plusDays(200)).getValeurComptable());
    assertEquals(400000,patrimoineZetyAu17Septembre2024.projectionFuture(au3Juillet2024.plusDays(1_220)).getValeurComptable());
  }
  @Test
  void compte_bancaire_evolution(){
    var au3July2024 = LocalDate.of(2024,JULY,3);
    var Zety = new Personne("Zety");
    var compteBancaire = new Argent("compte bancaire",au3July2024,100_000);

    var FraisTenueCompte = new FluxArgent(
            "frais tenue compte",
            compteBancaire,
            LocalDate.of(2024,JULY,25),
            au3July2024.plusDays(54654798),
            -20_000,
            30
    );
    var patrimoineZetyau3Juillet2024 = new Patrimoine(
            "patrimoine zety au 3 july",
            Zety,
            au3July2024,
            Set.of(
                    compteBancaire,FraisTenueCompte
            )
    );

    assertEquals(100000,patrimoineZetyau3Juillet2024.projectionFuture(au3July2024.plusDays(20)).getValeurComptable());
    assertEquals(-240000,patrimoineZetyau3Juillet2024.projectionFuture(au3July2024.plusDays(562)).getValeurComptable());
  }
}