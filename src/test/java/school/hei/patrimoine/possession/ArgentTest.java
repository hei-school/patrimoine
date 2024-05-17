package school.hei.patrimoine.possession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ArgentTest {

  @Test
  void argent_dans_compte_ponctionne_par_train_de_vie() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

    var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
    var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
    var vieEstudiantine = new TrainDeVie(
        "Ma super(?) vie d'étudiant",
        500_000,
        aLOuvertureDeHEI,
        aLaDiplomation,
        compteCourant,
        1);

    var au14juillet24 = Instant.parse("2024-07-14T00:00:00.00Z");

    int financementSimule = 1_000_000;
    int valeurComptableAttendue = 600_000 - financementSimule;

    Argent projetCompteurCourant = new Argent(
        compteCourant.getNom(),
        au14juillet24,
        valeurComptableAttendue,
        compteCourant.getFinances());

    assertEquals(valeurComptableAttendue, projetCompteurCourant.getValeurComptable());
  }

  @Test
  void testFinancementsFutur() {
    Set<TrainDeVie> finances = new HashSet<>();

    Argent argent = new Argent("Argent", Instant.now(), 5000);
    TrainDeVie trainDeVie1 = new TrainDeVie("Train 1", 1000, Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2023-01-01T00:00:00Z"), argent, 0);
    finances.add(trainDeVie1);

    TrainDeVie trainDeVie2 = new TrainDeVie("Train 2", 2000, Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-12-31T00:00:00Z"), argent, 0);
    finances.add(trainDeVie2);

    Argent argentAvecFinances = new Argent("Argent avec finances", Instant.now(), 10000, finances);

    Instant tFutur = Instant.parse("2025-01-01T00:00:00Z");

    int sommeFinancements = argentAvecFinances.financementsFutur(tFutur);

    int sommeAttendue = trainDeVie1.getDepensesMensuelle() + trainDeVie2.getDepensesMensuelle();

    assertEquals(sommeAttendue, sommeFinancements);
  }

}
