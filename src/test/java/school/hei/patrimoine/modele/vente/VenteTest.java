package school.hei.patrimoine.modele.vente;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Vente;

public class VenteTest {
  @Test
  void vendre_possession() {
    var dateAcquisition = LocalDate.now().minusDays(1);
    var t = LocalDate.now();
    var valeur_du_type_dagregat_non_valide = Argent.ariary(200_000);
    var type_dagregat_non_valide =
        new Compte("test", LocalDate.of(2023, Month.APRIL, 4), valeur_du_type_dagregat_non_valide);

    var compteBeneficiaire =
        new Compte("porte feuille", dateAcquisition.minusDays(1), Argent.ariary(0));

    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          new Vente(
              t,
              type_dagregat_non_valide,
              valeur_du_type_dagregat_non_valide,
              t.plusDays(1),
              compteBeneficiaire);
        });
  }
}
