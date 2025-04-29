package school.hei.patrimoine.cas;

import static java.time.Month.JANUARY;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.cas.example.PatrimoineZetyAu3Juillet2024.AU_14_FEVRIER_2025;
import static school.hei.patrimoine.cas.example.PatrimoineZetyAu3Juillet2024.AU_26_OCTOBRE_2025;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Argent.euro;
import static school.hei.patrimoine.modele.Devise.EUR;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.example.PatrimoineZetyAu3Juillet2024;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Compte;

@Slf4j
class PatrimoineDeZetyTest {
  private final PatrimoineZetyAu3Juillet2024 patrimoineDeZetyAu3JuilletSupplier =
      new PatrimoineZetyAu3Juillet2024();

  private Patrimoine patrimoineDeZety3Jul2024() {
    return patrimoineDeZetyAu3JuilletSupplier.get();
  }

  private Patrimoine patrimoineDeZetySendette() {
    return patrimoineDeZetyAu3JuilletSupplier.zetySendette();
  }

  private Compte argentEnEspècesDeZetyEn20242025() {
    return patrimoineDeZetyAu3JuilletSupplier.argentEnEspècesDeZetyEn20242025();
  }

  private Patrimoine patrimoineDeZetyLe14Fev2025() {
    return patrimoineDeZetyAu3JuilletSupplier.patrimoineDeZetyLe14Fevrier2025();
  }

  private Patrimoine patrimoineDeZetyLe26Octobre2025() {
    return patrimoineDeZetyAu3JuilletSupplier.patrimoineDeZety26Octobre2025();
  }

  @Test
  void zety_étudie_en_2023_2024() {
    var patrimoineDeZetyAu3Jul = patrimoineDeZety3Jul2024();
    var projeté = patrimoineDeZetyAu3Jul.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17));

    assertEquals(ariary(3_600_000), patrimoineDeZetyAu3Jul.getValeurComptable());
    assertEquals(ariary(2_978_849), projeté.getValeurComptable());
  }

  @Test
  void zety_s_endette() {
    var patrimoineDu03Juillet2024 = patrimoineDeZety3Jul2024();
    var patrimoineDu17Septembre =
        patrimoineDu03Juillet2024.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17));
    var patrimoineDeZetySendette = patrimoineDeZetySendette();

    var differenceEntreLesDeuxPatrimoines =
        patrimoineDu17Septembre
            .getValeurComptable()
            .minus(patrimoineDeZetySendette.getValeurComptable(), LocalDate.MIN);

    assertEquals(ariary(2_978_849), patrimoineDu17Septembre.getValeurComptable());
    assertEquals(ariary(1_976_465), patrimoineDeZetySendette.getValeurComptable());
    assertEquals(ariary(1_002_384), differenceEntreLesDeuxPatrimoines);
  }

  @Test
  void zety_étudie_en_2024_2025() {
    var argentEnEspècesDeZetyEn20242025 = argentEnEspècesDeZetyEn20242025();
    log.debug(
        "montant valeur comptable 2024 2025 {} ",
        argentEnEspècesDeZetyEn20242025.valeurComptable());
    LocalDate dayOfFailureFrom18September = LocalDate.of(2024, SEPTEMBER, 18);

    int i = 0;
    do {
      LocalDate tFutur = dayOfFailureFrom18September.plusDays(i);
      var argentEnEspècesProjeté = argentEnEspècesDeZetyEn20242025.projectionFuture(tFutur);
      log.debug("à t={} montant = {}", argentEnEspècesProjeté.valeurComptable(), tFutur);
      if (argentEnEspècesProjeté.valeurComptable().le(0)) {
        dayOfFailureFrom18September = tFutur;
        break;
      }
      i++;
    } while (true);

    assertEquals(LocalDate.of(2025, JANUARY, 1), dayOfFailureFrom18September);
  }

  @Test
  void zety_veut_partir_le_14_février_2025() {
    var patrimoineDeZetyLe14Fevrier2025 =
        patrimoineDeZetyLe14Fev2025().projectionFuture(AU_14_FEVRIER_2025);

    assertEquals(ariary(-1_528_684), patrimoineDeZetyLe14Fevrier2025.getValeurComptable());
  }

  @Test
  void zety_part_en_Allemagne() {
    var patrimoineDeZetyLe14Fevrier2025 =
        patrimoineDeZetyLe26Octobre2025().projectionFuture(AU_26_OCTOBRE_2025);

    assertEquals(euro(-9_477), patrimoineDeZetyLe14Fevrier2025.getValeurComptable(EUR));
  }
}
