package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EvolutionPatrimoineTest {

  @Test
  void diminution_patrimoine_de_Zety_entre_17_et_18_septembre_2024() {
    // Création de Zety avec son patrimoine au 17 septembre 2024
    var zety = new Personne("Zety");

    // Date du test : 17 et 18 septembre 2024
    var au17sept24 = LocalDate.of(2024, SEPTEMBER, 17);
    var au18sept24 = LocalDate.of(2024, SEPTEMBER, 18);

    // Argent en espèces avant le prêt
    var initialCash = new Argent("Espèces", au17sept24, 800_000);

    // Compte bancaire avant le prêt
    var initialBankAccount = new Argent("Compte bancaire", au17sept24, 100_000);

    // Créer un patrimoine au 17 septembre 2024
    var patrimoineZetyAu17sept24 = new Patrimoine(
            "Patrimoine de Zety au 17 septembre 2024",
            zety,
            au17sept24,
            Set.of(initialCash, initialBankAccount));

    // Ajouter le flux d'argent du prêt au 18 septembre 2024
    var bankAccountAfterLoan = new Argent("Compte bancaire", au18sept24, 10_100_000);

    // Créer la dette
    var loanDebt = new Dette("Dette envers la banque", au18sept24, 11_000_000, au18sept24.plusYears(1));

    // Créer un patrimoine au 18 septembre 2024 avec la classe EvolutionPatrimoine
    var evolutionPatrimoine = new EvolutionPatrimoine(
            "Evolution du patrimoine de Zety",
            patrimoineZetyAu17sept24,
            au17sept24,
            au18sept24);

    // Calculer la diminution du patrimoine à l'aide de la méthode série de valeurs comptables du patrimoine
    var serieValeursComptables = evolutionPatrimoine.serieValeursComptablesPatrimoine();
    int valeurPatrimoine17Sept24 = serieValeursComptables.get(0);
    int valeurPatrimoine18Sept24 = serieValeursComptables.get(1);
    int diminutionPatrimoine = valeurPatrimoine17Sept24 - valeurPatrimoine18Sept24;

    // Valeur attendue : -1 000 000 Ar (coût du prêt)
    assertEquals(-1_000_000, diminutionPatrimoine,
            "La diminution du patrimoine de Zety entre le 17 et le 18 septembre 2024 devrait être de 1 000 000 Ar.");
  }
}
