package school.hei.patrimoine.zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    int valeurPatrimoine = zetyPatrimoine.getValeurComptable();

    @Test
    void testValeurPatrimoineLe17Septembre2024() {
        Patrimoine patrimoineFutur = zetyPatrimoine.projectionFuture(dateFuture);
        int valeurFuture = patrimoineFutur.getValeurComptable();

        assertEquals(2978848, valeurFuture);
    }

    @Test
    void testDiminutionPatrimoineDeZety() {
        FluxArgent ajoutCompteBancaire = new FluxArgent("Ajout au compte bancaire", compteBancaire, LocalDate.of(2024, 9, 18), LocalDate.of(2024, 9, 18), 10000000, 18);
        possessions.add(ajoutCompteBancaire);
        Argent dette = new Dette("Dette bancaire", LocalDate.of(2024, 9, 18), -11000000);
        possessions.add(dette);

        Patrimoine patrimoineApresDette = zetyPatrimoine.projectionFuture(LocalDate.of(2024, 9, 18));

        int valeurApres = patrimoineApresDette.getValeurComptable();
        int diminution = valeurPatrimoine - valeurApres ;

       assertEquals(1623536, diminution);
    }

    @Test
    void testDateFinEspèces() {
        FluxArgent paiementFraisScolarite = new FluxArgent("Paiement des frais de scolarité", compteBancaire, LocalDate.of(2024, 9, 21), LocalDate.of(2024, 9, 21), -2500000, 21);
        possessions.add(paiementFraisScolarite);

        FluxArgent donMensuelParents = new FluxArgent("Don mensuel des parents", argentEspece, LocalDate.of(2024, 1, 15), LocalDate.of(2025, 2, 15), 100000, 15);
        possessions.add(donMensuelParents);

        LocalDate debutDepensesMensuelles = LocalDate.of(2024, 10, 1);
        LocalDate finDepensesMensuelles = LocalDate.of(2025, 2, 13);
        int montantDepensesMensuelles = 250000;

        int argentRestant = argentEspece.getValeurComptable();
        LocalDate dateEpuisement = debutDepensesMensuelles;

        while (argentRestant > 0 && !dateEpuisement.isAfter(finDepensesMensuelles)) {
            argentRestant -= montantDepensesMensuelles;
            dateEpuisement = dateEpuisement.plusMonths(1);
        }
        
        assertEquals(LocalDate.of(2025, 2, 1), dateEpuisement);
    }
}