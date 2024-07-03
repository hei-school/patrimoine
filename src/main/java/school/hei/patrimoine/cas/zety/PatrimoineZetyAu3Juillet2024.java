package school.hei.patrimoine.cas.zety;

import static java.time.Month.AUGUST;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.NOVEMBER;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class PatrimoineZetyAu3Juillet2024 implements Supplier<Patrimoine> {

	public static final LocalDate AU_3_JUILLET_2024 = LocalDate.of(2024, JULY, 3);
	public static final LocalDate AU_18_SEPTEMBRE_2024 = LocalDate.of(2024, SEPTEMBER, 18);
	public static final LocalDate AU_14_FEVRIER_2025 = LocalDate.of(2025, FEBRUARY, 14);
	public static final LocalDate AU_18_SEPTEMBRE_2025 = LocalDate.of(2025, SEPTEMBER, 18);

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

	private static Set<Possession> possessionsRajoutéesLe18Septembre2024(Argent compteBancaire) {
		LocalDate dateDePriseDeffetDette = AU_18_SEPTEMBRE_2024;
		LocalDate dateDeRemboursementDette = AU_18_SEPTEMBRE_2025;
		int valeurDetteARembourser = -11_000_000;
		var dette = new Dette("dette de 10M", dateDePriseDeffetDette, valeurDetteARembourser);
		new FluxArgent("remboursement de la dette", compteBancaire, dateDeRemboursementDette, dateDeRemboursementDette, valeurDetteARembourser, dateDeRemboursementDette.getDayOfMonth());
		int valeurDetteRajoutéeAuCompte = 10_000_000;
		new FluxArgent("flux de la dette", compteBancaire, dateDePriseDeffetDette, dateDePriseDeffetDette, valeurDetteRajoutéeAuCompte, dateDePriseDeffetDette.getDayOfMonth());
		return Set.of(dette);
	}

	private static Set<Possession> possessionsRajoutéesAprèsLe18Septembre2024(Argent espèces, Argent compteBancaire) {
		LocalDate au21Septembre2024 = LocalDate.of(2024, SEPTEMBER, 21);
		new FluxArgent("frais de scolarité 1 fois", compteBancaire, au21Septembre2024, au21Septembre2024, -2_500_000, au21Septembre2024.getDayOfMonth());
		new FluxArgent("dons parentaux", espèces, LocalDate.of(2024, JANUARY, 1), LocalDate.MAX, 100_000, 15);
		new FluxArgent("train de vie mensuel", espèces, LocalDate.of(2024, OCTOBER, 1), LocalDate.of(2025, FEBRUARY, 13), -250_000, 1);
		return Set.of();
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

	public Patrimoine zetySendette() {
		var zety = new Personne("zety");
		var ordinateur = ordinateur();
		var vêtements = vêtements();
		var espèces = espèces();
		var compteBancaire = compteBancaire();
		GroupePossession possessionsDu3Juillet = new GroupePossession("possessions du 3 Juillet", AU_3_JUILLET_2024, possessionsDu3Juillet2024(ordinateur, vêtements, espèces, compteBancaire));
		GroupePossession possessionsRajoutéesLe18Septembre = new GroupePossession("possessions ajoutées le 18 Septembre 2024", AU_18_SEPTEMBRE_2024, possessionsRajoutéesLe18Septembre2024(compteBancaire));

		return new Patrimoine("zety au 18 Septembre 2024", zety, AU_18_SEPTEMBRE_2024, Set.of(possessionsDu3Juillet, possessionsRajoutéesLe18Septembre))
			.projectionFuture(AU_18_SEPTEMBRE_2024);
	}

	public Argent argentEnEspècesDeZetyEn20242025() {
		//kept for some sort of pattern in all the getters
		var zety = new Personne("zety");
		var ordinateur = ordinateur();
		var vêtements = vêtements();
		var espèces = espèces();
		var compteBancaire = compteBancaire();
		new GroupePossession("possessions du 3 Juillet", AU_3_JUILLET_2024, possessionsDu3Juillet2024(ordinateur, vêtements, espèces, compteBancaire));
		new GroupePossession("possessions ajoutées le 18 Septembre 2024", AU_18_SEPTEMBRE_2024, possessionsRajoutéesLe18Septembre2024(compteBancaire));
		new GroupePossession("possessions ajoutées après le 18 Septembre", AU_18_SEPTEMBRE_2024, possessionsRajoutéesAprèsLe18Septembre2024(espèces, compteBancaire));

		return espèces;
	}

	public Patrimoine patrimoineDeZetyLe14Fevrier2025() {
		var zety = new Personne("zety");
		var ordinateur = ordinateur();
		var vêtements = vêtements();
		var espèces = espèces();
		var compteBancaire = compteBancaire();
		GroupePossession possessionsDu3Juillet2024 = new GroupePossession("possessions du 3 Juillet", AU_3_JUILLET_2024, possessionsDu3Juillet2024(ordinateur, vêtements, espèces, compteBancaire));
		GroupePossession possessionsRajoutéesLe18Septembre2024 = new GroupePossession("possessions ajoutées le 18 Septembre 2024", AU_18_SEPTEMBRE_2024, possessionsRajoutéesLe18Septembre2024(compteBancaire));
		GroupePossession possessionsRajoutéesAprèsLe18Septembre2024 = new GroupePossession("possessions ajoutées après le 18 Septembre", AU_18_SEPTEMBRE_2024, possessionsRajoutéesAprèsLe18Septembre2024(espèces, compteBancaire));

		return new Patrimoine("zety au 14 Février 2025", zety, AU_14_FEVRIER_2025, Set.of(possessionsDu3Juillet2024, possessionsRajoutéesLe18Septembre2024, possessionsRajoutéesAprèsLe18Septembre2024));
	}
}
