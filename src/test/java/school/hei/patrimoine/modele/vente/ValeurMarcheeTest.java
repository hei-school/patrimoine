package school.hei.patrimoine.modele.vente;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValeurMarcheeTest {

  LocalDate dateAcquisitionTypeDagregat;
  Argent valeurTypeDagregatInitiale;
  Compte type_dagregat_non_valide;
  ValeurMarchee subject;

  @BeforeAll
  void setUp() {
    var now = LocalDate.now();

    valeurTypeDagregatInitiale = Argent.ariary(300_000);
    dateAcquisitionTypeDagregat = now.minusDays(7);
    type_dagregat_non_valide =
        new Compte("non valide", dateAcquisitionTypeDagregat, now, valeurTypeDagregatInitiale);
  }

  @Test
  void creer_valeur_marchee() {
    var dateValeurMarchee = dateAcquisitionTypeDagregat.plusDays(3);

    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          new ValeurMarchee(
              type_dagregat_non_valide,
              dateValeurMarchee,
              valeurTypeDagregatInitiale.minus(Argent.ariary(20_000), dateValeurMarchee));
        });
  }
}
