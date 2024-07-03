package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;
import java.time.LocalDate;
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
}
