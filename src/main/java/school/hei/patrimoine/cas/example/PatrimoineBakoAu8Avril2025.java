package school.hei.patrimoine.cas.example;

import static java.time.Month.APRIL;
import static java.time.Month.DECEMBER;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class PatrimoineBakoAu8Avril2025 implements Supplier<Patrimoine> {

  public static final LocalDate AU_8_AVRIL_2025 = LocalDate.of(2025, APRIL, 8);
  public static final LocalDate AU_31_DECEMBRE_2025 = LocalDate.of(2025, DECEMBER, 31);

  private Compte compteBNI() {
    return new Compte("Compte courant BNI", AU_8_AVRIL_2025, ariary(2_000_000));
  }

  private Compte compteBMOI() {
    return new Compte("Compte épargne BMOI", AU_8_AVRIL_2025, ariary(625_000));
  }

  private Compte coffreMaison() {
    return new Compte("Coffre fort à la maison", AU_8_AVRIL_2025, ariary(1_750_000));
  }

  private Materiel ordinateur() {
    return new Materiel(
        "Ordinateur portable", AU_8_AVRIL_2025, AU_8_AVRIL_2025, ariary(3_000_000), -0.12);
  }

  private Set<Possession> possessionsDeBako() {
    var compteBNI = compteBNI();
    var compteBMOI = compteBMOI();
    var coffreMaison = coffreMaison();
    var ordi = ordinateur();

    // Salaire mensuel le 2 du mois
    new FluxArgent(
        "Salaire mensuel",
        compteBNI,
        LocalDate.of(2025, 5, 2),
        AU_31_DECEMBRE_2025,
        2,
        ariary(2_125_000));

    // Épargne automatique de 200 000 Ar depuis BNI vers BMOI chaque 3 du mois
    new FluxArgent(
        "Épargne mensuelle",
        compteBMOI,
        LocalDate.of(2025, 5, 3),
        AU_31_DECEMBRE_2025,
        3,
        ariary(200_000));
    new FluxArgent(
        "Épargne mensuelle - ponction BNI",
        compteBNI,
        LocalDate.of(2025, 5, 3),
        AU_31_DECEMBRE_2025,
        3,
        ariary(-200_000));

    // Dépenses mensuelles (vie courante) le 1er du mois
    new FluxArgent(
        "Dépenses vie courante",
        compteBNI,
        LocalDate.of(2025, 5, 1),
        AU_31_DECEMBRE_2025,
        1,
        ariary(-700_000));

    // Loyer payé tous les 26 du mois
    new FluxArgent(
        "Loyer colocation",
        compteBNI,
        LocalDate.of(2025, 4, 26),
        AU_31_DECEMBRE_2025,
        26,
        ariary(-600_000));

    return Set.of(compteBNI, compteBMOI, coffreMaison, ordi);
  }

  @Override
  public Patrimoine get() {
    var bako = new Personne("bako");
    var possessionsInitiales = possessionsDeBako();
    return Patrimoine.of(
            "Patrimoine de Bako au 8 avril 2025", MGA, AU_8_AVRIL_2025, bako, possessionsInitiales)
        .projectionFuture(AU_31_DECEMBRE_2025);
  }
}
