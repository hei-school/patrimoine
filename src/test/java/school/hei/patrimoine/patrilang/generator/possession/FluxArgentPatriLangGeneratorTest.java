package school.hei.patrimoine.patrilang.generator.possession;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class FluxArgentPatriLangGeneratorTest {
  private final FluxArgentPatriLangGenerator subject = new FluxArgentPatriLangGenerator();
  private static final LocalDate date = LocalDate.of(2025, 1, 1);
  private static final Compte compte = new Compte("comptePersonnel", date, Argent.ariary(5000));

  @Test
  void test_entree() {
    var flux = new FluxArgent("flux1", compte, date, Argent.ariary(1000));
    var result = subject.apply(flux);

    String expected =
        "* `flux1`, le 1 janvier 2025 entrer 1_000Ar vers Trésoreries:comptePersonnel";
    assertEquals(expected, result);
    assertTrue(result.contains("entrer"));
  }

  @Test
  void test_sortie() {
    var flux = new FluxArgent("flux1", compte, date, Argent.ariary(-1000));
    var result = subject.apply(flux);

    String expected =
        "* `flux1`, le 1 janvier 2025 sortir 1_000Ar depuis Trésoreries:comptePersonnel";
    assertEquals(expected, result);
    assertTrue(result.contains("sortir"));
  }

  @Test
  void test_zero_flux() {
    var flux = new FluxArgent("flux1", compte, date, Argent.ariary(0));
    var result = subject.apply(flux);

    String expected = "* `flux1`, le 1 janvier 2025 entrer 0Ar vers Trésoreries:comptePersonnel";
    assertEquals(expected, result);
    assertTrue(result.contains("entrer"));
  }

  @Test
  void test_null_compte() {
    assertThrows(
        NullPointerException.class,
        () -> {
          var flux = new FluxArgent("flux1", null, date, Argent.ariary(1000));
          subject.apply(flux);
        });
  }

  @Test
  void test_null_date() {
    assertThrows(
        NullPointerException.class,
        () -> {
          var flux = new FluxArgent("flux1", compte, null, Argent.ariary(1000));
          subject.apply(flux);
        });
  }
}
