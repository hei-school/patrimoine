package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.TransfertArgent;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Set;
import java.util.function.Supplier;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static java.time.Month.MAY;
import static java.util.Calendar.JUNE;
import static school.hei.patrimoine.modele.Devise.ARIARY;

public class PatrimoineRichePireCas implements Supplier<Patrimoine> {

  @Override
  public Patrimoine get() {
    var ilo = new Personne("Cresus");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var compteCourant = new Argent("BP", au13mai24.minusDays(1), au13mai24, 13_410, ARIARY);
    var salaire = new FluxArgent(
        "Salaire",
        compteCourant,
        LocalDate.of(2023, JANUARY, 1),
        LocalDate.of(2026, DECEMBER, 31),
        4_800,
        3, ARIARY);
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
                27, ARIARY),
            new FluxArgent(
                "Courses",
                compteCourant,
                LocalDate.of(2023, JANUARY, 1),
                LocalDate.of(2026, DECEMBER, 31),
                -1_100,
                1, ARIARY)
        ), ARIARY);

    var voiture = new AchatMaterielAuComptant(
        "Voiture",
        LocalDate.of(2025, JUNE, 4),
        22_450,
        -0.4,
        compteCourant, ARIARY);
    var mac = new Materiel(
        "MacBook Pro",
        au13mai24,
        2_000,
        au13mai24,
        -0.9, ARIARY);

    var compteEpargne = new Argent("CE", LocalDate.of(2025, Calendar.SEPTEMBER, 7), 0, ARIARY);
    var transfertVersEpargne = new TransfertArgent(
        "Salaire",
        compteCourant,
        compteEpargne,
        LocalDate.of(2025, DECEMBER, 1),
        LocalDate.of(2026, Calendar.JULY, 27),
        3_200,
        3, ARIARY);

    return new Patrimoine(
        "Cresus (pire)",
        ilo,
        au13mai24,
        Set.of(compteCourant, compteEpargne, trainDeVie, voiture, mac));
  }
}
