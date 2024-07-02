package school.hei.patrimoine.cas;



import static java.time.Month.*;


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
import school.hei.patrimoine.modele.possession.Devise;

public class PatrimoineZetyCas implements Supplier<Patrimoine> {

    public static final LocalDate AU_3_JUILLET_2024 = LocalDate.of(2024, JULY, 3);
    public static final LocalDate AU_18_SEPTEMBRE_2024 = LocalDate.of(2024, SEPTEMBER, 18);
    public static final LocalDate AU_18_SEPTEMBRE_2025 = LocalDate.of(2025, SEPTEMBER, 18);
    public static final LocalDate AU_26_OCTOBRE_2025 = LocalDate.of(2025, OCTOBER, 26);
    public static final LocalDate AU_15_FEVRIER_2025 = LocalDate.of(2024, FEBRUARY, 15);
    public static final LocalDate AU_14_FEVRIER_2025 = LocalDate.of(2025, FEBRUARY, 14);

    private Argent compteBancaire() {
        return new Argent("compte bancaire argent", AU_3_JUILLET_2024, 100_000);
    }

    private Argent especes() {
        return new Argent("espèces", AU_3_JUILLET_2024, AU_3_JUILLET_2024, 800_000);
    }

    private Materiel vetements() {
        return new Materiel("vêtements", AU_3_JUILLET_2024, 1_500_000, AU_3_JUILLET_2024, -0.5);
    }

    private Materiel ordinateur() {
        return new Materiel("ordinateur", AU_3_JUILLET_2024, 1_200_000, AU_3_JUILLET_2024, -0.1);
    }

    private static Set<Possession> possessionsDu3Juillet2024(Materiel ordinateur, Materiel vetements, Argent especes, Argent compteBancaire) {
        new FluxArgent(" 2023-2024", especes, LocalDate.of(2023, NOVEMBER, 1), LocalDate.of(2024, AUGUST, 28), -200_000, 27);
        new FluxArgent("frais de compte", compteBancaire, AU_3_JUILLET_2024, LocalDate.MAX, -20_000, 25);
        return Set.of(ordinateur, vetements, especes, compteBancaire);
    }

    private static Set<Possession> possessionsRajouteesLe18Septembre2024(Argent compteBancaire) {
        LocalDate datePriseEffetDette = AU_18_SEPTEMBRE_2024;
        LocalDate dateRemboursementDette = AU_18_SEPTEMBRE_2025;
        int valeurDetteARembourser = -11_000_000;
        Dette dette = new Dette("dette de 10M", datePriseEffetDette, valeurDetteARembourser);
        new FluxArgent("remboursement", compteBancaire, dateRemboursementDette, dateRemboursementDette, valeurDetteARembourser, dateRemboursementDette.getDayOfMonth());
        int valeurDetteRajouteeAuCompte = 10_000_000;
        new FluxArgent("flux de la dette", compteBancaire, datePriseEffetDette, datePriseEffetDette, valeurDetteRajouteeAuCompte, datePriseEffetDette.getDayOfMonth());
        return Set.of(dette);
    }

    private static Set<Possession> possessionsRajouteesApresLe18Septembre2024(Argent especes, Argent compteBancaire) {
        LocalDate au21Septembre2024 = LocalDate.of(2024, SEPTEMBER, 21);
        new FluxArgent("frais de scolarité 1 fois", compteBancaire, au21Septembre2024, au21Septembre2024, -2_500_000, au21Septembre2024.getDayOfMonth());
        new FluxArgent("dons parentaux", especes, LocalDate.of(2024, JANUARY, 1), LocalDate.MAX, 100_000, 15);
        new FluxArgent("train de vie mensuel", especes, LocalDate.of(2024, OCTOBER, 1), LocalDate.of(2025, FEBRUARY, 13), -250_000, 1);
        return Set.of();
    }

    @Override
    public Patrimoine get() {
        var zety = new Personne("zety");
        var ordinateur = ordinateur();
        var vetements = vetements();
        var especes = especes();
        var compteBancaire = compteBancaire();

        Set<Possession> possessionsDu3Juillet = possessionsDu3Juillet2024(ordinateur, vetements, especes, compteBancaire);
        return new Patrimoine("zety au 3 juillet 2024", zety, AU_3_JUILLET_2024, possessionsDu3Juillet);
    }

    public Patrimoine zetySendette() {
        var zety = new Personne("zety");
        var ordinateur = ordinateur();
        var vetements = vetements();
        var especes = especes();
        var compteBancaire = compteBancaire();
        GroupePossession possessionsDu3Juillet = new GroupePossession("possessions du 3 Juillet", AU_3_JUILLET_2024, possessionsDu3Juillet2024(ordinateur, vetements, especes, compteBancaire));
        GroupePossession possessionsRajouteesLe18Septembre = new GroupePossession("possessions ajoutées le 18 Septembre 2024", AU_18_SEPTEMBRE_2024, possessionsRajouteesLe18Septembre2024(compteBancaire));

        return new Patrimoine("zety au 18 Septembre 2024", zety, AU_18_SEPTEMBRE_2024, Set.of(possessionsDu3Juillet, possessionsRajouteesLe18Septembre))
                .projectionFuture(AU_18_SEPTEMBRE_2024);
    }

    public Argent argentEnEspecesDeZetyEn20242025() {
        var zety = new Personne("zety");
        var ordinateur = ordinateur();
        var vetements = vetements();
        var especes = especes();
        var compteBancaire = compteBancaire();
        new GroupePossession("possessions du 3 Juillet", AU_3_JUILLET_2024, possessionsDu3Juillet2024(ordinateur, vetements, especes, compteBancaire));
        new GroupePossession("possessions ajoutées le 18 Septembre 2024", AU_18_SEPTEMBRE_2024, possessionsRajouteesLe18Septembre2024(compteBancaire));
        new GroupePossession("possessions ajoutées après le 18 Septembre", AU_18_SEPTEMBRE_2024, possessionsRajouteesApresLe18Septembre2024(especes, compteBancaire));

        return especes;
    }
}
