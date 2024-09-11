package school.hei.patrimoine.cas.zety;

import static java.time.Month.AUGUST;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.NOVEMBER;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Argent.euro;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
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
  public static final LocalDate AU_26_OCTOBRE_2025 = LocalDate.of(2025, OCTOBER, 26);
  public static final LocalDate AU_15_FEVRIER_2025 = LocalDate.of(2024, FEBRUARY, 15);

  private static Set<Possession> possessionsDu3Juillet2024(
      Materiel ordinateur, Materiel vêtements, Compte espèces, Compte compteBancaire) {
    new FluxArgent(
        "scolarité 2023-2024",
        espèces,
        LocalDate.of(2023, NOVEMBER, 1),
        LocalDate.of(2024, AUGUST, 28),
        ariary(-200_000),
        27);
    new FluxArgent(
        "frais de tenue de compte",
        compteBancaire,
        AU_3_JUILLET_2024,
        LocalDate.MAX,
        ariary(-20_000),
        25);
    return Set.of(ordinateur, vêtements, espèces, compteBancaire);
  }

  private static Set<Possession> possessionsRajoutéesLe18Septembre2024(Compte compteBancaire) {
    LocalDate dateDePriseDeffetDette = AU_18_SEPTEMBRE_2024;
    LocalDate dateDeRemboursementDette = AU_18_SEPTEMBRE_2025;
    int valeurDetteARembourser = -11_000_000;
    var dette = new Dette("dette de 10M", dateDePriseDeffetDette, ariary(valeurDetteARembourser));
    new FluxArgent(
        "remboursement de la dette",
        compteBancaire,
        dateDeRemboursementDette,
        dateDeRemboursementDette,
        ariary(valeurDetteARembourser),
        dateDeRemboursementDette.getDayOfMonth());
    int valeurDetteRajoutéeAuCompte = 10_000_000;
    new FluxArgent(
        "flux de la dette",
        compteBancaire,
        dateDePriseDeffetDette,
        dateDePriseDeffetDette,
        ariary(valeurDetteRajoutéeAuCompte),
        dateDePriseDeffetDette.getDayOfMonth());
    return Set.of(dette);
  }

  private static Set<Possession> possessionsRajoutéesAprèsLe18Septembre2024(
      Compte espèces, Compte compteBancaire) {
    LocalDate au21Septembre2024 = LocalDate.of(2024, SEPTEMBER, 21);
    return Set.of(
        new FluxArgent(
            "frais de scolarité 1 fois",
            compteBancaire,
            au21Septembre2024,
            au21Septembre2024,
            ariary(-2_500_000),
            au21Septembre2024.getDayOfMonth()),
        new FluxArgent(
            "dons parentaux",
            espèces,
            LocalDate.of(2024, JANUARY, 1),
            LocalDate.MAX,
            ariary(100_000),
            15),
        new FluxArgent(
            "train de vie mensuel",
            espèces,
            LocalDate.of(2024, OCTOBER, 1),
            LocalDate.of(2025, FEBRUARY, 13),
            ariary(-250_000),
            1));
  }

  private static Set<Possession> possessionsRajoutéesAprèsLe14Février2025() {
    Compte compteAllemand = new Compte("compte bancaire allemand", AU_15_FEVRIER_2025, euro(0));
    int montantPositifDette = 7000;
    Dette dette = new Dette("dette allemande", AU_15_FEVRIER_2025, euro(-montantPositifDette));
    LocalDate datePriseDette = AU_15_FEVRIER_2025;
    new FluxArgent(
        "entrée dette dans compte",
        compteAllemand,
        datePriseDette,
        datePriseDette,
        euro(montantPositifDette),
        datePriseDette.getDayOfMonth());
    LocalDate dateRemboursementDette = AU_15_FEVRIER_2025.plusYears(1);
    new FluxArgent(
        "remboursement dette dans 1 an",
        compteAllemand,
        dateRemboursementDette,
        dateRemboursementDette,
        euro(-montantPositifDette),
        dateRemboursementDette.getDayOfMonth());
    return Set.of(compteAllemand, dette);
  }

  private Compte compteBancaire() {
    return new Compte("compte bancaire argent", AU_3_JUILLET_2024, ariary(100_000));
  }

  private Compte espèces() {
    return new Compte("espèces", AU_3_JUILLET_2024, AU_3_JUILLET_2024, ariary(800_000));
  }

  private Materiel vêtements() {
    return new Materiel("vêtements", AU_3_JUILLET_2024, ariary(1_500_000), AU_3_JUILLET_2024, -0.5);
  }

  private Materiel ordinateur() {
    return new Materiel(
        "ordinateur", AU_3_JUILLET_2024, ariary(1_200_000), AU_3_JUILLET_2024, -0.1);
  }

  @Override
  public Patrimoine get() {
    var zety = new Personne("zety");
    var ordinateur = ordinateur();
    var vêtements = vêtements();
    var espèces = espèces();
    var compteBancaire = compteBancaire();

    Set<Possession> possessionsDu3Juillet =
        possessionsDu3Juillet2024(ordinateur, vêtements, espèces, compteBancaire);
    return Patrimoine.of(
        "zety au 3 juillet 2024", MGA, Set.of(zety), AU_3_JUILLET_2024, possessionsDu3Juillet);
  }

  public Patrimoine zetySendette() {
    var zety = new Personne("zety");
    var ordinateur = ordinateur();
    var vêtements = vêtements();
    var espèces = espèces();
    var compteBancaire = compteBancaire();
    GroupePossession possessionsDu3Juillet =
        new GroupePossession(
            "possessions du 3 Juillet",
            AU_3_JUILLET_2024,
            possessionsDu3Juillet2024(ordinateur, vêtements, espèces, compteBancaire),
            MGA);
    GroupePossession possessionsRajoutéesLe18Septembre =
        new GroupePossession(
            "possessions ajoutées le 18 Septembre 2024",
            AU_18_SEPTEMBRE_2024,
            possessionsRajoutéesLe18Septembre2024(compteBancaire),
            MGA);

    return Patrimoine.of(
            "zety au 18 Septembre 2024",
            MGA,
            Set.of(zety),
            AU_18_SEPTEMBRE_2024,
            Set.of(possessionsDu3Juillet, possessionsRajoutéesLe18Septembre))
        .projectionFuture(AU_18_SEPTEMBRE_2024);
  }

  public Compte argentEnEspècesDeZetyEn20242025() {
    // kept for some sort of pattern in all the getters
    var zety = new Personne("zety");
    var ordinateur = ordinateur();
    var vêtements = vêtements();
    var espèces = espèces();
    var compteBancaire = compteBancaire();
    new GroupePossession(
        "possessions du 3 Juillet",
        AU_3_JUILLET_2024,
        possessionsDu3Juillet2024(ordinateur, vêtements, espèces, compteBancaire),
        MGA);
    new GroupePossession(
        "possessions ajoutées le 18 Septembre 2024",
        AU_18_SEPTEMBRE_2024,
        possessionsRajoutéesLe18Septembre2024(compteBancaire),
        MGA);
    new GroupePossession(
        "possessions ajoutées après le 18 Septembre",
        AU_18_SEPTEMBRE_2024,
        possessionsRajoutéesAprèsLe18Septembre2024(espèces, compteBancaire),
        MGA);

    return espèces;
  }

  public Patrimoine patrimoineDeZetyLe14Fevrier2025() {
    var zety = new Personne("zety");
    var ordinateur = ordinateur();
    var vêtements = vêtements();
    var espèces = espèces();
    var compteBancaire = compteBancaire();
    GroupePossession possessionsDu3Juillet2024 =
        new GroupePossession(
            "possessions du 3 Juillet",
            AU_3_JUILLET_2024,
            possessionsDu3Juillet2024(ordinateur, vêtements, espèces, compteBancaire),
            MGA);
    GroupePossession possessionsRajoutéesLe18Septembre2024 =
        new GroupePossession(
            "possessions ajoutées le 18 Septembre 2024",
            AU_18_SEPTEMBRE_2024,
            possessionsRajoutéesLe18Septembre2024(compteBancaire),
            MGA);
    GroupePossession possessionsRajoutéesAprèsLe18Septembre2024 =
        new GroupePossession(
            "possessions ajoutées après le 18 Septembre",
            AU_18_SEPTEMBRE_2024,
            possessionsRajoutéesAprèsLe18Septembre2024(espèces, compteBancaire),
            MGA);

    return Patrimoine.of(
        "zety au 14 Février 2025",
        MGA,
        Set.of(zety),
        AU_14_FEVRIER_2025,
        Set.of(
            possessionsDu3Juillet2024,
            possessionsRajoutéesLe18Septembre2024,
            possessionsRajoutéesAprèsLe18Septembre2024));
  }

  public Patrimoine patrimoineDeZety26Octobre2025() {
    var zety = new Personne("zety");
    var ordinateur = ordinateur();
    var vêtements = vêtements();
    var espèces = espèces();
    var compteBancaire = compteBancaire();

    // possessions en Ariary
    GroupePossession possessionsDu3Juillet2024 =
        new GroupePossession(
            "possessions du 3 Juillet",
            AU_3_JUILLET_2024,
            possessionsDu3Juillet2024(ordinateur, vêtements, espèces, compteBancaire),
            MGA);
    GroupePossession possessionsRajoutéesLe18Septembre2024 =
        new GroupePossession(
            "possessions ajoutées le 18 Septembre 2024",
            AU_18_SEPTEMBRE_2024,
            possessionsRajoutéesLe18Septembre2024(compteBancaire),
            MGA);
    GroupePossession possessionsRajoutéesAprèsLe18Septembre2024 =
        new GroupePossession(
            "possessions ajoutées après le 18 Septembre",
            AU_18_SEPTEMBRE_2024,
            possessionsRajoutéesAprèsLe18Septembre2024(espèces, compteBancaire),
            MGA);
    // possession en euros
    GroupePossession possessionsRajoutéesAprèsLe14Février2025 =
        new GroupePossession(
            "possessions rajoutées après le 14 Février 2025",
            AU_14_FEVRIER_2025,
            possessionsRajoutéesAprèsLe14Février2025(),
            EUR);

    // patrimoine avec possessions mixtes
    return Patrimoine.of(
        "zety au 26 Octobre 2025",
        MGA,
        Set.of(zety),
        AU_26_OCTOBRE_2025,
        Set.of(
            possessionsDu3Juillet2024,
            possessionsRajoutéesLe18Septembre2024,
            possessionsRajoutéesAprèsLe18Septembre2024,
            possessionsRajoutéesAprèsLe14Février2025));
  }
}
