package school.hei.patrimoine.modele.vente;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Materiel;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValeurMarcheeTest {
  LocalDate dateAcquisitionPc;
  Argent valeurPcInitiale;
  Materiel pc;
  ValeurMarche subject;

  @BeforeAll
  void setUp() {
    var now = LocalDate.now();

    valeurPcInitiale = Argent.ariary(300_000);
    dateAcquisitionPc = now.minusDays(7);
    pc = new Materiel("Asus X555", dateAcquisitionPc, now, valeurPcInitiale, 10);
  }

  @Test
  void creer_valeur_marchee() {
    var dateValeurMarchee = dateAcquisitionPc.plusDays(3);
    subject =
        new ValeurMarche(
            pc,
            dateValeurMarchee,
            valeurPcInitiale.minus(Argent.ariary(20_000), dateValeurMarchee));

    assertEquals(subject, pc.getValeurMarche(dateValeurMarchee.plusDays(1)));
  }
}
