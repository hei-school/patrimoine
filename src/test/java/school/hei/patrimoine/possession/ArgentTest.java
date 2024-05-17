
package school.hei.patrimoine.possession;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class ArgentTest {
    @Test
    public void argent_finance_rien(){
        var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
        var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

        var au13mai25 = Instant.parse("2025-05-13T00:00:00.00Z");
        assertEquals(600_000, compteCourant.projectionFuture(au13mai25).getValeurComptable());
    }

    @Test
    public void argent_finance_une_train_de_vie(){
        var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
        var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

        var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
        var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
        var vieEstudiantine = new TrainDeVie(
            "Ma super(?) vie d'etudiant",
            500_000,
            aLOuvertureDeHEI,
            aLaDiplomation,
            compteCourant,
            2);

        var au13Decembre21 = Instant.parse("2021-12-13T00:00:00.00Z");
        assertEquals(-400_000, compteCourant.projectionFuture(au13Decembre21).getValeurComptable());
    }

    @Test
    public void argent_finance_plusieur_train_de_vie(){
        var au13mai20 = Instant.parse("2000-05-13T00:00:00.00Z");
        var compteCourant = new Argent("Compte courant", au13mai20, 600_000);

        var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
        var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
        var aLaRegime = Instant.parse("2022-01-01T00:00:00.00Z");
        var vieEstEtudianteGourmande = new TrainDeVie(
            "Ma super vie",
            100_000,
            aLOuvertureDeHEI,
            aLaRegime,
            compteCourant,
            2);
        var vieEstEtudianteJoueuse = new TrainDeVie(
            "Ma super vie",
            100_000,
            aLOuvertureDeHEI,
            aLaDiplomation,
            compteCourant,
            10);
        var vieEstudiantine = new TrainDeVie(
            "Ma super(?) vie d'etudiant",
            50,
            aLOuvertureDeHEI,
            aLaDiplomation,
            compteCourant,
            7);

        var au9Janvier22 = Instant.parse("2022-01-09T00:00:00.00Z");
        assertEquals(199_850, compteCourant.projectionFuture(au9Janvier22).getValeurComptable());
    }
}