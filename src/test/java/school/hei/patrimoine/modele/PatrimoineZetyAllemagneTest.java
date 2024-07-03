package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.*;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.TransfertArgent;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineZetyAllemagneTest {

    @Test
    void valeur_patrimoine_zety_le_14_fevrier_2025() {
        var zety = new Personne("Zety");

        var au3juillet24 = LocalDate.of(2024, Month.JULY, 3);
        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.0996);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.4988);
        var espece = new Argent("Espèces", au3juillet24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        var fraisTenueCompte = new FluxArgent(
                "Frais tenue de compte",
                compteBancaire, au3juillet24, LocalDate.of(2024, Month.SEPTEMBER, 25), -20_000, 25);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                espece, LocalDate.of(2023, Month.NOVEMBER, 27), LocalDate.of(2024, Month.AUGUST, 27), -200_000, 30);

        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, espece, compteBancaire, fraisTenueCompte, fraisScolarite));
        LocalDate currentDate = au3juillet24.plusMonths(1);

        while (currentDate.isBefore(LocalDate.of(2025, Month.FEBRUARY, 14))) {
            if (currentDate.isEqual(LocalDate.of(2024, Month.OCTOBER, 1)) || currentDate.isAfter(LocalDate.of(2024, Month.OCTOBER, 1))) {
                var trainDeVie = new FluxArgent(
                        "Train de vie mensuel",
                        espece, currentDate, LocalDate.of(2025, Month.FEBRUARY, 13), -250_000, 1);

            }
            if (currentDate.getDayOfMonth() == 15) {
                var donMensuel = new TransfertArgent(
                        "Don mensuel des parents",
                        espece, espece, currentDate, LocalDate.of(2025, Month.FEBRUARY, 13), 100_000, 15);
            }
            currentDate = currentDate.plusMonths(1);
        }
        var valeurComptable = patrimoineZety.projectionFuture(LocalDate.of(2025, Month.FEBRUARY, 14)).getValeurComptable();
        assertEquals(1_802_725, valeurComptable);
    }
}

