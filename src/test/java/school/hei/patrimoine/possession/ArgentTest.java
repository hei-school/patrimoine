package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgentTest {
    @Test
    void argent_avec_plusieurs_trains_de_vie_dans_le_futur() {
        var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
        var compteCourant = new Argent("Compte courant", au13mai24, 1_000_000);

        var debutHEI = Instant.parse("2021-10-26T00:00:00.00Z");
        var finTrainDeVie1 = Instant.parse("2024-06-26T00:00:00.00Z");
        var finTrainDeVie2 = Instant.parse("2024-12-26T00:00:00.00Z");

        new TrainDeVie(
                "Vie d'etudiant aaa",
                300_000,
                debutHEI,
                finTrainDeVie1,
                compteCourant,
                1);

        new TrainDeVie(
                "Vie d'etudiant bbb",
                200_000,
                debutHEI,
                finTrainDeVie2,
                compteCourant,
                1);

        var au14Jul24 = Instant.parse("2024-07-14T00:00:00.00Z");

        assertEquals(
                500_000, compteCourant.valeurComptableFuture(au14Jul24));

        var au14Dec24 = Instant.parse("2024-12-14T00:00:00.00Z");

        assertEquals(
                200_000, compteCourant.valeurComptableFuture(au14Dec24));
    }
}
