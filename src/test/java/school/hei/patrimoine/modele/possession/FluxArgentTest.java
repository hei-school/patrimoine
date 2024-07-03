package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Monnaie;

import java.time.LocalDate;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FluxArgentTest {
  @Test
  void train_de_vie_est_finance_par_compte_courant() {
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var arriary = new Monnaie("arriary", 4_821, au13mai24, -.1);
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000, arriary);

    var aLOuvertureDeITM = LocalDate.of(2021, OCTOBER, 26);
    var aLaDiplomation = LocalDate.of(2024, DECEMBER, 26);
    var vieEstudiantine = new FluxArgent(
        "Ma super(?) vie d'etudiant",
        compteCourant, aLOuvertureDeITM, aLaDiplomation, -500_000,
        1, arriary);
    var donsDePapaEtMamanAuDebut = new FluxArgent(
        "La générosité des parents au début",
        compteCourant, aLOuvertureDeITM, aLOuvertureDeITM.plusDays(100), 400_000,
        30, arriary);
    var donsDePapaEtMamanALaFin = new FluxArgent(
        "La générosité des parents à la fin",
        compteCourant, aLaDiplomation, aLaDiplomation.minusDays(100), 400_000,
        30, arriary);

    assertEquals(0, compteCourant.projectionFuture(au13mai24.minusDays(100)).valeurComptable);
    assertEquals(600_000, compteCourant.projectionFuture(au13mai24).valeurComptable);
    var au26juin24 = LocalDate.of(2024, JUNE, 26);
    assertEquals(100_000, compteCourant.projectionFuture(au26juin24).valeurComptable);
    assertEquals(-2_900_000, compteCourant.projectionFuture(aLaDiplomation).valeurComptable);
    assertEquals(-2_900_000, compteCourant.projectionFuture(aLaDiplomation.plusDays(100)).valeurComptable);
  }
}