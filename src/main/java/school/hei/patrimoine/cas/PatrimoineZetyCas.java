package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;

import static java.time.Month.*;

public class PatrimoineZetyCas implements Supplier<Patrimoine> {
    @Override
    public Patrimoine get() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY,3);
        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet2024,
                1200000,
                au3juillet2024.minusDays(10),
                -0.1);
        var vetements = new Materiel(
                "Vetements",
                au3juillet2024,
                1500000,
                au3juillet2024.minusDays(1000),
                -0.5);
        var argentEspece = new Argent("Especes",au3juillet2024,800000);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarite",
                argentEspece,
                LocalDate.of(2023,NOVEMBER,1),
                LocalDate.of(2024,AUGUST,31),
                -200000,
                27);
        var argentCompte = new Argent("Espèces", LocalDate.of(2024, JULY, 3), 100000);
        var fraisDeTenueDeCompte = new FluxArgent(
                "Frais de tenue de compte",
                argentEspece,
                LocalDate.of(2024, JULY, 1),
                null ,
                -20000,
                25);
        return new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur,vetements,argentCompte,argentEspece,fraisScolarite,fraisDeTenueDeCompte)
        );
    }

}
