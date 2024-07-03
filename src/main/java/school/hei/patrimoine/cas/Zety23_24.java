package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.function.Supplier;

public class Zety23_24 implements Supplier<Patrimoine> {
    @Override
    public Patrimoine get() {
        var zety = new Personne("Zety");

        var au27Novembre23 = LocalDate.of(2023, Month.NOVEMBER, 27);
        var au3Juillet24 = LocalDate.of(2024, Month.JULY, 3);
        var au27Août24 = LocalDate.of(2024, Month.AUGUST, 27);
        var au26Octobre25 = LocalDate.of(2025, Month.OCTOBER, 26);

        // Matériels
        var ordinateur = new Materiel(
                "Ordinateur",
                au3Juillet24,
                1_200_000,
                au3Juillet24,
                -0.1);
        var vêtements = new Materiel(
                "Vêtements",
                au3Juillet24,
                1_500_000,
                au3Juillet24,
                -0.5);

        // Argents
        var espèces = new Argent("Espèces", au3Juillet24, 800_000);
        var compteBanquaire = new Argent("Compte Banquaire", au3Juillet24, 100_000);

        // Flux Argent
        var fraisDeScolaritéMensuel = new FluxArgent(
                "Frais de scolarité",
                espèces,
                au27Novembre23,
                au27Août24,
                -200_000,
                27);
        var fraisDeTenue = new FluxArgent(
                "Frais de tenue",
                compteBanquaire,
                au3Juillet24,
                au26Octobre25,
                -20_000,
                25);

        return new Patrimoine(
                "Patrimoine de Zety 2023 2024",
                zety,
                au3Juillet24,
                Set.of(
                        ordinateur,
                        vêtements,
                        espèces,
                        fraisDeScolaritéMensuel,
                        compteBanquaire,
                        fraisDeTenue
                ));
    }
}