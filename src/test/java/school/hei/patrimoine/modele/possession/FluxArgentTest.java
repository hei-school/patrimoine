package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FluxArgentTest {
  @Test
  void train_de_vie_est_finance_par_compte_courant() {
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

    var aLOuvertureDeITM = LocalDate.of(2021, OCTOBER, 26);
    var aLaDiplomation = LocalDate.of(2024, DECEMBER, 26);
    var vieEstudiantine = new FluxArgent(
            "Ma super(?) vie d'etudiant",
            compteCourant, aLOuvertureDeITM, aLaDiplomation, -500_000,
            1);
    var donsDePapaEtMamanAuDebut = new FluxArgent(
            "La générosité des parents au début",
            compteCourant, aLOuvertureDeITM, aLOuvertureDeITM.plusDays(100), 400_000,
            30);
    var donsDePapaEtMamanALaFin = new FluxArgent(
            "La générosité des parents à la fin",
            compteCourant, aLaDiplomation, aLaDiplomation.minusDays(100), 400_000,
            30);

    assertEquals(0, compteCourant.projectionFuture(au13mai24.minusDays(100)).valeurComptable);
    assertEquals(600_000, compteCourant.projectionFuture(au13mai24).valeurComptable);
    var au26juin24 = LocalDate.of(2024, JUNE, 26);
    assertEquals(100_000, compteCourant.projectionFuture(au26juin24).valeurComptable);
    assertEquals(-2_900_000, compteCourant.projectionFuture(aLaDiplomation).valeurComptable);
    assertEquals(-2_900_000, compteCourant.projectionFuture(aLaDiplomation.plusDays(100)).valeurComptable);
  }

  @Test
  void train_de_vie_de_zety_est_finance_en_espece() {
    var au03Juil24 = LocalDate.of(2024, JULY, 3);
    var espece = new  Argent("Argent en espèce", au03Juil24, 800_000);

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

    assertEquals(0, espece.projectionFuture(debutFraisDeScolarite.plusDays(1)).valeurComptable);
    assertEquals(400_000, espece.projectionFuture(finFraisDeScolarite).valeurComptable);
    assertEquals(400_000, espece.projectionFuture(finFraisDeScolarite.plusYears(1)).valeurComptable);
  }

  @Test
  void frais_de_tenue_de_compte_de_Zety() {
    var au03Juil24 = LocalDate.of(2024, JULY, 3);
    var ponctionProche = LocalDate.of(2024, JULY, 25);

    var compteBancaire = new Argent("Compte bancaire",  au03Juil24, 100_000);

    var fraisDeTenueDuCompte = new FluxArgent(
            "Frais de tenue du coimpte",
            compteBancaire,
            ponctionProche,
            LocalDate.MAX,
            -20_000,
            25
    );

    assertEquals(100_000, compteBancaire.projectionFuture(au03Juil24.plusDays(1)).valeurComptable);
    assertEquals(80_000, compteBancaire.projectionFuture(au03Juil24.plusMonths(1)).valeurComptable);
    assertEquals(-140_000, compteBancaire.projectionFuture(au03Juil24.plusYears(1)).valeurComptable);
  }
}