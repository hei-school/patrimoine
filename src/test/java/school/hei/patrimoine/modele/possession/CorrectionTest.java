package school.hei.patrimoine.modele.possession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Devise.EUR;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;

public class CorrectionTest {
  @Test
  void projection_future_utilise_la_date_de_la_possession_projete() {
    var t0 = LocalDate.of(2025, 1, 1);
    var tFutur = LocalDate.of(2030, 1, 1);

    var possession = new Compte("ComptePrincipal", t0, new Argent(1_000, EUR));

    var correction = new Correction(possession, "ajustement", t0, new Argent(500, EUR));

    var projected = correction.projectionFuture(tFutur);

    assertEquals(tFutur, projected.t);
  }

  @Test
  void projection_future_conserve_exactement_le_nom_de_la_possession() {
    var possession = new Compte("Banque", LocalDate.of(2025, 1, 1), new Argent(20_000, EUR));

    var correction = new Correction(possession, "fix", possession.t, new Argent(5_000, EUR));

    var projected = correction.projectionFuture(LocalDate.of(2027, 1, 1));

    assertEquals(possession.nom, projected.nom);
  }
}
