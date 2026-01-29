package school.hei.patrimoine.patrilang.generator.possession;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.possession.TypeFEC.*;

import java.time.LocalDate;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class FluxArgentPatriLangGeneratorTest {
  private static final FluxArgentPatriLangGenerator subject = new FluxArgentPatriLangGenerator();
  private static final LocalDate date = LocalDate.of(2025, JANUARY, 1);
  private static final Compte compte = new Compte("comptePersonnel", date, ariary(5000));

  @Test
  void test_entree() {
    var flux = new FluxArgent("flux1", compte, date, ariary(1000));
    var actual = subject.apply(flux);
    var expected = "* `flux1`, le 1 janvier 2025 entrer 1_000Ar vers Trésoreries:comptePersonnel";

    assertEquals(expected, actual);
  }

  @Test
  void test_sortie() {
    var flux = new FluxArgent("flux1", compte, date, ariary(-1000));
    var actual = subject.apply(flux);
    var expected = "* `flux1`, le 1 janvier 2025 sortir 1_000Ar depuis Trésoreries:comptePersonnel";

    assertEquals(expected, actual);
  }

  @Test
  void test_zero_flux() {
    var flux = new FluxArgent("flux1", compte, date, ariary(0));
    var actual = subject.apply(flux);
    var expected = "* `flux1`, le 1 janvier 2025 entrer 0Ar vers Trésoreries:comptePersonnel";
    assertEquals(expected, actual);
  }

  @Test
  void test_generator_is_stateless() {
    var flux = new FluxArgent("flux", compte, date, ariary(300));
    var first = subject.apply(flux);
    var second = subject.apply(flux);

    assertEquals(first, second);
  }

  @Test
  void test_getTypeFEC_dans_FluxArgent_est_null_ou_pas() {
    var flux1 = new FluxArgent("flux1", compte, date, ariary(100));
    var flux2 = new FluxArgent("flux2", compte, date, ariary(100), CCA);

    assertEquals(flux1.getTypeFEC(), PRODUIT);
    assertEquals(flux2.getTypeFEC(), CCA);
  }

  private String extractId(String character) {
    var pattern = Pattern.compile("`([^`]+)`");
    var matcher = pattern.matcher(character);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }
}
