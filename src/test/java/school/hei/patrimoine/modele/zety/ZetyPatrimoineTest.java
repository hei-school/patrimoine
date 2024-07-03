package school.hei.patrimoine.modele.zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

public class ZetyPatrimoineTest {

    @Test
    void zety(){
        //Zety étudie en 2023-2024
        var zety = new Personne("Zety");
        var au07juillet2024 = LocalDate.of(2024, 07, 07);

        var ordinateur = new Materiel("ordinateur", au07juillet2024, 1200000, au07juillet2024, -0.1);
        var vetements = new Materiel("vêtements", au07juillet2024, 1500000, au07juillet2024, -0.5);
        var argent = new Argent("éspèces", au07juillet2024, 800000, Devise.ARIARY);
        var fraisScolaire = new FluxArgent(
                "frais de scolarité 2023-2024", argent, LocalDate.of(2023, 11, 01),
                LocalDate.of(2024, 8, 30), 200000, 27);
        var compteBancaire = new Argent("Compte Bancaire", au07juillet2024, 100000, Devise.ARIARY);
        var tenueDeCompte = new FluxArgent("Tenue de compte", compteBancaire, au07juillet2024,
                LocalDate.of(2024,8,17), 20000, 25);

        var patrimoineZety = new Patrimoine(
                "patrimoineZety",
                zety,
                au07juillet2024,
                Set.of(ordinateur, vetements, argent, fraisScolaire, compteBancaire, tenueDeCompte),
                Devise.ARIARY);

        var patrimoineLe17Septembre2024 = patrimoineZety.projectionFuture(LocalDate.of(2024,9,17)).getValeurComptable();
        System.out.println("Le patrimoine de Zety le 17 septembre 2024 vaut: " + patrimoineLe17Septembre2024 + " Ariary");


        //Zety s’endette
        var au18Septembre2024 = LocalDate.of(2024, 9, 18);
        var fraisFutur = new Dette("Frais 2024-2025", au18Septembre2024, -10000000, Devise.ARIARY);
        var ncb = new FluxArgent("Compte bancaire", compteBancaire, au18Septembre2024, au18Septembre2024,10000000,18);
        var coutPret = new Dette("coût du prêt", au18Septembre2024, -1000000, Devise.ARIARY);

        var nouveauPatrimoine = new Patrimoine(
                "patrimoine après dette", zety, au18Septembre2024, Set.of(fraisFutur, coutPret, ncb),Devise.ARIARY
        );

        var patrimoineLe18Septembre2024 = nouveauPatrimoine.projectionFuture(LocalDate.of(2024,9,18)).getValeurComptable();
        System.out.println("Le patrimoine de Zety entre le 17 et le 18 septembre 2024 a diminué de: " +
                (patrimoineLe17Septembre2024 + patrimoineLe18Septembre2024) + " Ariary");

        //Zety étudie en 2024-2025
        var frais2025 = new FluxArgent("frais scolaire 2025", compteBancaire,
                LocalDate.of(2024,9,21), LocalDate.of(2024,9,21),
                2500000, 21);
        var donParents = new FluxArgent("don des parents", argent,
                LocalDate.of(224,01,01), LocalDate.of(2025,02,13), 100000,15);
        var trainDeVie = new FluxArgent("train de vie", argent,
                LocalDate.of(2024,10,01),LocalDate.of(2025,02,13),
                250000,1);

        var patrimoine2025 = new Patrimoine("patrimoine 2025", zety, LocalDate.of(2024,9,21),
                Set.of(frais2025,donParents,trainDeVie), Devise.ARIARY);

        if (argent.getValeurComptable() <= 0){
            System.out.println("Zety n'a plus d'éspèce le: " + argent.getT());
        }

        //Zety part en Allemagne
        System.out.println("Le patrimoine de Zety le 14 Février 2025: " +
                patrimoine2025.projectionFuture(LocalDate.of(2025,02,14)).getValeurComptable() +
                " Ariary");

        var nouveauCompteBancaire = new Dette("dette", LocalDate.of(2025,2,15),-7000, Devise.EURO);

        var patrimoineAllemagne = new Patrimoine("patrimoine en Allemagne", zety,
                LocalDate.of(2025,02,15), Set.of(nouveauCompteBancaire), Devise.EURO);

        System.out.println("Le patrimoine de Zety à date du 26 octobre 2025 vaut: " +
                patrimoineAllemagne.projectionFuture(LocalDate.of(2025,10,26)).getValeurComptable() +
                " Euro");

    }
}
