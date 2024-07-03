package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MaterielTest {

  @Test
  void mon_mac_s_apprecie_negativement_dans_le_futur() {
    var au26Oct21 = LocalDate.of(2021, OCTOBER, 26);
    var mac = new Materiel(
            "MacBook Pro",
            au26Oct21,
            2_000_000,
            au26Oct21.minusDays(2),
            -0.10);

    var au26juin24 = LocalDate.of(2024, JUNE, 26);
    assertEquals(1_466_301, mac.valeurComptableFuture(au26juin24));
  }

  @Test
  void l_ordinateur_de_zety_s_apprecie_negativement_dans_le_futur() {
    var au03Juil24 = LocalDate.of(2024, JULY, 3);
    var ordinateur = new Materiel(
            "Ordinateur portable",
            au03Juil24,
            1_200_000,
            au03Juil24.minusDays(2),
            -0.1
    );

    var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);
    assertEquals(1_175_013, ordinateur.valeurComptableFuture(au17Sept2024));
    assertEquals(1_174_684, ordinateur.projectionFuture(au17Sept2024.plusDays(1)).valeurComptable);
    assertEquals(1_165_150, ordinateur.valeurComptableFuture(au17Sept2024.plusMonths(1)));
  }

  @Test
  void les_vetements_de_zety_s_apprecie_negativement_dans_le_futur() {
    var au03Juil24 = LocalDate.of(2024, JULY, 3);

    var vetements = new Materiel(
            "Vêtements",
            au03Juil24,
            1_500_000,
            au03Juil24.minusDays(2),
            -0.5
    );

    var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);
    assertEquals(1_343_835, vetements.valeurComptableFuture(au17Sept2024));
    assertEquals(1_341_780, vetements.projectionFuture(au17Sept2024.plusDays(1)).valeurComptable);
    assertEquals(1_282_191, vetements.valeurComptableFuture(au17Sept2024.plusMonths(1)));
  }
}