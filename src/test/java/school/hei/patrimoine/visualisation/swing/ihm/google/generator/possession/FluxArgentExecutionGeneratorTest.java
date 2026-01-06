package school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession;

import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.visualisation.swing.ihm.google.generator.PossessionGeneratorFactory.MULTIPLE_EXECUTION_NOM_FORMAT;

import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class FluxArgentExecutionGeneratorTest {
  private static final FluxArgentExecutionGenerator subject = new FluxArgentExecutionGenerator();
  private static final LocalDate date = LocalDate.of(2025, SEPTEMBER, 5);

  private Argent valeur;
  private Compte compte;

  @BeforeEach
  void setUp() {
    valeur = ariary(5000);
    compte = new Compte("RandriaPersonnel", date, ariary(5000));
  }

  @Test
  void validateArgs_succeeds_when_all_arguments_are_present() {
    Map<String, Object> args =
        Map.of(
            "nom", "Salaire septembre",
            "date", date,
            "valeur", valeur,
            "compte", compte);

    assertDoesNotThrow(() -> subject.validateArgs(args));
  }

  @Test
  void validateArgs_throws_exception_when_name_is_missing() {
    Map<String, Object> args =
        Map.of(
            "date", date,
            "valeur", valeur,
            "compte", compte);

    var thrownException =
        assertThrows(IllegalArgumentException.class, () -> subject.validateArgs(args));
    assertEquals("nom is mandatory to create a fluxArgent", thrownException.getMessage());
  }

  @Test
  void validateArgs_throws_exception_when_date_is_missing() {
    Map<String, Object> args =
        Map.of(
            "nom", "Salaire septembre",
            "valeur", valeur,
            "compte", compte);

    var thrownException =
        assertThrows(IllegalArgumentException.class, () -> subject.validateArgs(args));
    assertEquals("date is mandatory to create a fluxArgent", thrownException.getMessage());
  }

  @Test
  void validateArgs_throws_exception_when_value_is_missing() {
    Map<String, Object> args =
        Map.of(
            "nom", "Salaire septembre",
            "date", date,
            "compte", compte);

    var thrownException =
        assertThrows(IllegalArgumentException.class, () -> subject.validateArgs(args));
    assertEquals("valeur is mandatory to create a fluxArgent", thrownException.getMessage());
  }

  @Test
  void validateArgs_throws_exception_when_account_is_missing() {
    Map<String, Object> args =
        Map.of(
            "nom", "Salaire septembre",
            "date", date,
            "valeur", valeur);

    var thrownException =
        assertThrows(IllegalArgumentException.class, () -> subject.validateArgs(args));
    assertEquals("compte is mandatory to create a fluxArgent", thrownException.getMessage());
  }

  @Test
  void apply_creates_flux_argent_correctly_without_prevu() {
    Map<String, Object> args =
        Map.of(
            "nom", "Salaire septembre",
            "date", date,
            "valeur", valeur,
            "compte", compte);

    FluxArgent actua = subject.apply(args);

    assertNotNull(actua);
    assertEquals("Salaire septembre", actua.nom());
    assertEquals(compte, actua.getCompte());
    assertEquals(date, actua.getDebut());
    assertEquals(date, actua.getFin());
    assertEquals(valeur, actua.getFluxMensuel());
  }

  @Test
  void apply_creates_flux_argent_with_formatted_name_when_prevu_exists() {
    var prevu = new FluxArgent("Revenus", compte, date, valeur, null);

    Map<String, Object> args =
        Map.of(
            "nom", "Salaire septembre",
            "date", date,
            "valeur", valeur,
            "compte", compte,
            "prevu", prevu);

    FluxArgent actual = subject.apply(args);

    String expectedNom =
        String.format(MULTIPLE_EXECUTION_NOM_FORMAT, prevu.nom(), "Salaire septembre");
    assertEquals(expectedNom, actual.nom());
  }
}
