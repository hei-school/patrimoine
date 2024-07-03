package school.hei.patrimoine.cas;

import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.zety.PatrimoineZetyAu3Juillet2024;
import school.hei.patrimoine.modele.Patrimoine;

class PatrimoineDeZetyTest {
	private final PatrimoineZetyAu3Juillet2024 patrimoineDeZetyAu3JuilletSupplier = new PatrimoineZetyAu3Juillet2024();

	private Patrimoine patrimoineDeZety3Jul2024() {
		return patrimoineDeZetyAu3JuilletSupplier.get();
	}

	private Patrimoine patrimoineDeZetySendette() {
		return patrimoineDeZetyAu3JuilletSupplier.zetySendette();
	}

	@Test
	void zety_étudie_en_2023_2024() {
		var patrimoineDeZetyAu3Jul = patrimoineDeZety3Jul2024();
		var projeté = patrimoineDeZetyAu3Jul.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17));

		assertEquals(3_600_000, patrimoineDeZetyAu3Jul.getValeurComptable());
		assertEquals(2_978_848, projeté.getValeurComptable());
	}

	@Test
	void zety_s_endette() {
		var patrimoineDu03Juillet2024 = patrimoineDeZety3Jul2024();
		var patrimoineDu17Septembre = patrimoineDu03Juillet2024.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17));
		var patrimoineDeZetySendette = patrimoineDeZetySendette();

		var differenceEntreLesDeuxPatrimoines = patrimoineDu17Septembre.getValeurComptable() - patrimoineDeZetySendette.getValeurComptable();

		assertEquals(2_978_848, patrimoineDu17Septembre.getValeurComptable());
		assertEquals(1_976_464, patrimoineDeZetySendette.getValeurComptable());
		assertEquals(1_002_384, differenceEntreLesDeuxPatrimoines);
	}
}
