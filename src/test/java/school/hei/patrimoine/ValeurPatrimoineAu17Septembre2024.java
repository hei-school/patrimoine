package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.CompteBancaire;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValeurPatrimoineAu17Septembre2024 {

    @Test
    public void valeur_Patrimoine_Au_17_Septembre2024(){
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, Month.JULY, 3); // Correction de la date
        var au17septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 17);
        var ordinateur = new Materiel("Azus Rog", au3juillet24, 1_200_000, au3juillet24, -0.10);
        var vetement = new Materiel("chemise,costume,chaussure...", au3juillet24, 1_500_000, au3juillet24, -0.50);
        var argentEspeces = new Argent("Argent en espèces", au3juillet24, 800_000);
        var compteBancaire = new CompteBancaire("Compte bancaire", au3juillet24, 100_000);
        Set<Possession> possessions = Set.of(ordinateur, vetement, argentEspeces, compteBancaire);
        var patrimoineDeZety = new Patrimoine("Patrimoine de zety au 3 juillet 2024", zety, au3juillet24, possessions);

        Patrimoine patrimoineFutur = patrimoineDeZety.projectionFuture(au17septembre24);
        int valeurComptableFuture = patrimoineFutur.getValeurComptable();


        assertEquals(3_378_848, valeurComptableFuture);
    }
}
