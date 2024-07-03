package school.hei.patrimoine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class PatrimoineZetyTest {

  @Test
  void testValeurPatrimoine17_septembre_2024() {
    LocalDate dateReference = LocalDate.of(2024, 7, 3);
    LocalDate dateFuture = LocalDate.of(2024, 9, 17);

    Materiel ordinateur = new Materiel("Ordinateur", dateReference, 1200000, dateReference, -0.10);
    Materiel vetements = new Materiel("Vêtements", dateReference, 1500000, dateReference, -0.50);
    Argent argentEspèces = new Argent("Espèces", dateReference, 800000);

    LocalDate debutScolarite = LocalDate.of(2023, 11, 27);
    LocalDate finScolarite = LocalDate.of(2024, 8, 27);

    LocalDate dateScolarite = debutScolarite;
    while (!dateScolarite.isAfter(finScolarite)) {
      FluxArgent fraisScolarite = new FluxArgent("Frais Scolarité", argentEspèces, dateScolarite, dateScolarite, -200000, 27);
      argentEspèces.addFinancés(fraisScolarite);
      dateScolarite = dateScolarite.plusMonths(1);
    }

    Argent compteBancaire = new Argent("Compte Bancaire", dateReference, 100000);
    FluxArgent fraisCompteBancaire = new FluxArgent("Frais Compte Bancaire", compteBancaire, dateReference, dateFuture, -20000, 25);

    compteBancaire.addFinancés(fraisCompteBancaire);

    Set<Possession> possessions = new HashSet<>();
    possessions.add(ordinateur);
    possessions.add(vetements);
    possessions.add(argentEspèces);
    possessions.add(compteBancaire);

    Patrimoine patrimoine = new Patrimoine("Patrimoine de Zety", null, dateReference, possessions);

    Patrimoine patrimoineFutur = patrimoine.projectionFuture(dateFuture);

    int valeurAttendue = 0;
    valeurAttendue += patrimoineFutur.possessionParNom("Ordinateur").valeurComptableFuture(dateFuture);
    valeurAttendue += patrimoineFutur.possessionParNom("Vêtements").valeurComptableFuture(dateFuture);
    valeurAttendue += patrimoineFutur.possessionParNom("Espèces").valeurComptableFuture(dateFuture);
    valeurAttendue += patrimoineFutur.possessionParNom("Compte Bancaire").valeurComptableFuture(dateFuture);

    assertEquals(valeurAttendue, patrimoineFutur.getValeurComptable());
  }
}
