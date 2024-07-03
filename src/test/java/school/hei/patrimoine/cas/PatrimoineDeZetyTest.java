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

	@Test
	void zety_étudie_en_2023_2024() {
		var patrimoineDeZetyAu3Jul = patrimoineDeZety3Jul2024();
		var projeté = patrimoineDeZetyAu3Jul.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17));

		assertEquals(3_600_000, patrimoineDeZetyAu3Jul.getValeurComptable());
		assertEquals(2_978_848, projeté.getValeurComptable());
	}
}
