package school.hei.patrimoine.cas.zety;

import static java.time.Month.AUGUST;
import static java.time.Month.JULY;
import static java.time.Month.NOVEMBER;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class PatrimoineZetyAu3Juillet2024 implements Supplier<Patrimoine> {

	public static final LocalDate AU_3_JUILLET_2024 = LocalDate.of(2024, JULY, 3);

	private Argent compteBancaire() {
		return new Argent("compte bancaire argent", AU_3_JUILLET_2024, 100_000);
	}

	private Argent espèces() {
		return new Argent("espèces", AU_3_JUILLET_2024, AU_3_JUILLET_2024, 800_000);
	}

	private Materiel vêtements() {
		return new Materiel("vêtements", AU_3_JUILLET_2024, 1_500_000, AU_3_JUILLET_2024, -0.5);
	}

	private Materiel ordinateur() {
		return new Materiel("ordinateur", AU_3_JUILLET_2024, 1_200_000, AU_3_JUILLET_2024, -0.1);
	}

	private static Set<Possession> possessionsDu3Juillet2024(Materiel ordinateur, Materiel vêtements, Argent espèces, Argent compteBancaire) {
		new FluxArgent("scolarité 2023-2024", espèces, LocalDate.of(2023, NOVEMBER, 1), LocalDate.of(2024, AUGUST, 28), -200_000, 27);
		new FluxArgent("frais de tenue de compte", compteBancaire, AU_3_JUILLET_2024, LocalDate.MAX, -20_000, 25);
		return Set.of(ordinateur, vêtements, espèces, compteBancaire);
	}

	@Override
	public Patrimoine get() {
		var zety = new Personne("zety");
		var ordinateur = ordinateur();
		var vêtements = vêtements();
		var espèces = espèces();
		var compteBancaire = compteBancaire();

		Set<Possession> possessionsDu3Juillet = possessionsDu3Juillet2024(ordinateur, vêtements, espèces, compteBancaire);
		return new Patrimoine("zety au 3 juillet 2024", zety, AU_3_JUILLET_2024, possessionsDu3Juillet);
	}
}
