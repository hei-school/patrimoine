package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  void patrimoine_zety_17_Septembre() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au27Novembre2023 = LocalDate.of(2023, NOVEMBER, 27);
    var au27Aout2024 = LocalDate.of(2024, AUGUST, 27);
    var au25Juillet2024 = LocalDate.of(2024, JULY, 25);
    var auDateIndeterminee = LocalDate.of(2024, JULY, 25);

    var financeur = new Argent("Espèces", au3juillet24, 800_000);
    var financeur2 = new Argent("Compte bancaire", au3juillet24, 100_000);

    var ordinateur = new AchatMaterielAuComptant("Ordinateur", au3juillet24, 1_200_000, -0.1, financeur);
    var vetement = new AchatMaterielAuComptant("Vêtements", au3juillet24, 1_500_000, -0.5, financeur);

    var fraisDeScolarite = new FluxArgent("Frais de scolarité", financeur, au27Novembre2023, au27Aout2024, -200_000, 30);
    var fraisTenueCompte = new FluxArgent("Frais tenue de compte", financeur2, au25Juillet2024, auDateIndeterminee, -20_000, 30);

    var patrimoineZetyAu3Juillet2024 = new Patrimoine(
            "patrimoineZety2024",
            zety,
            au3juillet24,
            Set.of(financeur, financeur2, ordinateur, vetement, fraisDeScolarite, fraisTenueCompte)
    );

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "Zety",
            patrimoineZetyAu3Juillet2024,
            LocalDate.of(2024, JULY, 3),
            LocalDate.of(2024, SEPTEMBER, 17)
    );

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();

    assertEquals(900_000, evolutionJournaliere.get(LocalDate.of(2024, JULY, 3)).getValeurComptable());
    assertEquals(518_848, evolutionJournaliere.get(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable());
  }

  @Test
  void patrimoine_zety_17_18_Septembre() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au27Novembre2023 = LocalDate.of(2023, NOVEMBER, 27);
    var au27Aout2024 = LocalDate.of(2024, AUGUST, 27);
    var au25Juillet2024 = LocalDate.of(2024, JULY, 25);
    var auDateIndeterminee = LocalDate.of(2024, SEPTEMBER, 18);
    var au18Septembre2024 = LocalDate.of(2024, SEPTEMBER, 18);


    var financeur = new Argent("Espèces", au3juillet24,800_000);
    var financeur2 = new Argent("Compte bancaire",au3juillet24, 100_000);

    var ordinateur = new AchatMaterielAuComptant("Ordinateur", au3juillet24, 1_200_000, -0.1, financeur);
    var vetement = new AchatMaterielAuComptant("Vêtements", au3juillet24, 1_500_000, -0.5, financeur);

    var fraisDeScolarite = new FluxArgent("Frais de scolarité", financeur, au27Novembre2023, au27Aout2024, -200_000, 30);
    var fraisTenueCompte = new FluxArgent("Frais tenue de compte", financeur2, au25Juillet2024, auDateIndeterminee, -20_000, 30);
    var loan = new FluxArgent("Prêt bancaire", financeur2, au18Septembre2024, au18Septembre2024, 10_000_000, 1);
    var debt = new Dette("Dette envers la banque", au18Septembre2024, -11_000_000);

    var patrimoineZetyAu3Juillet2024 = new Patrimoine(
            "patrimoineZety2024",
            zety,
            au3juillet24,
            Set.of(financeur, financeur2, ordinateur, vetement, fraisDeScolarite, fraisTenueCompte, loan, debt)
    );

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "Zety",
            patrimoineZetyAu3Juillet2024,
            LocalDate.of(2024, SEPTEMBER, 17),
            LocalDate.of(2024, SEPTEMBER, 18)
    );

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();

    assertEquals(478_848, evolutionJournaliere.get(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable());
    assertEquals(-10_483_536, evolutionJournaliere.get(LocalDate.of(2024, SEPTEMBER, 18)).getValeurComptable());
  }

  @Test
  void zety_sans_especes_en_futur() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au27Novembre2023 = LocalDate.of(2023, NOVEMBER, 27);
    var au27Aout2024 = LocalDate.of(2024, AUGUST, 27);
    var au25Juillet2024 = LocalDate.of(2024, JULY, 25);
    var auDateIndeterminee = LocalDate.of(2025, DECEMBER, 25);
    var au21Septembre2024 = LocalDate.of(2024, SEPTEMBER, 21);
    var au1Octobre2024 = LocalDate.of(2024, OCTOBER, 1);
    var au13Fevrier2025 = LocalDate.of(2025, FEBRUARY, 13);

    var financeur = new Argent("Espèces", au3juillet24, 800_000);
    var financeur2 = new Argent("Compte bancaire", au3juillet24, 100_000);

    var ordinateur = new AchatMaterielAuComptant("Ordinateur", au3juillet24, 1_200_000, -0.1, financeur);
    var vetement = new AchatMaterielAuComptant("Vêtements", au3juillet24, 1_500_000, -0.5, financeur);

    var fraisDeScolarite = new FluxArgent("Frais de scolarité", financeur, au27Novembre2023, au27Aout2024, -200_000, 30);
    var fraisTenueCompte = new FluxArgent("Frais tenue de compte", financeur2, au25Juillet2024, auDateIndeterminee, -20_000, 30);

    var patrimoineZetyAu3Juillet2024 = new Patrimoine(
            "patrimoineZety2024",
            zety,
            au3juillet24,
            Set.of(financeur, financeur2, ordinateur, vetement, fraisDeScolarite, fraisTenueCompte)
    );

    var paiementFraisScolarite = new FluxArgent("Paiement frais de scolarité", financeur2, au21Septembre2024, au21Septembre2024, -2_500_000, 0);
    financeur2 = new Argent("Compte bancaire", au21Septembre2024, financeur2.getValeurComptable() + paiementFraisScolarite.getValeurComptable());

    LocalDate dateDebut = LocalDate.of(2024, JANUARY, 15);
    while (dateDebut.isBefore(au13Fevrier2025.plusDays(1))) {
      var transfertMensuel = new FluxArgent("Transfert mensuel des parents", financeur, dateDebut, dateDebut, 100_000, 30);
      financeur = new Argent("Espèces", dateDebut, financeur.getValeurComptable() + transfertMensuel.getValeurComptable());
      dateDebut = dateDebut.plusMonths(1);
    }

    dateDebut = au1Octobre2024;
    while (dateDebut.isBefore(au13Fevrier2025.plusDays(1))) {
      var depenseMensuelle = new FluxArgent("Dépense mensuelle", financeur, dateDebut, dateDebut, -250_000, 1);
      financeur = new Argent("Espèces", dateDebut, financeur.getValeurComptable() + depenseMensuelle.getValeurComptable());
      dateDebut = dateDebut.plusMonths(1);
    }

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "Zety",
            patrimoineZetyAu3Juillet2024,
            LocalDate.of(2024, JULY, 3),
            LocalDate.of(2025, FEBRUARY, 13)
    );

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();

    LocalDate dateEpuisementEspece = LocalDate.of(2024, JULY, 3);
    while (dateEpuisementEspece.isBefore(LocalDate.of(2025, FEBRUARY, 13))) {
      if (evolutionJournaliere.get(dateEpuisementEspece).getValeurComptable() < 0) {
        break;
      }
      dateEpuisementEspece = dateEpuisementEspece.plusDays(1);
    }

    assertEquals(LocalDate.of(2025, FEBRUARY, 13), dateEpuisementEspece);
  }

  @Test
  void patrimoine_zety_14_Fevrier_2025() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au27Novembre2023 = LocalDate.of(2023, NOVEMBER, 27);
    var au27Aout2024 = LocalDate.of(2024, AUGUST, 27);
    var au25Juillet2024 = LocalDate.of(2024, JULY, 25);
    var auDateIndeterminee = LocalDate.of(2025, DECEMBER, 25);
    var au21Septembre2024 = LocalDate.of(2025, FEBRUARY, 14);
    var au1Octobre2024 = LocalDate.of(2024, OCTOBER, 1);
    var au13Fevrier2025 = LocalDate.of(2025, FEBRUARY, 13);

    var financeur = new Argent("Espèces", au3juillet24, 800_000);
    var financeur2 = new Argent("Compte bancaire", au3juillet24, 100_000);

    var ordinateur = new AchatMaterielAuComptant("Ordinateur", au3juillet24, 1_200_000, -0.1, financeur);
    var vetement = new AchatMaterielAuComptant("Vêtements", au3juillet24, 1_500_000, -0.5, financeur);

    var fraisDeScolarite = new FluxArgent("Frais de scolarité", financeur, au27Novembre2023, au27Aout2024, -200_000, 30);
    var fraisTenueCompte = new FluxArgent("Frais tenue de compte", financeur2, au25Juillet2024, auDateIndeterminee, -20_000, 30);

    LocalDate dateDebut = au1Octobre2024;
    while (dateDebut.isBefore(au13Fevrier2025.plusDays(1))) {
      var depenseMensuelle = new FluxArgent("Dépense mensuelle", financeur2, dateDebut, dateDebut, -250_000, 1);
      financeur2 = new Argent("Compte bancaire", dateDebut, financeur2.getValeurComptable() + depenseMensuelle.getValeurComptable());
      dateDebut = dateDebut.plusMonths(1);
    }

    var patrimoineZetyAu3Juillet2024 = new Patrimoine(
            "patrimoineZety2024",
            zety,
            au3juillet24,
            Set.of(financeur, financeur2, ordinateur, vetement, fraisDeScolarite, fraisTenueCompte)
    );

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "Zety",
            patrimoineZetyAu3Juillet2024,
            LocalDate.of(2024, JULY, 3),
            LocalDate.of(2025, FEBRUARY, 14)
    );

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();

    assertEquals(161_314, evolutionJournaliere.get(LocalDate.of(2025, FEBRUARY, 14)).getValeurComptable());
  }
}