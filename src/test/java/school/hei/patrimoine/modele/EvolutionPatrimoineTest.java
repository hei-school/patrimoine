package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EvolutionPatrimoineTest {

  @Test
  void patrimoine_evolue() {
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

    var evolutionPatrimoine = new EvolutionPatrimoine(
        "Nom",
        patrimoineIloAu13mai24,
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2024, MAY, 17));

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
    assertEquals(0, evolutionJournaliere.get(LocalDate.of(2024, MAY, 12)).getValeurComptable());
    assertEquals(600_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 13)).getValeurComptable());
    assertEquals(600_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 14)).getValeurComptable());
    assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 15)).getValeurComptable());
    assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 16)).getValeurComptable());
    assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 17)).getValeurComptable());
  }

  @Test
  public void testValeurPatrimoineZety() {
    LocalDate dateActuelle = LocalDate.of(2024, 7, 3);
    LocalDate dateFuture = LocalDate.of(2025, 2, 14);

    Patrimoine zetyPatrimoine = getPatrimoine(dateActuelle);

    EvolutionPatrimoine evolutionPatrimoine = new EvolutionPatrimoine("Evolution de Patrimoine de Zety", zetyPatrimoine, dateActuelle, dateFuture);

    // Obtenir la série des valeurs comptables du patrimoine de Zety jusqu'à la date spécifiée
    List<Integer> serieValeursComptablesPatrimoine = evolutionPatrimoine.serieValeursComptablesPatrimoine();

    // Calculer la valeur totale du patrimoine de Zety le 14 février 2025
    int valeurPatrimoine = serieValeursComptablesPatrimoine.get(serieValeursComptablesPatrimoine.size() - 1);

    // Valeur attendue
    int valeurAttendue = 2521314;

    // Vérifier que la valeur calculée correspond à la valeur attendue
    assertEquals(valeurAttendue, valeurPatrimoine);
  }

  private static Patrimoine getPatrimoine(LocalDate dateActuelle) {
    Materiel ordinateur = new Materiel("Ordinateur", dateActuelle, 1200000, dateActuelle, -10.0 / 100);
    Materiel vetements = new Materiel("Vêtements", dateActuelle, 1500000, dateActuelle, -50.0 / 100);
    Argent especes = new Argent("Argent en espèces", dateActuelle, 800000);
    Argent compteBancaire = new Argent("Compte bancaire", dateActuelle, 100000);
    FluxArgent fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte",
            compteBancaire,
            dateActuelle,
            LocalDate.of(2999, 12, 31),
            -20000,
            25);
    FluxArgent fraisScolarite = new FluxArgent(
            "Frais de scolarité",
            especes,
            LocalDate.of(2023, 11, 1),
            LocalDate.of(2024, 8, 31),
            -200000,
            27);

    Set<Possession> possessions = Set.of(ordinateur, vetements, especes, compteBancaire, fraisTenueCompte, fraisScolarite);
    Patrimoine zetyPatrimoine = new Patrimoine("Patrimoine de Zety", new Personne("Zety"), dateActuelle, possessions);
    return zetyPatrimoine;
  }
}