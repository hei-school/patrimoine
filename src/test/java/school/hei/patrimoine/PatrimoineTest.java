package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.TrainDeVie;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        Instant.parse("2024-05-13T00:00:00.00Z"),
        Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var patrimoineIloAu13mai24 = new Patrimoine(
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

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 400_000);

    var trainDeVie = new TrainDeVie(null, 0, null, null, financeur, 0);

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie));
    assertEquals(400_000, patrimoineIloAu13mai24.getValeurComptable());
  }
  void patrimone_dans_le_future() {
    var ilo = new Personne("Ilo");
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");

    var au26Oct21 = Instant.parse("2021-10-26T00:00:00.00Z");
    var mac = new Materiel(
            "MacBook Pro",
            au26Oct21,
            2_000_000,
            -0.10);

    var au01Jan24 = Instant.parse("2024-01-01T00:00:00.00Z");
    var vetements = new Materiel(
            "Effets Vestimentaires",
            au01Jan24,
            1_000_000,
            -0.20);

    var espèces = new Argent("Espèces", au13mai24, 400_000);
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000);
    var compteEpargne = new Argent("Compte epargne", au13mai24, 200_000);

    var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
    var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
    var vieEstudiantine = new TrainDeVie(
            "Ma super(?) vie d'etudiant",
            500_000,
            aLOuvertureDeHEI,
            aLaDiplomation,
            compteCourant,
            1);

    var patrimoineIloAu13mai24 = new Patrimoine(
            ilo,
            au13mai24,
            Set.of(
                    espèces,
                    compteCourant,
                    compteEpargne,
                    mac,
                    vetements,
                    vieEstudiantine));

    var au26Jui24 = Instant.parse("2024-06-26T00:00:00.00Z");
    var patrimoineIlAu26Juin24 = patrimoineIloAu13mai24.projectionFuture(au26Jui24);
    assertTrue(
            patrimoineIloAu13mai24.getValeurComptable() > patrimoineIlAu26Juin24.getValeurComptable());

    var au14Jul24 = Instant.parse("2024-07-14T00:00:00.00Z");
    var patrimoineIloAu14Juil24 = patrimoineIloAu13mai24.projectionFuture(au14Jul24);
    assertTrue(
            patrimoineIlAu26Juin24.getValeurComptable() > patrimoineIloAu14Juil24.getValeurComptable());
  }
}