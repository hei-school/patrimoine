package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Patrimoine14Fev2025Test {

  @Test
  void patrimoine_de_Zety_au_14_fev_2025() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au14fev25 = LocalDate.of(2025, FEBRUARY, 14);

    var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);

    var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);

    var especes = new Argent("Espèces", au3juillet24, 800_000);

    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);

    for (int month = NOVEMBER.getValue(); month <= AUGUST.getValue(); month++) {
      var dateFrais = LocalDate.of(2023 + (month <= DECEMBER.getValue() ? 0 : 1), month, 27);
      compteBancaire = (Argent) new FluxArgent("Frais de scolarité", compteBancaire, dateFrais, dateFrais.plusDays(1), -200_000, 27)
              .projectionFuture(dateFrais.plusDays(1));
    }

    for (int month = JANUARY.getValue(); month <= FEBRUARY.getValue(); month++) {
      var dateDon = LocalDate.of(2024 + (month <= DECEMBER.getValue() ? 0 : 1), month, 15);
      especes = (Argent) new FluxArgent("Don des parents", new Argent("Temp", dateDon, 100_000), dateDon, dateDon.plusDays(1), 100_000, 15)
              .projectionFuture(dateDon.plusDays(1));
    }

    for (int month = JULY.getValue(); month <= FEBRUARY.getValue(); month++) {
      var dateFrais = LocalDate.of(2024 + (month <= DECEMBER.getValue() ? 0 : 1), month, 25);
      compteBancaire = (Argent) new FluxArgent("Frais de tenue de compte", compteBancaire, dateFrais, dateFrais.plusDays(1), -20_000, 25)
              .projectionFuture(dateFrais.plusDays(1));
    }

    var fraisScolarite = new TransfertArgent("Paiement frais scolarité", compteBancaire, especes, LocalDate.of(2024, SEPTEMBER, 21), LocalDate.of(2024, SEPTEMBER, 21), 2_500_000, 21);
    compteBancaire = (Argent) fraisScolarite.projectionFuture(LocalDate.of(2024, SEPTEMBER, 21));

    for (int month = OCTOBER.getValue(); month <= FEBRUARY.getValue(); month++) {
      var dateTrainDeVie = LocalDate.of(2024 + (month <= DECEMBER.getValue() ? 0 : 1), month, 1);
      especes = (Argent) new FluxArgent("Train de vie", especes, dateTrainDeVie, dateTrainDeVie.plusDays(1), -250_000, 1)
              .projectionFuture(dateTrainDeVie.plusDays(1));
    }

    var dateDette = LocalDate.of(2024, SEPTEMBER, 18);
    compteBancaire = (Argent) new FluxArgent("Ajout dette", compteBancaire, dateDette, dateDette.plusDays(1), 10_000_000, 18)
            .projectionFuture(dateDette.plusDays(1));
    var dette = new Dette("Dette pour frais de scolarité", dateDette, -11_000_000);

    var patrimoineZety = new Patrimoine(
            "Patrimoine de Zety au 3 juillet 2024",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, especes, compteBancaire, dette)
    );

    var patrimoineZetyFutur = patrimoineZety.projectionFuture(au14fev25);

    var expectedValue = patrimoineZetyFutur.getValeurComptable();

    assertEquals(expectedValue, patrimoineZetyFutur.getValeurComptable());
  }
}

