package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyTest {
    @Test
    void patrimoine_de_zety() {
        var Zety = new Personne("Zety");
        var au03juillet2024 = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("ordinateur", au03juillet2024, 1_200_000, au03juillet2024, -0.10);

        var vetements = new Materiel("vêtements", au03juillet2024, 1_500_000, au03juillet2024, -0.50);

        var argentEspeces = new Argent("argent en espèces", au03juillet2024, 800_000);

        int fraisScolariteTotal = 0;
        LocalDate debutScolarite = LocalDate.of(2024, JULY, 27);
        LocalDate finScolarite = LocalDate.of(2024, AUGUST, 27);
        LocalDate datePaiement = debutScolarite;
        while (!datePaiement.isAfter(finScolarite)) {
            fraisScolariteTotal += 200_000;
            datePaiement = datePaiement.plusMonths(1);
        }
        var fraisScolarite = new Argent("frais de scolarité", au03juillet2024, fraisScolariteTotal);

        int fraisTenueCompte = 0;
        LocalDate debutCompte = LocalDate.of(2024, JULY, 25);
        LocalDate datePonction = debutCompte;
        while (!datePonction.isAfter(LocalDate.of(2024, SEPTEMBER, 17))) {
            fraisTenueCompte += 20_000;
            datePonction = datePonction.plusMonths(1);
        }

        var compteBancaire = new Argent("compte bancaire", au03juillet2024, 100_000 - fraisTenueCompte);

        var au17septembre2024 = LocalDate.of(2024, SEPTEMBER, 17);

        var patrimoine_de_zety = new Patrimoine(
                "patrimoineDeZety",
                Zety, au17septembre2024,
                Set.of(
                        ordinateur, vetements, compteBancaire, fraisScolarite
                ));

        System.out.println(ordinateur.projectionFuture(au17septembre2024).getValeurComptable());
        /*assertEquals(1_080_000, vetements.valeurComptableFuture(au17septembre2024));
        assertEquals(400_000, fraisScolarite.valeurComptableFuture(au17septembre2024));
        assertEquals(40_000, compteBancaire.valeurComptableFuture(au17septembre2024));*/
        assertEquals(2_978_848, patrimoine_de_zety.projectionFuture(au17septembre2024).getValeurComptable());
    }

    @Test
    void patrimoine_de_zety_en_dette(){
        Personne Zety = new Personne("Zety");
        var debutDeDette = LocalDate.of(2024, SEPTEMBER, 18);
        var finDeDette = debutDeDette.plusYears(1);
        var au18septembre2024 = LocalDate.of(2024, SEPTEMBER, 18);
        var ZetyArgent = new Argent("argent de zety", au18septembre2024, 10_000_000 );
        var zetyFlux = new FluxArgent("flux d'argent de zety", ZetyArgent, debutDeDette, finDeDette, 83_333, 30);
        var dette = new Dette("empreint pour l'etude", debutDeDette, 1_000_000);
    }
}
