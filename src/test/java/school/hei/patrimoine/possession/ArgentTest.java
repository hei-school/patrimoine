package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ArgentTest {
    @Test
    void valeur_comptable_compte_courant() {
            Instant au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
            Instant aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
            Instant aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
            Instant apresLaDiplomation = Instant.parse("2025-12-26T00:00:00.00Z");
            Instant unMoisApresOuvertureDeHEI = Instant.parse("2021-11-26T00:00:00.00Z");
            Instant avantLaDateDePonction = Instant.parse("2021-11-01T00:00:00.00Z");
            Argent compteCourant = new Argent("Compte courant", au13mai24, 600_000);
            TrainDeVie vieEstudiantine = new TrainDeVie(
                    "Ma super(?) vie d'etudiant",
                    500_000,
                    aLOuvertureDeHEI,
                    aLaDiplomation,
                    compteCourant,
                    1);
            TrainDeVie projectionApresDiplomation = vieEstudiantine.projectionFuture(apresLaDiplomation);
            assertEquals(0, projectionApresDiplomation.getDepensesMensuelle());
            assertEquals(600_000, projectionApresDiplomation.getFinancePar().getValeurComptable());
            TrainDeVie projectionUnMoisApresOuverture = vieEstudiantine.projectionFuture(unMoisApresOuvertureDeHEI);
            assertEquals(500_000, projectionUnMoisApresOuverture.getDepensesMensuelle());
            assertEquals(100_000, projectionUnMoisApresOuverture.getFinancePar().getValeurComptable());
            TrainDeVie projectionAvantDatePonction = vieEstudiantine.projectionFuture(avantLaDateDePonction);
            assertEquals(500_000, projectionAvantDatePonction.getDepensesMensuelle());
            assertEquals(600_000, projectionAvantDatePonction.getFinancePar().getValeurComptable());
        }
    }
