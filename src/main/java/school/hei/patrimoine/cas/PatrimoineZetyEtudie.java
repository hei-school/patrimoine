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

import static java.time.Month.*;
import static java.util.Calendar.JUNE;

public class PatrimoineZetyEtudie implements Supplier<Patrimoine> {

    @Override
    public Patrimoine get() {
        var ilo = new Personne("Zety");
        var au3july24 = LocalDate.of(2024, JULY, 3);
        var argent = new Argent("Espèce", au3july24.minusDays(1), au3july24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au3july24.minusDays(1), au3july24, 100_000);
        var vieEstudiantine = new GroupePossession(
                "Vie estudiantine",
                au3july24,
                Set.of(
                        new FluxArgent(
                                "Frais de scolarité",
                                argent,
                                LocalDate.of(2023, NOVEMBER, 27),
                                LocalDate.of(2024, AUGUST, 27),
                                -200_000,
                                27),
                        new FluxArgent(
                                "Frais de tenue de compte",
                                compteBancaire,
                                LocalDate.of(2024, JULY, 25),
                                LocalDate.of(2024, SEPTEMBER, 17),
                                -20_000,
                                1)
                ));

        var ordinateur = new Materiel(
                "Ordinateur",
                au3july24,
                1_200_000,
                au3july24, -0.10);
        var vetêment = new Materiel(
                "vetêment",
                au3july24,
                1_500_000,
                au3july24,
                -0.50);


        return new Patrimoine(
                "Zety (étudie 2023-2024)",
                ilo,
                au3july24,
                Set.of(argent, compteBancaire , vieEstudiantine, ordinateur, vetêment));
    }
}
