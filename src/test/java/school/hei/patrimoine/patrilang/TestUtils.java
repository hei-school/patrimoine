package school.hei.patrimoine.patrilang;

import static java.time.Month.APRIL;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

class TestUtils {
  static final LocalDate AU_18_AVRIL_2025 = LocalDate.of(2025, APRIL, 18);

  static Compte findCompteByNom(String nom, Set<Compte> comptes) {
    return comptes.stream().filter(compte -> compte.nom().equals(nom)).findFirst().orElseThrow();
  }

  static Patrimoine patrimoineWithoutPossessions() {
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), Set.of());
  }

  static Patrimoine patrimoineWithTrésoriers() {
    Set<Possession> possessions = new HashSet<>(trésoriers());
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), possessions);
  }

  static Patrimoine patrimoineWithCréances() {
    Set<Possession> possessions = new HashSet<>(créances());
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), possessions);
  }

  static Patrimoine patrimoineWithDettes() {
    Set<Possession> possessions = new HashSet<>(dettes());
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), possessions);
  }

  static Patrimoine patrimoineWithTrésorierEtOpérations() {
    Set<Possession> possessions = new HashSet<>();
    var comptes = trésoriers();
    possessions.addAll(comptes);
    possessions.addAll(opérations((nom) -> findCompteByNom(nom, comptes)));

    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), possessions);
  }

  static Patrimoine patrimoineWithTrésorierEtGroupOpérations() {
    Set<Possession> possessions = new HashSet<>();
    var comptes = trésoriers();
    possessions.addAll(comptes);
    possessions.addAll(opérationsWithGroupPossession((nom) -> findCompteByNom(nom, comptes)));

    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), possessions);
  }

  static Set<Compte> trésoriers() {
    return Set.of(
        new Compte("BMOI", AU_18_AVRIL_2025, ariary(15_000)),
        new Compte("BNI", AU_18_AVRIL_2025, ariary(15_000)));
  }

  static Set<Creance> créances() {
    return Set.of(
        new Creance("Myriade_Fr", AU_18_AVRIL_2025, ariary(5_000)),
        new Creance("FanoCréance", AU_18_AVRIL_2025, ariary(3_000)));
  }

  static Set<Dette> dettes() {
    return Set.of(
        new Dette("DetteA", AU_18_AVRIL_2025, ariary(-5_000)),
        new Dette("DetteB", AU_18_AVRIL_2025, ariary(-3_000)));
  }

  static Set<Possession> opérations(Function<String, Compte> compteGetter) {
    return Set.of(
        new Materiel("ordinateur", AU_18_AVRIL_2025, AU_18_AVRIL_2025, ariary(200_000), -0.1),
        new Materiel("terrain", AU_18_AVRIL_2025, AU_18_AVRIL_2025, ariary(150_000), 0.05),
        new AchatMaterielAuComptant(
            "villa", AU_18_AVRIL_2025, ariary(150_000), 0.05, compteGetter.apply("BNI")),
        new FluxArgent(
            "PourAiderMonAmi", compteGetter.apply("BMOI"), AU_18_AVRIL_2025, ariary(-8_000)),
        new FluxArgent("Prime", compteGetter.apply("BNI"), AU_18_AVRIL_2025, ariary(100_000)),
        new TransfertArgent(
            "TransfertÉpargne",
            compteGetter.apply("BMOI"),
            compteGetter.apply("BNI"),
            AU_18_AVRIL_2025,
            ariary(100_000)));
  }

  static Set<Possession> opérationsWithGroupPossession(Function<String, Compte> compteGetter) {
    return Set.of(
        new GroupePossession(
            "Hei",
            MGA,
            AU_18_AVRIL_2025,
            Set.of(
                new Materiel(
                    "ordinateur", AU_18_AVRIL_2025, AU_18_AVRIL_2025, ariary(200_000), -0.1),
                new Materiel("terrain", AU_18_AVRIL_2025, AU_18_AVRIL_2025, ariary(150_000), 0.05),
                new AchatMaterielAuComptant(
                    "villa", AU_18_AVRIL_2025, ariary(150_000), 0.05, compteGetter.apply("BNI")))),
        new GroupePossession(
            "Autre",
            MGA,
            AU_18_AVRIL_2025,
            Set.of(
                new FluxArgent(
                    "PourAiderMonAmi",
                    compteGetter.apply("BMOI"),
                    AU_18_AVRIL_2025,
                    ariary(-8_000)),
                new FluxArgent(
                    "Prime", compteGetter.apply("BNI"), AU_18_AVRIL_2025, ariary(100_000)),
                new TransfertArgent(
                    "TransfertÉpargne",
                    compteGetter.apply("BMOI"),
                    compteGetter.apply("BNI"),
                    AU_18_AVRIL_2025,
                    ariary(100_000)))));
  }

  static final String SECTION_GENERAL =
      """
          # Général
          * Spécifié le 18 du 04-2025
          * Patrimoine de Zety
          * Devise en Ar
      """;

  static final String SECTION_TRÉSORIER =
      """
          # Trésoreries
          * BMOI, valant 15000Ar le 18 du 04-2025
          * BNI, valant 15000Ar le 18 du 04-2025
      """;

  static final String SECTION_CREANCE =
      """
          # Créances
          * Myriade_Fr, valant 5000Ar le 18 du 04-2025
          * FanoCréance, valant 3000Ar le 18 du 04-2025
      """;

  static final String SECTION_DETTE =
      """
          # Dettes
          * DetteA, valant 5000Ar le 18 du 04-2025
          * DetteB, valant 3000Ar le 18 du 04-2025
      """;

  static final String SECTION_OPERATION =
      """
          # Opérations
          * `PossèdeOrdinateur` Le 18 du 04-2025, je possède ordinateur, valant 200000Ar, se dépréciant annuellement de 10%
          * `PossèdeTerrain` Le 18 du 04-2025, je possède terrain, valant 150000Ar, s'appréciant annuellement de 5%
          * `AchatDeVilla` Le 18 du 04-2025, je acheter villa, valant 150000Ar, s'appréciant annuellement de 5%, depuis BNI
          * `PourAiderMonAmi` Le 18 du 04-2025, je sors 8000Ar depuis BMOI
          * `Prime` Le 18 du 04-2025, je entrer 100000Ar vers BNI
          * `TransfertÉpargne` Le 18 du 04-2025, je transférer 100000Ar depuis BMOI vers BNI
      """;

  static final String SECTION_OPERATION_WITH_GROUP_POSSESSION =
      """
          # Opérations
          ## HEI, le 18 du 04-2025, devise en Ar
          * `idA` Le 18 du 04-2025, je possède ordinateur, valant 200000Ar, se dépréciant annuellement de 10%
          * `idB` Le 18 du 04-2025, je possède terrain, valant 150000Ar, s'appréciant annuellement de 5%
          * `idC` Le 18 du 04-2025, je acheter villa, valant 150000Ar, s'appréciant annuellement de 5%, depuis BNI

          ## Autre, le 18 du 04-2025, devise en Ar
          * `idD` Le 18 du 04-2025, je sors 15000.55Ar depuis BMOI
          * `idE` Le 18 du 04-2025, je entrer 100000Ar vers BNI
          * `idF` Le 18 du 04-2025, je transférer 100000Ar depuis BMOI vers BNI
      """;
}
