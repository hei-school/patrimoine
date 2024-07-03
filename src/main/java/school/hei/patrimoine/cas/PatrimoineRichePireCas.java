package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Monnaie;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Set;
import java.util.function.Supplier;

import static java.time.Month.*;
import static java.util.Calendar.JUNE;

public class PatrimoineRichePireCas implements Supplier<Patrimoine> {

  @Override
  public Patrimoine get() {
    var ilo = new Personne("Cresus");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var arriary = new Monnaie("arriary", 4_821, au13mai24, -.1);
    var compteCourant = new Argent("BP", au13mai24.minusDays(1), au13mai24, 13_410, arriary);
    var salaire = new FluxArgent(
        "Salaire",
        compteCourant,
        LocalDate.of(2023, JANUARY, 1),
        LocalDate.of(2026, DECEMBER, 31),
        4_800,
        3, arriary);
    var trainDeVie = new GroupePossession(
        "Train de vie",
        au13mai24,
        Set.of(
            new FluxArgent(
                "Loyer",
                compteCourant,
                LocalDate.of(2023, JANUARY, 1),
                LocalDate.of(2026, DECEMBER, 31),
                -1_450,
                27, arriary),
            new FluxArgent(
                "Courses",
                compteCourant,
                LocalDate.of(2023, JANUARY, 1),
                LocalDate.of(2026, DECEMBER, 31),
                -1_100,
                1, arriary)
        ), arriary);

    var voiture = new AchatMaterielAuComptant(
        "Voiture",
        LocalDate.of(2025, JUNE, 4),
        22_450,
        -0.4,
        compteCourant, arriary);
    var mac = new Materiel(
        "MacBook Pro",
        au13mai24,
        2_000,
        au13mai24,
        -0.9, arriary);

    var compteEpargne = new Argent("CE", LocalDate.of(2025, Calendar.SEPTEMBER, 7), 0, arriary);
    var transfertVersEpargne = new TransfertArgent(
        "Salaire",
        compteCourant,
        compteEpargne,
        LocalDate.of(2025, DECEMBER, 1),
        LocalDate.of(2026, Calendar.JULY, 27),
        3_200,
        3, arriary);

    return new Patrimoine(
        "Cresus (pire)",
        ilo,
        au13mai24,
        Set.of(compteCourant, compteEpargne, trainDeVie, voiture, mac));
  }
}
