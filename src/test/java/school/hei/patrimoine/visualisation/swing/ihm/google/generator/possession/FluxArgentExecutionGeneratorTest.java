package school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.generator.PossessionGeneratorFactory.MULTIPLE_EXECUTION_NOM_FORMAT;

import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class FluxArgentExecutionGeneratorTest {
  private FluxArgentExecutionGenerator subject;
  private LocalDate date;
  private Argent valeur;
  private Compte compte;

  @BeforeEach
  void setUp() {
    subject = new FluxArgentExecutionGenerator();
    date = LocalDate.of(2025, 9, 5);
    valeur = Argent.ariary(5000);
    compte = new Compte("RandriaPersonnel", date, Argent.ariary(5000));
  }

  @Test
  void validateArgs_ok_si_tous_les_arguments_sont_presents() {
    Map<String, Object> args =
        Map.of(
            "nom", "Salaire septembre",
            "date", date,
            "valeur", valeur,
            "compte", compte);

    assertDoesNotThrow(() -> subject.validateArgs(args));
  }

  @Test
  void validateArgs_lance_exception_si_nom_manquant() {
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
  void validateArgs_lance_exception_si_date_manquante() {
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
  void validateArgs_lance_exception_si_valeur_manquante() {
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
  void validateArgs_lance_exception_si_compte_manquant() {
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
  void apply_cree_flux_argent_correctement_sans_prevu() {
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
  void apply_cree_flux_argent_avec_nom_formate_quand_prevu_existe() {
    FluxArgent prevu = new FluxArgent("Revenus", compte, date, valeur);

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
