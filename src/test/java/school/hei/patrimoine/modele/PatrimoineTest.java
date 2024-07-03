package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.JULY;
import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        LocalDate.of(2024, MAY, 13),
        Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 = new Patrimoine(
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
  void patrimoine_possede_un_train_de_vie_financé_par_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
        15);

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie));

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_possede_groupe_de_train_de_vie_et_d_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
        15);

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie))));

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  private static Patrimoine getPatrimoineZetyAu3juillet2024(LocalDate au3juillet2024, Personne zety) {
    var ordinateur_de_zety = new Materiel("ordinateurDeZety", au3juillet2024, 1_200_000, au3juillet2024, -0.10);
    var vetement_de_zety = new Materiel("vetements", au3juillet2024, 1_500_000, au3juillet2024, -0.50);
    var argent_en_espece = new Argent("argentDeZety", au3juillet2024, 800_000);
    var ecollage_par_mois = new FluxArgent("ecolage_203_2024", argent_en_espece, LocalDate.of(2023, 11, 1), LocalDate.of(2024, 8, 31), -200_000, 27);
    var compte_bancaire = new Argent("compte bancaire", au3juillet2024, 100_000);
    var compte_bancaire_avec_le_ponctionnement = new FluxArgent("ponctionnement", compte_bancaire, au3juillet2024, LocalDate.of(9999, 12, 31), -20_000, 25);
    return new Patrimoine("patrimoine de zety", zety, au3juillet2024, Set.of(ordinateur_de_zety, vetement_de_zety, argent_en_espece, ecollage_par_mois, compte_bancaire, compte_bancaire_avec_le_ponctionnement));
  }

  @Test
  void patrimoine_zety_au_3_juillet_2024_jusquau_17_septembre_2024() {
    var zety = new Personne("Zety");
    var au3juillet2024 = LocalDate.of(2024, 7, 3);
    var patrimoine_zety_au_3juillet2024 = getPatrimoineZetyAu3juillet2024(au3juillet2024, zety);
    assertEquals(3_600_000, patrimoine_zety_au_3juillet2024.getValeurComptable());
    var au_17_septembre_2024 = LocalDate.of(2024, 9, 17);
    var patrimoine_zety_au_17_septembre_2024 = patrimoine_zety_au_3juillet2024.projectionFuture(au_17_septembre_2024);
    var valeur_comptable_future = patrimoine_zety_au_17_septembre_2024.getValeurComptable();
    assertTrue(patrimoine_zety_au_3juillet2024.getValeurComptable() > valeur_comptable_future);
    assertEquals(2978848, valeur_comptable_future);
  }

  @Test
  void valeur_comptable_de_patrimoine_zety_apres_emprunt() {
    var zety = new Personne("Zety");
    var au3juillet2024 = LocalDate.of(2024, 7, 3);
    var patrimoine_zety_au_3juillet2024 = getPatrimoineZetyAu3juillet2024(au3juillet2024, zety);
    var au_17_septembre_2024 = LocalDate.of(2024, 9, 17);
    var patrimoine_zety_au_17_septembre_2024 = patrimoine_zety_au_3juillet2024.projectionFuture(au_17_septembre_2024);
    var au_18_septembre_2024 = LocalDate.of(2024, 9, 18);
    var patrimoine_zety_au_18_septembre_2024 = patrimoine_zety_au_17_septembre_2024.projectionFuture(au_18_septembre_2024);
    var argent_ajoute_zety = new Argent("Argent ajoute", au_18_septembre_2024, 10_000_000);
    var dette_zety = new Dette("dette", au_18_septembre_2024, -11_000_000);
    var fluxDArgentAvecDette = new FluxArgent("avec dette", argent_ajoute_zety, au_18_septembre_2024, au_18_septembre_2024.plusYears(1), -1_000_000, 18);
    patrimoine_zety_au_18_septembre_2024.ajouterPossessions(argent_ajoute_zety);
    patrimoine_zety_au_18_septembre_2024.ajouterPossessions(fluxDArgentAvecDette);
    patrimoine_zety_au_18_septembre_2024.ajouterPossessions(dette_zety);
    assertTrue(patrimoine_zety_au_17_septembre_2024.getValeurComptable() > patrimoine_zety_au_18_septembre_2024.getValeurComptable());
    assertEquals(1976685, patrimoine_zety_au_18_septembre_2024.getValeurComptable());
  }


}