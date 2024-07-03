package school.hei.patrimoine.zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

class ZetyTest {
    LocalDate dateActuelle = LocalDate.of(2024, 7, 3);
    LocalDate dateFuture = LocalDate.of(2024, 9, 17);

    Materiel ordinateur = new Materiel("Ordinateur", dateActuelle, 1200000, dateActuelle, -0.10);
    Materiel vetements = new Materiel("Vêtements", dateActuelle, 1500000, dateActuelle, -0.50);
    Argent argentEspece = new Argent("Argent en espèces", dateActuelle, 800000);
    Argent compteBancaire = new Argent("Compte bancaire", dateActuelle, 100000);

    FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité", argentEspece, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200000, 27);
    FluxArgent fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, dateActuelle, dateFuture, -20000, 25);

    Set<Possession> possessions = new HashSet<>();

    Patrimoine zetyPatrimoine;

    {
        possessions.add(ordinateur);
        possessions.add(vetements);
        possessions.add(argentEspece);
        possessions.add(compteBancaire);
        possessions.add(fraisScolarite);
        possessions.add(fraisTenueCompte);

        zetyPatrimoine = new Patrimoine("Patrimoine de Zety", new Personne("Zety"), dateActuelle, possessions);
    }

    Patrimoine patrimoineFutur = zetyPatrimoine.projectionFuture(dateFuture);

    @Test
    void testValeurPatrimoineLe17Septembre2024() {
        int valeurFuture = patrimoineFutur.getValeurComptable();
        System.out.println("Valeur du patrimoine de Zety le 17 septembre 2024 : " + valeurFuture);
    }

    @Test
    void testDiminutionPatrimoineDeZety() {
        Argent dette = new Dette("Dette bancaire", LocalDate.of(2024, 9, 18), -11000000);
        possessions.add(dette);
        Patrimoine patrimoineApres = zetyPatrimoine.projectionFuture(LocalDate.of(2024, 9, 18));

        int valeurAvantDette = zetyPatrimoine.getValeurComptable();
        int valeurApresDette = patrimoineApres.getValeurComptable();
        int diminution = valeurApresDette - valeurApresDette;

        System.out.println(diminution);
    }
}
