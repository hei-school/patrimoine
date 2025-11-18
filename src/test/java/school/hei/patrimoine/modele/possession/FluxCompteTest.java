package school.hei.patrimoine.modele.possession;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class FluxCompteTest {
  @Test
  void train_de_vie_est_finance_par_compte_courant() {
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var compteCourant = new Compte("Compte courant", au13mai24, ariary(600_000));

    var aLOuvertureDeITM = LocalDate.of(2021, OCTOBER, 26);
    var aLaDiplomation = LocalDate.of(2024, DECEMBER, 26);
    new FluxArgent(
        "Ma super(?) vie d'etudiant",
        compteCourant,
        aLOuvertureDeITM,
        aLaDiplomation,
        1,
        ariary(-500_000));
    new FluxArgent(
        "La générosité des parents au début",
        compteCourant,
        aLOuvertureDeITM,
        aLOuvertureDeITM.plusDays(100),
        30,
        ariary(400_000));
    new FluxArgent(
        "La générosité des parents à la fin",
        compteCourant,
        aLaDiplomation,
        aLaDiplomation.minusDays(100),
        30,
        ariary(400_000));

    assertEquals(
        ariary(0), compteCourant.projectionFuture(au13mai24.minusDays(100)).valeurComptable);
    assertEquals(ariary(600_000), compteCourant.projectionFuture(au13mai24).valeurComptable);
    var au26juin24 = LocalDate.of(2024, JUNE, 26);
    assertEquals(ariary(100_000), compteCourant.projectionFuture(au26juin24).valeurComptable);
    assertEquals(
        ariary(-2_900_000), compteCourant.projectionFuture(aLaDiplomation).valeurComptable);
    assertEquals(
        ariary(-2_900_000),
        compteCourant.projectionFuture(aLaDiplomation.plusDays(100)).valeurComptable);
  }

  @Test
  void dateOperation_superieur_a_lastDayOfMonth_est_corrigee() {
    var t0 = LocalDate.of(2025, JANUARY, 15);
    var compte = new Compte("comptePersonne", t0, ariary(0));

    var debut = LocalDate.of(2025, JANUARY, 1);
    var fin = LocalDate.of(2025, APRIL, 30);

    new FluxArgent(
            "Flux mensuel comptePersonnel",
            compte,
            debut,
            fin,
            31,
            ariary(-100_000)
    );

    var actual = compte.projectionFuture(LocalDate.of(2025, APRIL, 30));
    assertEquals(ariary(-400_000), actual.valeurComptable);
  }

}
