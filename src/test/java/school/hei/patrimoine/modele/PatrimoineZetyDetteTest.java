package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineZetyDetteTest {

  @Test
  void valeur_patrimoine_de_zety_apres_dette_au_18_septembre_2024() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);

    var ordinateur = new Materiel(
        "Ordinateur",
        au3juillet24,
        1_200_000,
        au3juillet24.minusDays(1),
        -0.10);

    var vetement = new Materiel(
        "Vêtements",
        au3juillet24,
        1_500_000,
        au3juillet24.minusDays(1),
        -0.50);

    var argentEnEspeces = new Argent("Espèces", au3juillet24, 800_000);

    var fraisScolarite = new FluxArgent(
        "Frais de scolarité",
        argentEnEspeces,
        LocalDate.of(2023, NOVEMBER, 27),
        LocalDate.of(2024, AUGUST, 27),
        -200_000,
        1);

    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
    var fraisDeTenueDeCompte = new FluxArgent(
        "Frais de tenue de compte",
        compteBancaire,
        au3juillet24.minusMonths(1),
        LocalDate.MAX,
        -20_000,
        1);

    var patrimoineZety = new Patrimoine(
        "Patrimoine de Zety",
        zety,
        au3juillet24,
        Set.of(ordinateur, vetement, argentEnEspeces, fraisScolarite, compteBancaire, fraisDeTenueDeCompte)
    );

    var au17sept24 = LocalDate.of(2024, SEPTEMBER, 17);
    var valeurAu17Septembre = patrimoineZety.projectionFuture(au17sept24).getValeurComptable();

    var au18sept24 = LocalDate.of(2024, SEPTEMBER, 18);
    var dette = new Dette("Dette envers banque", au18sept24, -11_000_000);
    compteBancaire.addFlux(new FluxArgent("Prêt reçu", compteBancaire, au18sept24, au18sept24, 10_000_000, 1));
    patrimoineZety.addPossession(dette);

    var valeurAu18Septembre = patrimoineZety.projectionFuture(au18sept24).getValeurComptable();

    var diminutionPatrimoineDeZety = valeurAu17Septembre - valeurAu18Septembre;

    assertEquals(1_002_384, diminutionPatrimoineDeZety);
  }
}

