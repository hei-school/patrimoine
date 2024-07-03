package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static java.util.Calendar.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EndettementTest {
    @Test
    void patrimoine_de_zety_apres_endettement() {
        var au17sept24 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18sept24 = LocalDate.of(2024, SEPTEMBER, 18);

        var ordinateur = new Materiel("Ordinateur", au17sept24, 1_200_000, au17sept24.minusYears(1), -0.10);
        var vetements = new Materiel("Vêtements", au17sept24, 1_500_000, au17sept24.minusYears(1), -0.50);
        var argentEspeces = new Argent("Espèces", au17sept24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au17sept24, 100_000);

        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                compteBancaire,
                LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27),
                -200_000,
                27
        );

        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                au17sept24.withDayOfMonth(25),
                LocalDate.of(9999, 12, 31),
                -20_000,
                25
        );

        // Patrimoine au 17 septembre 2024
        var patrimoine17sept24 = new GroupePossession("Patrimoine de Zety au 17/09/2024", au17sept24, Set.of());
        patrimoine17sept24.ajouterPossession(ordinateur);
        patrimoine17sept24.ajouterPossession(vetements);
        patrimoine17sept24.ajouterPossession(argentEspeces);
        patrimoine17sept24.ajouterPossession(compteBancaire);

        var fluxEndettement = new FluxArgent(
                "Endettement auprès de la banque",
                compteBancaire,
                au18sept24,
                au18sept24,
                10_000_000,
                18
        );

        var dette = new Dette("Dette auprès de la banque", au18sept24, -11_000_000,"EUR");


        var patrimoine18sept24 = new GroupePossession("Patrimoine de Zety au 18/09/2024", au18sept24, Set.of());
        patrimoine18sept24.ajouterPossession(ordinateur.projectionFuture(au18sept24));
        patrimoine18sept24.ajouterPossession(vetements.projectionFuture(au18sept24));
        patrimoine18sept24.ajouterPossession(argentEspeces.projectionFuture(au18sept24));
        patrimoine18sept24.ajouterPossession(compteBancaire.projectionFuture(au18sept24));
        patrimoine18sept24.ajouterPossession(dette);

        int valeurPatrimoine17sept24 = patrimoine17sept24.valeurComptableFuture(au17sept24);
        int valeurPatrimoine18sept24 = patrimoine18sept24.valeurComptableFuture(au18sept24);

        int diminutionPatrimoine = valeurPatrimoine17sept24 - valeurPatrimoine18sept24;

        double expectedValue = 11_000_000;

        assertEquals(expectedValue, diminutionPatrimoine);
    }
}
