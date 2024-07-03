package school.hei.patrimoine.cas.de.zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.*;

public class DePatrimoineDeZetyTest {
  @Test
  public void test_de_patrimoine_de_zety_le_17_septembre_2024(){
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

    var patrimoine = new Patrimoine(
      "patrimoine de zety",
      zety,
      le3Juillet2024,
      new HashSet<>(Set.of(ordinateur, vetement, argent, fraisDeScolarité, compteBancaire))
    );

    var saValeurComptableLeJuin = patrimoine.getValeurComptable();
    var septembre17 = LocalDate.of(2024, SEPTEMBER, 17);
    var saValeurComptableEnSeptembre = patrimoine
      .projectionFuture(septembre17)
      .getValeurComptable();

    assertTrue(saValeurComptableLeJuin > saValeurComptableEnSeptembre);
    assertEquals(2878848, saValeurComptableEnSeptembre);
  }

  @Test
  public void test_de_dette(){
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

    var septembre17 = LocalDate.of(2024, SEPTEMBER, 17);
    var argentDeDette = new Argent("argent de dette", septembre17.plusDays(1), 10_000_000);
    var dette = new FluxArgent(
      "dette",
      argentDeDette,
      septembre17.plusDays(1),
      septembre17.plusYears(1),
      -1_000_000,
      18
    );

    var patrimoine = new Patrimoine(
      "patrimoine de zety",
      zety,
      le3Juillet2024,
      new HashSet<>(Set.of(ordinateur, vetement, argent, fraisDeScolarité, compteBancaire, dette))
    );

    var saValeurComptableLeJuin = patrimoine.getValeurComptable();
    var saValeurComptableEnSeptembre = patrimoine
      .projectionFuture(septembre17)
      .getValeurComptable();

    assertTrue(saValeurComptableLeJuin > saValeurComptableEnSeptembre);
    assertTrue(saValeurComptableEnSeptembre > 1_000_000);
  }
}
