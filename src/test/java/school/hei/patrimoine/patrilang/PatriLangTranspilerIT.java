package school.hei.patrimoine.patrilang;

import static java.time.Month.APRIL;
import static java.util.Comparator.comparing;
import static org.antlr.v4.runtime.CharStreams.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

public class PatriLangTranspilerIT {
  PatriLangTranspiler subject = new PatriLangTranspiler();
  private static final LocalDate AU_18_AVRIL_2025 = LocalDate.of(2025, APRIL, 18);

  @Test
  void patrimoine_without_possession_ok() {
    var expected = patrimoineWithoutPossessions();
    var input = fromString(SECTION_GENERAL);

    var actual = subject.apply(input);

    assertEquals(expected.getT(), actual.getT());
    assertEquals(expected.getNom(), actual.getNom());
    assertEquals(expected.getDevise(), actual.getDevise());
    assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
    assertEquals(expected.getPossessions(), actual.getPossessions());
  }

  @Test
  void patrimoine_with_trésorier_ok() {
    var expected = patrimoineWithTrésoriers();
    var input = fromString(SECTION_GENERAL + SECTION_TRÉSORIER);

    var actual = subject.apply(input);

    assertEquals(expected.getT(), actual.getT());
    assertEquals(expected.getNom(), actual.getNom());
    assertEquals(expected.getDevise(), actual.getDevise());
    assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
    assertEquals(
        sortPossessions(expected.getPossessions()), sortPossessions(actual.getPossessions()));
  }

  @Test
  void patrimoine_with_créance_ok() {
    var expected = patrimoineWithCréances();
    var input = fromString(SECTION_GENERAL + SECTION_CREANCE);

    var actual = subject.apply(input);

    assertEquals(expected.getT(), actual.getT());
    assertEquals(expected.getNom(), actual.getNom());
    assertEquals(expected.getDevise(), actual.getDevise());
    assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
    assertEquals(
        sortPossessions(expected.getPossessions()), sortPossessions(actual.getPossessions()));
  }

  @Test
  void patrimoine_with_dettes_ok() {
    var expected = patrimoineWithDettes();
    var input = fromString(SECTION_GENERAL + SECTION_DETTE);

    var actual = subject.apply(input);

    assertEquals(expected.getT(), actual.getT());
    assertEquals(expected.getNom(), actual.getNom());
    assertEquals(expected.getDevise(), actual.getDevise());
    assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
    assertEquals(
        sortPossessions(expected.getPossessions()), sortPossessions(actual.getPossessions()));
  }

  @Test
  void patrimoine_with_operations_ok() {
    var expected = patrimoineWithTrésorierEtOpérations();
    var input = fromString(SECTION_GENERAL + SECTION_TRÉSORIER + SECTION_OPERATION);

    var actual = subject.apply(input);

    assertEquals(expected.getT(), actual.getT());
    assertEquals(expected.getNom(), actual.getNom());
    assertEquals(expected.getDevise(), actual.getDevise());
    assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
    assertEquals(
        sortPossessions(expected.getPossessions()), sortPossessions(actual.getPossessions()));
  }

  List<Possession> sortPossessions(Set<Possession> possessions) {
    return possessions.stream().sorted(comparing(Possession::nom)).collect(Collectors.toList());
  }

  Compte findCompteByNom(String nom, Set<Compte> comptes) {
    return comptes.stream().filter(compte -> compte.nom().equals(nom)).findFirst().orElseThrow();
  }

  Patrimoine patrimoineWithoutPossessions() {
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), Set.of());
  }

  Patrimoine patrimoineWithTrésoriers() {
    Set<Possession> possessions = new HashSet<>(trésoriers());
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), possessions);
  }

  Patrimoine patrimoineWithCréances() {
    Set<Possession> possessions = new HashSet<>(créances());
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), possessions);
  }

  Patrimoine patrimoineWithDettes() {
    Set<Possession> possessions = new HashSet<>(dettes());
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), possessions);
  }

  Patrimoine patrimoineWithTrésorierEtOpérations() {
    Set<Possession> possessions = new HashSet<>();
    var comptes = trésoriers();
    possessions.addAll(comptes);
    possessions.addAll(opérations((nom) -> findCompteByNom(nom, comptes)));

    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), possessions);
  }

  Set<Compte> trésoriers() {
    return Set.of(
        new Compte("BMOI", AU_18_AVRIL_2025, ariary(15_000)),
        new Compte("BNI", AU_18_AVRIL_2025, ariary(15_000)));
  }

  Set<Creance> créances() {
    return Set.of(
        new Creance("Myriade_Fr", AU_18_AVRIL_2025, ariary(5_000)),
        new Creance("FanoCréance", AU_18_AVRIL_2025, ariary(3_000)));
  }

  Set<Dette> dettes() {
    return Set.of(
        new Dette("DetteA", AU_18_AVRIL_2025, ariary(-5_000)),
        new Dette("DetteB", AU_18_AVRIL_2025, ariary(-3_000)));
  }

  Set<Possession> opérations(Function<String, Compte> compteGetter) {
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

  private static final String SECTION_GENERAL =
      """
          # Général
          * Spécifié le 18 du 04-2025
          * Patrimoine de Zety
          * Devise en Ar
      """;

  private static final String SECTION_TRÉSORIER =
      """
          # Trésoreries
          * BMOI, valant 15000Ar le 18 du 04-2025
          * BNI, valant 15000Ar le 18 du 04-2025
      """;

  private static final String SECTION_CREANCE =
      """
          # Créances
          * Myriade_Fr, valant 5000Ar le 18 du 04-2025
          * FanoCréance, valant 3000Ar le 18 du 04-2025
      """;

  private static final String SECTION_DETTE =
      """
          # Dettes
          * DetteA, valant 5000Ar le 18 du 04-2025
          * DetteB, valant 3000Ar le 18 du 04-2025
      """;
  private static final String SECTION_OPERATION =
      """
    # Opérations
    * `PossèdeOrdinateur` Le 18 du 04-2025, je possède ordinateur, valant 200000Ar, se dépréciant annuellement de 10%
    * `PossèdeTerrain` Le 18 du 04-2025, je possède terrain, valant 150000Ar, s'appréciant annuellement de 5%
    * `AchatDeVilla` Le 18 du 04-2025, je acheter villa, valant 150000Ar, s'appréciant annuellement de 5%, depuis BNI
    * `PourAiderMonAmi` Le 18 du 04-2025, je sors 8000Ar depuis BMOI
    * `Prime` Le 18 du 04-2025, je entrer 100000Ar vers BNI
    * `TransfertÉpargne` Le 18 du 04-2025, je transférer 100000Ar depuis BMOI vers BNI
""";
}
