package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.*;

public class PatrimoineDeZety {
  public Patrimoine saPatrimoine(){
      var zety = new Personne("Zety");
      var le3Juillet2024 = LocalDate.of(2024, JULY, 3);

      var ordinateur = new Materiel(
        "ordinateur",
        le3Juillet2024,
        1_200_000,
        le3Juillet2024,
        -0.1
      );

      var vetement = new Materiel(
        "vetement",
        le3Juillet2024,
        1_500_000,
        le3Juillet2024,
        -0.5
      );

      var argent = new Argent("espèce", le3Juillet2024, 800_000);

      var leNovembre2023 = LocalDate.of(2023, NOVEMBER, 1);
      var leAout2024 = LocalDate.of(2024, AUGUST, 31);
      var fraisDeScolarité = new FluxArgent(
        "frais de scolarité 2023-2024",
        argent,
        leNovembre2023,
        leAout2024,
        -200_000,
        25
      );

      var duréeIndéterminé = LocalDate.MAX;

      var compteBancaire = new FluxArgent(
        "compte bancaire",
        argent,
        le3Juillet2024,
        duréeIndéterminé,
        -20_000,
        25
      );

      return new Patrimoine(
        "patrimoine de zety",
        zety,
        le3Juillet2024,
        new HashSet<>(Set.of(ordinateur, vetement, argent, fraisDeScolarité, compteBancaire))
      );
  }
}
