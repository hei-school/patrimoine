package school.hei.patrimoine.modele.vente;


import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;

public class ValeurMarcheTest {
    @Test
    void valeurMarche_doit_stocker_correctement_les_valeurs() {
        var date = LocalDate.of(2025, 1, 1);
        var argent = new Argent(300_000, Devise.EUR);
        var vm = new ValeurMarche(materiel, date, argent);

  @Test


    assertEquals(
  }
}
