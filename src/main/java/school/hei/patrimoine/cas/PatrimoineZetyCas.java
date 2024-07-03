package school.hei.patrimoine.cas;

import static java.time.Month.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

public class PatrimoineZetyCas implements Supplier<Patrimoine> {

  @Override
  public Patrimoine get() {
    var zety = new Personne("Zety");
    var au03Juillet2024 = LocalDate.of(2024, JULY, 03);

    var ordinateur = new Materiel("Ordinateur", au03Juillet2024, 1_200_000, au03Juillet2024, -0.1);
    var vetements = new Materiel("Garde-Robe", au03Juillet2024, 1_500_000, au03Juillet2024, -0.5);

    var espece = new Argent("Espèces", au03Juillet2024, 800_000);
    var debutFraisScolarite = LocalDate.of(2023, NOVEMBER, 01);
    var finFraisDeScolarite = LocalDate.of(2024, AUGUST, 28);
    var fraisDeScolarite =
        new FluxArgent(
            "Frais de scolarité", espece, debutFraisScolarite, finFraisDeScolarite, -200_000, 27);

    var compteBancaire = new Argent("Compte Bancaire", au03Juillet2024, 100_000);
    var fraisDeTenueDeCompte =
        new FluxArgent(
            "Frais de tenue de compte",
            compteBancaire,
            au03Juillet2024,
            LocalDate.MAX,
            -20_000,
            25);

    return new Patrimoine(
        "Zety",
        zety,
        au03Juillet2024,
        Set.of(
            ordinateur, vetements, espece, fraisDeScolarite, compteBancaire, fraisDeTenueDeCompte));
  }
}
