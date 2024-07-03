package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.util.Calendar.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineEnEuroTest {
    @Test
    void testPatrimoineEnEuroLe26Octobre2025() {


        var au3juillet24 = LocalDate.of(2024, 7, 3);
        var au26octobre25 = LocalDate.of(2025, OCTOBER, 26);


        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
        var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);


        var fraisScolarite = new FluxArgent(
                "Frais de scolarité", argentEspeces, LocalDate.of(2023, 11, 27),
                LocalDate.of(2024, 8, 27), -200_000, 27);


        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte", compteBancaire, au3juillet24,
                LocalDate.of(9999, 12, 31), -20_000, 25);


        var patrimoine = new Patrimoine("Patrimoine de Zety", new Personne("Zety"), au3juillet24,
                Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisTenueCompte));

        // Calcul de la valeur du patrimoine en euros au 26 octobre 2025
        double tauxChangeInitial = 4.821;  // 1 € = 4.821 Ar à la date du 3 juillet 2024
        double appreciationAnnuelleAriary = -0.10 / 365;  // Appréciation annuelle lissée sur chaque jour de l'année
        LocalDate debutCalcul = au3juillet24.plusDays(1); // Début du calcul le lendemain de la date de valeur initiale

        double tauxChangeAu26Octobre2025 = tauxChangeInitial * Math.pow(1 + appreciationAnnuelleAriary, au26octobre25.toEpochDay() - debutCalcul.toEpochDay());

        double valeurTotalePatrimoineEnEuros = patrimoine.getValeurComptable() / tauxChangeAu26Octobre2025;


        double valeurAttendueEnEuros = (1_200_000 * Math.pow(1 - 0.0002738, 845)) +
                (1_500_000 * Math.pow(1 - 0.001898, 845)) +
                (800_000 - 2_000_000) / tauxChangeAu26Octobre2025 +
                (100_000 - 60_000) / tauxChangeAu26Octobre2025;

        assertEquals(valeurAttendueEnEuros, valeurTotalePatrimoineEnEuros, 0.01, "La valeur du patrimoine en euros le 26 octobre 2025 ne correspond pas au résultat attendu.");
    }
}
