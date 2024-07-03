package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Set;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EvolutionPatrimoineTest {

  @Test
  void testZetyPatrimoineAu17Septembre2024() {
    LocalDate au3juillet24 = LocalDate.of(2024, Calendar.JULY, 3);
    LocalDate au17septembre24 = LocalDate.of(2024, Calendar.SEPTEMBER, 17);

    Materiel ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
    Materiel vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
    Argent argentEspeces = new Argent("Espèces", au3juillet24, 800_000);
    Argent compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);

    FluxArgent fraisScolarite = new FluxArgent(
            "Frais de scolarité", argentEspeces, LocalDate.of(2023, 11, 27),
            LocalDate.of(2024, 8, 27), -200_000, 27);

    FluxArgent fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte", compteBancaire, au3juillet24,
            LocalDate.of(9999, 12, 31), -20_000, 25);

    double valeurOrdinateur = ordinateur.projectionFuture(au17septembre24).getValeurComptable();
    double valeurVetements = vetements.projectionFuture(au17septembre24).getValeurComptable();
    double valeurArgentEspeces = argentEspeces.projectionFuture(au17septembre24).getValeurComptable();
    double valeurCompteBancaire = compteBancaire.projectionFuture(au17septembre24).getValeurComptable();


    double valeurTotalePatrimoine = valeurOrdinateur + valeurVetements + valeurArgentEspeces + valeurCompteBancaire;


    double valeurOrdinateurAttendue = 1_200_000 * Math.pow(1 - 0.10, LocalDate.of(2024, 7, 3).until(au17septembre24).getDays() / 365.0);
    double valeurVetementsAttendue = 1_500_000 * Math.pow(1 - 0.50, LocalDate.of(2024, 7, 3).until(au17septembre24).getDays() / 365.0);
    double valeurArgentEspecesAttendue = 800_000 - (200_000 * 10); // Calcul des frais de scolarité
    double valeurCompteBancaireAttendue = 100_000 - (20_000 * 3); // Calcul des frais de tenue de compte

    double expectedValue = valeurOrdinateurAttendue + valeurVetementsAttendue + valeurArgentEspecesAttendue + valeurCompteBancaireAttendue;

    // Assertion avec une marge de tolérance de 0.01
    assertEquals(expectedValue, valeurTotalePatrimoine, 0.01);
}
}