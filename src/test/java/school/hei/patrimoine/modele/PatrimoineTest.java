package school.hei.patrimoine.modele;

import static java.time.Month.*;
import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 =
        new Patrimoine("patrimoineIloAu13mai24", ilo, LocalDate.of(2024, MAY, 13), Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 =
        new Patrimoine(
            "patrimoineIloAu13mai24",
            ilo,
            au13mai24,
            Set.of(
                new Argent("Espèces", au13mai24, 400_000),
                new Argent("Compte epargne", au13mai24, 200_000),
                new Argent("Compte courant", au13mai24, 600_000)));

    assertEquals(1_200_000, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_possede_un_train_de_vie_finance_par_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            -100_000,
            15);

    var patrimoineIloAu13mai24 =
        new Patrimoine("patrimoineIloAu13mai24", ilo, au13mai24, Set.of(financeur, trainDeVie));

    assertEquals(
        500_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_possede_groupe_de_train_de_vie_et_d_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 = getPatrimoine(au13mai24, ilo);

    assertEquals(
        500_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_de_zety_le_17_sep_24() {
    var zety = new Personne("Zety");
    var au3Juillet24 = LocalDate.of(2024, JULY, 3);

    var argentEnEspecesAu3Juillet24 = new Argent("Espèces", au3Juillet24, 800_000);
    var argentEnBanqueAu3Juillet24 = new Argent("Compte en Banque", au3Juillet24, 100_000);

    var novembre23 = LocalDate.of(2023, NOVEMBER, 1);
    var aout24 = LocalDate.of(2024, AUGUST, 28);

    var au17Septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

    var patrimoineZetyAu3juillet24 =
        new Patrimoine(
            "patrimoineZetyAu13mai24",
            zety,
            au3Juillet24,
            Set.of(
                argentEnEspecesAu3Juillet24,
                argentEnBanqueAu3Juillet24,
                new FluxArgent(
                    "Frais de scolarité",
                    argentEnEspecesAu3Juillet24,
                    novembre23,
                    aout24,
                    -200_000,
                    27),
                new FluxArgent(
                    "Frais de tenue du compte",
                    argentEnBanqueAu3Juillet24,
                    au3Juillet24,
                    au17Septembre24,
                    -20_000,
                    25),
                new Materiel("Ordinateur", au3Juillet24, 1_200_000, null, -0.10),
                new Materiel("Vêtements", au3Juillet24, 1_500_000, null, -0.50)));

    var valeurComptablePatrimoineZetyAu3juillet24 = patrimoineZetyAu3juillet24.getValeurComptable();
    var valeurComptablePatrimoineZetyAu17Septembre24 =
        patrimoineZetyAu3juillet24.projectionFuture(au17Septembre24).getValeurComptable();

    assertTrue(
        valeurComptablePatrimoineZetyAu3juillet24 > valeurComptablePatrimoineZetyAu17Septembre24);
  }

  @Test
  void diminution_patrimoine_zety_entre_17_au_18_sep_24() {
    var zety = new Personne("Zety");
    var au3Juillet24 = LocalDate.of(2024, JULY, 3);

    var argentEnEspecesAu3Juillet24 = new Argent("Espèces", au3Juillet24, 800_000);
    var argentEnBanqueAu3Juillet24 = new Argent("Compte en Banque", au3Juillet24, 100_000);

    var novembre23 = LocalDate.of(2023, NOVEMBER, 1);
    var aout24 = LocalDate.of(2024, AUGUST, 28);

    var au17Septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
    var au18Septembre24 = au17Septembre24.plusDays(1);

    var patrimoineZetyAu3juillet24 =
        new Patrimoine(
            "patrimoineZetyAu13mai24",
            zety,
            au3Juillet24,
            Set.of(
                argentEnEspecesAu3Juillet24,
                argentEnBanqueAu3Juillet24,
                new FluxArgent(
                    "Frais de scolarité",
                    argentEnEspecesAu3Juillet24,
                    novembre23,
                    aout24,
                    -200_000,
                    27),
                new FluxArgent(
                    "Frais de tenue du compte",
                    argentEnBanqueAu3Juillet24,
                    au3Juillet24,
                    au17Septembre24,
                    -20_000,
                    25),
                new FluxArgent(
                    "Dette en banque",
                    argentEnBanqueAu3Juillet24,
                    au18Septembre24,
                    au18Septembre24,
                    10_000_000,
                    18),
                new Materiel("Ordinateur", au3Juillet24, 1_200_000, null, -0.10),
                new Materiel("Vêtements", au3Juillet24, 1_500_000, null, -0.50),
                new Dette("Dette en banque", au18Septembre24, -11_000_000)));

    var valeurComptablePatrimoineZetyAu17Septembre24 =
        patrimoineZetyAu3juillet24.projectionFuture(au17Septembre24).getValeurComptable();
    var valeurComptablePatrimoineZetyAu18Septembre24 =
        patrimoineZetyAu3juillet24.projectionFuture(au18Septembre24).getValeurComptable();

    assertTrue(
        valeurComptablePatrimoineZetyAu18Septembre24
            < valeurComptablePatrimoineZetyAu17Septembre24);
  }

  @Test
  void date_zety_sans_especes() {
    var au3Juillet24 = LocalDate.of(2024, JULY, 3);

    var argentEnEspecesAu3Juillet24 = new Argent("Espèces", au3Juillet24, 800_000);

    var daysToAdd = 0;
    while (argentEnEspecesAu3Juillet24
            .projectionFuture(au3Juillet24.plusDays(daysToAdd))
            .getValeurComptable()
        > 0) {
      daysToAdd++;
    }

    var dateDeFinEspeces = au3Juillet24.plusDays(daysToAdd);
    assertFalse(dateDeFinEspeces.isAfter(au3Juillet24));
  }

  @Test
  void valeur_patrimoine_en_euro() {
    var zety = new Personne("Zety");
    var au3Juillet24 = LocalDate.of(2024, JULY, 3);

    var argentEnEspecesAu3Juillet24 = new Argent("Espèces", au3Juillet24, 800_000);
    var argentEnBanqueAu3Juillet24 = new Argent("Compte en Banque", au3Juillet24, 100_000);

    var novembre23 = LocalDate.of(2023, NOVEMBER, 1);
    var aout24 = LocalDate.of(2024, AUGUST, 28);

    var au17Septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
    var au18Septembre24 = au17Septembre24.plusDays(1);
    var au21Septembre24 = LocalDate.of(2024, SEPTEMBER, 21);

    var debut2024 = LocalDate.of(2024, JANUARY, 1);
    var au1octobre24 = LocalDate.of(2024, OCTOBER, 1);
    var au13fevrier25 = LocalDate.of(2025, FEBRUARY, 13);
    var au14evrier25 = au13fevrier25.plusDays(1);
    var au15fevrier25 = au14evrier25.plusDays(1);

    var au26octobre25 = LocalDate.of(2025, OCTOBER, 26);

    var ariary = new Devise("ariary");
    var euro = new Devise("euro");
    euro.addTauxDeChange(ariary, new TauxDeChange(4021, au3Juillet24));
    ariary.addTauxDeChange(
        euro, new TauxDeChange(euro.from(1, ariary, au3Juillet24, -0.10), au3Juillet24));

    var argentAuDeutscheBank =
        new Argent(
            "Deutsche bank", au15fevrier25, (int) ariary.from(7_000, euro, au15fevrier25, -0.10));

    var patrimoineZetyAu3juillet24 =
        new Patrimoine(
            "patrimoineZetyAu13mai24",
            zety,
            au3Juillet24,
            Set.of(
                argentAuDeutscheBank,
                argentEnEspecesAu3Juillet24,
                argentEnBanqueAu3Juillet24,
                new FluxArgent(
                    "Frais de scolarité",
                    argentEnEspecesAu3Juillet24,
                    novembre23,
                    aout24,
                    -200_000,
                    27),
                new FluxArgent(
                    "Frais de tenue du compte",
                    argentEnBanqueAu3Juillet24,
                    au3Juillet24,
                    au17Septembre24,
                    -20_000,
                    25),
                new FluxArgent(
                    "Dette en banque",
                    argentEnBanqueAu3Juillet24,
                    au18Septembre24,
                    au18Septembre24,
                    10_000_000,
                    18),
                new Materiel("Ordinateur", au3Juillet24, 1_200_000, null, -0.10),
                new Materiel("Vêtements", au3Juillet24, 1_500_000, null, -0.50),
                new Dette("Dette en banque", au18Septembre24, -11_000_000),
                new FluxArgent(
                    "Don parentaux", argentEnEspecesAu3Juillet24, debut2024, null, 100_000, 15),
                new FluxArgent(
                    "Frais de scolarité 2024-2025",
                    argentEnBanqueAu3Juillet24,
                    au21Septembre24,
                    au21Septembre24,
                    -2_500_000,
                    21),
                new FluxArgent(
                    "Train de vie",
                    argentEnEspecesAu3Juillet24,
                    au1octobre24,
                    au13fevrier25,
                    -250_000,
                    1)));

    var valeurComptablePatrimoineZetyAu26octobre25 =
        patrimoineZetyAu3juillet24.valeurComptableFuture(au26octobre25, ariary, euro, -0.10);

    assertTrue(valeurComptablePatrimoineZetyAu26octobre25 > 7000);
  }

  private static Patrimoine getPatrimoine(LocalDate au13mai24, Personne ilo) {
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            -100_000,
            15);

    return new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie))));
  }
}
