package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;


import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ZetyEtude2024_2025Test {
  @Test
  public void testDateEpuisementEspèces() {
    var dateDebut = LocalDate.of(2024, JANUARY, 1);
    var dateFin = LocalDate.of(2025, FEBRUARY, 13);
    var montantInitial = 0;
    var fraisDeScolarite = 2500000;
    var donMensuel = 100000;
    var trainDeVieMensuel = 250000;

    var date = dateDebut;
    var montantEnEspèces = montantInitial;

    while (date.isBefore(dateFin) || date.isEqual(dateFin)) {
      if (date.isEqual(LocalDate.of(2024, SEPTEMBER, 21))) {
        montantEnEspèces -= fraisDeScolarite;
      }

      if (date.getDayOfMonth() == 15 && date.isAfter(LocalDate.of(2024, JANUARY, 14))) {
        montantEnEspèces += donMensuel;
      }

      if (date.getDayOfMonth() == 1 && date.isAfter(LocalDate.of(2024, SEPTEMBER, 30))
              && date.isBefore(LocalDate.of(2025, FEBRUARY, 14))) {
        montantEnEspèces -= trainDeVieMensuel;
      }

      if (montantEnEspèces < 0) {
        break;
      }

      date = date.plusMonths(1);
    }
    assertEquals( LocalDate.of(2024, OCTOBER, 1), date);
  }
}