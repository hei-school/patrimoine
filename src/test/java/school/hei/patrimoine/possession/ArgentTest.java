package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

public class ArgentTest {
    @Test
    void valeur_comptable_compte_courant_au_14_juillet_2024(){
        var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
        var compteCourant = new Argent("Compte courant", au13mai24, 600_000);
        var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
        var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
        var trainDeVieDebut = new TrainDeVie("train de vie de Ilo", 500_000, aLOuvertureDeHEI, aLaDiplomation, compteCourant, 1);
        var au14juilet2024 = Instant.parse("2024-07-14T00:00:00.00Z");
        var trainDeVieFinal = trainDeVieDebut.projectionFuture(au14juilet2024);
        assertEquals(trainDeVieFinal.getFinancePar().getValeurComptable(),compteCourant.valeurComptableFuture(au14juilet2024));
    }
}
