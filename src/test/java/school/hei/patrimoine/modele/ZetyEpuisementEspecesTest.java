package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZetyEpuisementEspecesTest {

  @Test
  void date_a_partir_de_laquelle_zety_n_a_plus_d_especes() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);


    var argentEnEspeces = new Argent("Espèces", au3juillet24, 800_000);
    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);

   
    var donMensuelParentsDeZety = new TransfertArgent(
        "Don des parents",
        new Argent("Compte parents", au3juillet24, 0),
        argentEnEspeces,
        LocalDate.of(2024, JANUARY, 15),
        LocalDate.MAX,
        100_000,
        15);

    var fraisScolarite = new TransfertArgent(
        "Frais de scolarité 2024-2025",
        compteBancaire,
        new Argent("Université", au3juillet24, 0),
        LocalDate.of(2024, SEPTEMBER, 21),
        LocalDate.of(2024, SEPTEMBER, 21),
        2_500_000,
        21);

    var trainDeVieMensuel = new FluxArgent(
        "Train de vie mensuel de loyer, nourriture, transport, revy",
        argentEnEspeces,
        LocalDate.of(2024, OCTOBER, 1),
        LocalDate.of(2025, FEBRUARY, 13),
        -250_000,
        1);

    var patrimoineZety = new Patrimoine(
        "Patrimoine de Zety",
        zety,
        au3juillet24,
        Set.of(argentEnEspeces, compteBancaire, donMensuelParentsDeZety, fraisScolarite, trainDeVieMensuel)
    );
    var dateDeVerification = au3juillet24;
    boolean especesEpuises = false;
    while (dateDeVerification.isBefore(LocalDate.of(2025, MARCH, 1)) && !especesEpuises) {
      var projection = patrimoineZety.projectionFuture(dateDeVerification);
      var valeurEspeces = projection.getValeurComptablePossession("Espèces");

      if (valeurEspeces <= 0) {
        especesEpuises = true;
      } else {
        dateDeVerification = dateDeVerification.plusDays(1);
      }
    }

    assertTrue(especesEpuises, "Zety n'aurai pas d'espèces avant le 1er mars 2025.");
    System.out.println("Date d'épuisement des espèces: " + dateDeVerification);
  }
}

