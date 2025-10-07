package school.hei.patrimoine.modele.recouppement.decomposeur;

import static java.time.Month.JANUARY;
import static java.time.Month.MARCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

public class FluxArgentDecomposeurTest {
  FluxArgentDecomposeur subject = new FluxArgentDecomposeur(LocalDate.MAX);

  @Test
  void decompose_flux_d_une_seule_date() {
    var compte = new Compte("Compte A", LocalDate.of(2025, JANUARY, 1), ariary(0));
    var flux =
        new FluxArgent(
            "Salaire unique",
            compte,
            LocalDate.of(2025, JANUARY, 12),
            LocalDate.of(2025, JANUARY, 12),
            12,
            ariary(500));

    var actual = subject.apply(flux);

    assertEquals(1, actual.size());
    assertEquals("Salaire unique", actual.getFirst().nom());
    assertEquals(LocalDate.of(2025, JANUARY, 12), actual.getFirst().getDebut());
    assertEquals(LocalDate.of(2025, JANUARY, 12), actual.getFirst().getFin());
  }

  @Test
  void decompose_flux_mensuel_sur_trois_mois() {
    var compte = new Compte("Compte B", LocalDate.of(2025, JANUARY, 1), ariary(0));
    var flux =
        new FluxArgent(
            "Salaire",
            compte,
            LocalDate.of(2025, JANUARY, 1),
            LocalDate.of(2025, MARCH, 31),
            12,
            ariary(1000));

    var actual = subject.apply(flux);

    assertEquals(3, actual.size());
    assertEquals("Salaire__du_2025_01_12", actual.get(0).nom());
    assertEquals("Salaire__du_2025_02_12", actual.get(1).nom());
    assertEquals("Salaire__du_2025_03_12", actual.get(2).nom());
  }

  @Test
  void decompose_flux_sans_date_operation_dans_intervalle() {
    var compte = new Compte("Compte C", LocalDate.of(2025, JANUARY, 1), ariary(0));
    var flux =
        new FluxArgent(
            "Salaire",
            compte,
            LocalDate.of(2025, JANUARY, 1),
            LocalDate.of(2025, JANUARY, 10),
            12,
            ariary(1000));

    var actual = subject.apply(flux);

    assertTrue(actual.isEmpty());
  }
}
