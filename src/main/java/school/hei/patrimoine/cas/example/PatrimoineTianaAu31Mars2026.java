package school.hei.patrimoine.cas.example;

import static java.time.Month.APRIL;
import static java.time.Month.MARCH;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class PatrimoineTianaAu31Mars2026 implements Supplier<Patrimoine> {

  // Date de départ de la simulation et date de projection finale
  public static final LocalDate AU_8_AVRIL_2025 = LocalDate.of(2025, APRIL, 8);
  public static final LocalDate AU_31_MARS_2026 = LocalDate.of(2026, MARCH, 31);

  // Le compte bancaire initial de Tiana
  private Compte compteBancaire() {
    return new Compte("Compte bancaire", AU_8_AVRIL_2025, ariary(60_000_000));
  }

  // Le terrain bâti, avec une valeur initiale et une appréciation de 10% par an
  private Materiel terrain() {
    return new Materiel(
        "Terrain bâti", AU_8_AVRIL_2025, AU_31_MARS_2026, ariary(100_000_000), 0.10);
  }

  private Set<Possession> possessionsDeTiana() {
    var compte = compteBancaire();
    var terrain = terrain();

    // 1. Dépense mensuelle de 4_000_000 Ar pour loger et nourrir toute la famille
    // On suppose que ces dépenses commencent le 1er mai 2025 et se répètent mensuellement
    new FluxArgent(
        "Dépenses familiales",
        compte,
        LocalDate.of(2025, 5, 1),
        AU_31_MARS_2026,
        1,
        ariary(-4_000_000));

    // 2. Projet entrepreneurial (du 1 juin 2025 au 31 décembre 2025) :
    // Coût mensuel de 5_000_000 Ar prélevé le 5 de chaque mois de juin à décembre 2025
    new FluxArgent(
        "Coût projet entrepreneurial",
        compte,
        LocalDate.of(2025, 6, 5),
        LocalDate.of(2025, 12, 31),
        5,
        ariary(-5_000_000));

    // Recette du projet en deux fois :
    // - 10% du projet (7_000_000 Ar) à encaisser le 1er mai 2025 (1 mois avant le lancement)
    new FluxArgent(
        "Recette projet - 10%",
        compte, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 1), 1, ariary(7_000_000));
    // - 90% du projet (63_000_000 Ar) à encaisser le 31 janvier 2026 (1 mois après la fin)
    new FluxArgent(
        "Recette projet - 90%",
        compte, LocalDate.of(2026, 1, 31), LocalDate.of(2026, 1, 31), 31, ariary(63_000_000));

    // 3. Prêt bancaire et son remboursement :
    // Prêt de 20_000_000 Ar le 27 juillet 2025
    new FluxArgent(
        "Prêt bancaire",
        compte,
        LocalDate.of(2025, 7, 27),
        LocalDate.of(2025, 7, 27),
        27,
        ariary(20_000_000));
    // Remboursement mensuel de 2_000_000 Ar à partir du 27 août 2025, se répétant jusqu'au 31 mars
    // 2026
    new FluxArgent(
        "Remboursement prêt bancaire",
        compte,
        LocalDate.of(2025, 8, 27),
        AU_31_MARS_2026,
        27,
        ariary(-2_000_000));

    return Set.of(compte, terrain);
  }

  @Override
  public Patrimoine get() {
    var tiana = new Personne("Tiana");
    GroupePossession possessions =
        new GroupePossession("Patrimoine de Tiana", MGA, AU_8_AVRIL_2025, possessionsDeTiana());
    return Patrimoine.of("Tiana au 8 avril 2025", MGA, AU_8_AVRIL_2025, tiana, Set.of(possessions))
        .projectionFuture(AU_31_MARS_2026);
  }
}
