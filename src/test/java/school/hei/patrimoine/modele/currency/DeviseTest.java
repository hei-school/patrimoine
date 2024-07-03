package school.hei.patrimoine.modele.currency;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.LocalDate.*;
import static org.junit.jupiter.api.Assertions.*;

class DeviseTest {

  @Test
  void devise_a_taux_de_change() {
    var ariary = new Devise("Ariary", "MGA");
    var euro = new Devise("Euro", "EUR");
    var tauxDeChange = new TauxDeChange(ariary, euro, 0.1, 0.5, now());
    System.out.println(ariary.convertir(50_000, euro));
  }
}
