package school.hei.patrimoine.zetyPatrimoine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static java.time.Month.*;

public class testPatrimoine {
    @Test
    public void test_au_17septembre24() {
        var zety = new Personne("zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au27noveembre23 = LocalDate.of(2023 , NOVEMBER , 27);
        var au27Aout2024 = LocalDate.of(24 , AUGUST , 27);
        var espace = new Argent("Espèces", au3juillet24, 800_000);
        var compteBancaire = new Argent("compte" , au3juillet24 , 100_000 );
        var au17sepembre24 = LocalDate.of(2024 , SEPTEMBER, 17);

        var fraisCompteBancaire = new FluxArgent(
                "frais compte bancaire",
                compteBancaire,
                au3juillet24,
                au3juillet24.plusYears(1),
                -20_000,
                25
        );
        var argentRetirerParLesFrais = 20_000 *2 ;

        var fraisDeScolarite = new FluxArgent(
                "frais de scolarite",
                espace ,
                au27noveembre23,
                au27Aout2024,
                -200_000,
                27
        );
        var totalPaye = 200_000;

        var ordinateur = new Materiel(
                "ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24,
                -0.1);
        var depresciationParJourPc = (0.1 / 365) * 1_200_000 ;
        var totalDepreciationPc = ChronoUnit.DAYS.between(au3juillet24 , au17sepembre24) * depresciationParJourPc;
        var valeurpc = 1_500_000 - totalDepreciationPc;

        var vetement = new Materiel(
                "vetements",
                au3juillet24,
                1_500_000,
                au3juillet24.minusDays(0),
                -0.5

        );

        var dette = new Dette(
                "dette pour frais de scolarite 2024-2025",
                LocalDate.of(2024 , SEPTEMBER , 18),
                -10_000_000
        );

        var depresciationParJour = (0.5 /365) * 1_500_000 ;
        var totalDepreciation = ChronoUnit.DAYS.between(au3juillet24 , au17sepembre24) * depresciationParJour;
        var valeurVetement = 1_500_000 - totalDepreciation;

        var zetyPatrimoine = new Patrimoine(
              "zety etudiante",
              zety,
              au3juillet24,
              Set.of(fraisDeScolarite , fraisCompteBancaire , ordinateur , vetement , espace , compteBancaire , dette )
        );

        var resteEspece = espace.getValeurComptable() - totalPaye ;
        var resteCompteBancaire = compteBancaire.getValeurComptable() - argentRetirerParLesFrais ;

        var patrimoinzValeur = resteEspece + resteCompteBancaire + valeurVetement + valeurpc ;
        var patrimoineAu17 = zetyPatrimoine.projectionFuture(au17sepembre24).getValeurComptable();

        var patrimoineAu18 = zetyPatrimoine.projectionFuture(LocalDate.of(2024 , SEPTEMBER , 18)).getValeurComptable();



       // Assertions.assertEquals(patrimoinzValeur , patrimoineAu17);
        Assertions.assertEquals((int)patrimoineAu18 ,(int) (patrimoineAu17 - 10_000_000 - depresciationParJour - depresciationParJourPc));


    }
}
