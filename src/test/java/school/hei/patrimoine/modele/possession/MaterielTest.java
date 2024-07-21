package school.hei.patrimoine.modele.possession;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Devise;

class MaterielTest {

  @Test
  void mon_mac_s_apprecie_negativement_dans_le_futur() {
    var au26Oct21 = LocalDate.of(2021, OCTOBER, 26);
    var mac = new Materiel("MacBook Pro", au26Oct21, 2_000_000, au26Oct21.minusDays(2), -0.10);

    var au26juin24 = LocalDate.of(2024, JUNE, 26);
    assertEquals(1_466_301, mac.valeurComptableFuture(au26juin24));
  }


  @Test
  void materiel_initialisation_correcte() {
    var dateAcquisition = LocalDate.of(2023, MAY, 13);
    var dateEvaluation = LocalDate.of(2024, MAY, 13);
    var materiel = new Materiel("Ordinateur", dateEvaluation, 1_000_000, dateAcquisition, 0.05);

    assertEquals("Ordinateur", materiel.getNom());
    assertEquals(dateEvaluation, materiel.getT());
    assertEquals(1_000_000, materiel.getValeurComptable());
    assertEquals(dateAcquisition, materiel.getDateAcquisition());
    assertEquals(0.05, materiel.getTauxDAppreciationAnnuelle());
    assertEquals(Devise.NON_NOMMEE, materiel.getDevise());
  }

  @Test
  void materiel_projectionFuture_avant_acquisition() {
    var dateAcquisition = LocalDate.of(2023, MAY, 13);
    var datePassé = LocalDate.of(2023, MAY, 12);
    var materiel = new Materiel("Ordinateur", dateAcquisition, 1_000_000, dateAcquisition, 0.05);

    var projection = materiel.projectionFuture(datePassé);
    assertEquals(0, projection.getValeurComptable());
  }

  @Test
  void materiel_projectionFuture_apres_acquisition() {
    var dateAcquisition = LocalDate.of(2023, MAY, 13);
    var dateFuture = LocalDate.of(2024, MAY, 13);
    var materiel = new Materiel("Ordinateur", dateAcquisition, 1_000_000, dateAcquisition, 0.05);

    var projection = materiel.projectionFuture(dateFuture);
    assertTrue(projection.getValeurComptable() > 1_000_000);
  }
}
