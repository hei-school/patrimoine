package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import static java.time.LocalDate.now;
import static java.time.Month.AUGUST;
import static java.time.Month.JULY;
import static java.time.Month.NOVEMBER;
import static java.util.stream.Collectors.toSet;

public class PatrimoineZetyCas implements Supplier<Patrimoine> {

    private final LocalDate ajd = now();
    private final LocalDate aPayeTous = LocalDate.of(2023, NOVEMBER, 27);
    private final LocalDate finEtudes = LocalDate.of(2024, AUGUST, 31);

    private Set<Possession> possessionsZety(Argent especes, Argent compteBancaire) {
        var ordinateur = new Materiel("Ordinateur", ajd, 1_200_000, ajd, -0.10);
        var vetements = new Materiel("Vêtements", ajd, 1_500_000, ajd, -0.50);
        new FluxArgent("Frais de scolarité", especes,aPayeTous, finEtudes, -200_000, 27);
        new FluxArgent("Frais de tenue de compte", compteBancaire, ajd, LocalDate.of(2030, JULY, 25), -20_000, 25);

        return Set.of(ordinateur, vetements, especes, compteBancaire);
    }

    @Override
    public Patrimoine get() {
        var zety = new Personne("Zety");
        var especes = new Argent("Espèces", ajd, 800_000);
        var compteBancaire = new Argent("Compte bancaire", ajd, 100_000);

        Set<Possession> possessionsZety = possessionsZety(especes, compteBancaire);

        return new Patrimoine(
                "Zety (2023-2024)",
                zety,
                ajd,
                possessionsZety.stream().collect(toSet()));
    }

    public static Patrimoine patrimoineZetyAvecEndettement() {
        var zety = new Personne("Zety");
        var au3Juillet24 = LocalDate.of(2024, 7, 3);
        Set<Possession> possessions = new HashSet<>();
        Materiel ordinateur = new Materiel("Ordinateur", au3Juillet24, 1_200_000, au3Juillet24, -0.10);
        possessions.add(ordinateur);

        Materiel vetements = new Materiel("Vêtements", au3Juillet24, 1_500_000, au3Juillet24, -0.50);
        possessions.add(vetements);
        possessions.add(new Argent("Espèces", au3Juillet24, 800_000));
        possessions.add(new Argent("Compte bancaire", au3Juillet24, 100_000));

        LocalDate au17Sept24 = LocalDate.of(2024, 9, 17);
        var patrimoineZetyAu17Sept24 = new Patrimoine(
                "patrimoineZetyAu17Sept24",
                zety,
                au17Sept24,
                possessions
        );

        LocalDate au18Sept24 = LocalDate.of(2024, 9, 18);
        Dette endettementZety = new Dette(
                "Endettement",
                au18Sept24,
                -11_000_000
        );
        possessions.add(endettementZety);

        patrimoineZetyAu17Sept24 = new Patrimoine(
                patrimoineZetyAu17Sept24.nom(),
                patrimoineZetyAu17Sept24.possesseur(),
                au18Sept24,
                possessions
        );

        return patrimoineZetyAu17Sept24;
    }

    public static Patrimoine patrimoineZetyAvecEtudes2024_2025() {
        var zety = new Personne("Zety");
        var au21Sept24 = LocalDate.of(2024, 9, 21);
        Set<Possession> possessions = new HashSet<>();
        Materiel ordinateur = new Materiel("Ordinateur", au21Sept24, 1_200_000, au21Sept24, -0.10);
        possessions.add(ordinateur);

        Materiel vetements = new Materiel("Vêtements", au21Sept24, 1_500_000, au21Sept24, -0.50);
        possessions.add(vetements);
        var especes = new Argent("Espèces", au21Sept24, 800_000);
        possessions.add(especes);
        var compteBancaire = new Argent("Compte bancaire", au21Sept24, 100_000);
        possessions.add(compteBancaire);

        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                compteBancaire,
                au21Sept24,
                au21Sept24,
                -2_500_000,
                21);
        possessions.add(fraisScolarite);

        FluxArgent donMensuelParents = new FluxArgent(
                "Don mensuel des parents",
                especes,
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2025, 1, 15),
                100_000,
                15);
        possessions.add(donMensuelParents);

        FluxArgent trainDeVieMensuel = new FluxArgent(
                "Train de vie mensuel",
                especes,
                LocalDate.of(2024, 10, 1),
                LocalDate.of(2025, 2, 13),
                -250_000,
                1);
        possessions.add(trainDeVieMensuel);

        var patrimoineZetyAu21Sept24 = new Patrimoine(
                "patrimoineZetyAvecEtudes2024_2025",
                zety,
                au21Sept24,
                possessions
        );

        return patrimoineZetyAu21Sept24;
    }
}
