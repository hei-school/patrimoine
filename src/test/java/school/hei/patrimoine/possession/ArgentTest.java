package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgentTest {
    @Test
    public void finance_compte_vide() {
        var instant1 = Instant.parse("2024-05-13T00:00:00.00Z");
        var compte = new Argent("Compte Vide", instant1, 0);

        var instant2 = Instant.parse("2025-05-13T00:00:00.00Z");
        assertEquals(0, compte.projectionFuture(instant2).getValeurComptable());
    }

    @Test
    public void finance_train_de_vie_simple() {
        var instant1 = Instant.parse("2024-05-13T00:00:00.00Z");
        var compte = new Argent("Compte", instant1, 600_000);

        var debutVie = Instant.parse("2021-10-26T00:00:00.00Z");
        var finVie = Instant.parse("2024-12-26T00:00:00.00Z");
        var vie = new TrainDeVie(
                "Vie Etudiante",
                500_000,
                debutVie,
                finVie,
                compte,
                2);

        var dateProjection = Instant.parse("2021-12-13T00:00:00.00Z");
        assertEquals(-400_000, compte.projectionFuture(dateProjection).getValeurComptable());
    }

    @Test
    public void finance_multiples_trains_de_vie() {
        var dateDebutCompte = Instant.parse("2000-05-13T00:00:00.00Z");
        var compte = new Argent("Compte", dateDebutCompte, 600_000);

        var debutVie = Instant.parse("2021-10-26T00:00:00.00Z");
        var finVie = Instant.parse("2024-12-26T00:00:00.00Z");
        var debutVie2 = Instant.parse("2022-01-01T00:00:00.00Z");
        var dateProjection = Instant.parse("2022-01-09T00:00:00.00Z");

        var vie1 = new TrainDeVie(
                "Vie 1",
                100_000,
                debutVie,
                debutVie2,
                compte,
                2);
        var vie2 = new TrainDeVie(
                "Vie 2",
                100_000,
                debutVie,
                finVie,
                compte,
                10);
        var vie3 = new TrainDeVie(
                "Vie 3",
                50,
                debutVie,
                finVie,
                compte,
                7);

        assertEquals(199_850, compte.projectionFuture(dateProjection).getValeurComptable());
    }
}
