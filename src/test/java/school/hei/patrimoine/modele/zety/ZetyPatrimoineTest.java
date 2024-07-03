package school.hei.patrimoine.modele.zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

public class ZetyPatrimoineTest {

    @Test
    void zety_etudie_en_2023_2024(){
        var zety = new Personne("Zety");
        var au07juillet2024 = LocalDate.of(2024, 07, 03);

        var ordinateur = new Materiel("ordinateur", au07juillet2024, 1200000, au07juillet2024, -0.1);
        var vetements = new Materiel("vêtements", au07juillet2024, 1500000, au07juillet2024, -0.5);
        var argent = new Argent("éspèces", au07juillet2024, 800000);
        var fraisScolaire = new FluxArgent(
                "frais de scolarité 2023-2024", argent, LocalDate.of(2023, 11, 01),
                LocalDate.of(2024, 8, 30), 200000, 27);
        var compteBancaire = new Argent("Compte Bancaire", au07juillet2024, 100000);
        var tenueDeCompte = new FluxArgent("Tenue de compte", compteBancaire, au07juillet2024,
                LocalDate.of(2024,8,17), 20000, 25);

        var patrimoineZety = new Patrimoine(
                "patrimoineZety",
                zety,
                au07juillet2024,
                Set.of(ordinateur, vetements, argent, fraisScolaire, compteBancaire, tenueDeCompte));

        System.out.println("Le patrimoine de Zety le 17 septembre 2024 vaut: " +
                patrimoineZety.getValeurComptable());
    }
}
