package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class diminutionPatrimoineDeZety {
    @Test
    public void diminution_Patrimoine_Entre_17_et_18_Septembre2024() {
        var zety = new Personne("Zety");
        var au17septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 17);
        var au18septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 18);

        var ordinateur = new Materiel("Azus Rog", au17septembre24, 1_200_000, au17septembre24, -0.10);
        var vetement = new Materiel("chemise, costume, chaussure...", au17septembre24, 1_500_000, au17septembre24, -0.50);
        var argentEspeces = new Argent("Argent en espèces", au17septembre24, 800_000);
        var compteBancaire = new CompteBancaire("Compte bancaire", au17septembre24, 100_000);

        Set<Possession> possessions = Set.of(ordinateur, vetement, argentEspeces, compteBancaire);

        var patrimoineDeZety = new Patrimoine("Patrimoine de Zety au 17 septembre 2024", zety, au17septembre24, possessions);


        var dette = new Dette("Dette à la banque", au18septembre24, -11_000_000);


        Set<Possession> possessionsPostEmprunt = new HashSet<>(possessions);
        possessionsPostEmprunt.add(dette);

        Patrimoine patrimoinePostEmprunt = patrimoineDeZety.projectionFuture(au18septembre24);
        int valeurComptablePostEmprunt = patrimoinePostEmprunt.getValeurComptable();

        int diminutionPatrimoine = (patrimoineDeZety.getValeurComptable() - valeurComptablePostEmprunt) * -1;

        assertEquals(-2384, diminutionPatrimoine);
    }

}

