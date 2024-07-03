package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZetyPatrimoineTest {
    @Test
    void patrimoine_de_zety_au17septembre2024(){
        var zety = new Personne("zety");
        var au3juillet2024 = LocalDate.of(2024, 7, 3);
        var au1Novemembre2023 = LocalDate.of(2023, 10, 1);
        var au1Aout2024 = LocalDate.of(2024, 8, 1);

        var zetyOrdinateur = new Materiel("zetyOrdinateur",au3juillet2024,1_200_000, au3juillet2024,-0.10);
        var zetyVetements = new Materiel("zetyVetements",au3juillet2024,1_500_000, au3juillet2024,-0.50);
        var zetyArgent = new Argent("zetyArgent", au3juillet2024, 800_000);
        var zetyFraisDeScolarite = new Argent("zetyFraisDeScolarite", au3juillet2024, 0);
        var zetyFluxDArgentSurScolarite = new FluxArgent(
            "zetyFluxDArgentSurScolarite",
            zetyFraisDeScolarite,
            au1Novemembre2023,
            au1Aout2024,
            200_000,
            27
        );
        var zetyCompteBancaire = new Argent("zetyCompteBancaire", au3juillet2024, 100_000);
        var zetyFluxDArgentBancaire = new FluxArgent("zetyFluxDArgentBancaire", zetyCompteBancaire, au3juillet2024, LocalDate.MAX, -20_000, 25);

        var patrimoineZetyAu3juillet2024 = new Patrimoine(
            "patrimoineZetyAu3juillet2024",
            zety,
            au3juillet2024,
            Set.of(
                zetyOrdinateur,
                zetyVetements,
                zetyArgent,
                new GroupePossession("Frais de scolaire", au3juillet2024,Set.of(zetyFraisDeScolarite, zetyFluxDArgentSurScolarite)),
                new GroupePossession("Frais compte bancaire", au3juillet2024,Set.of(zetyCompteBancaire, zetyFluxDArgentBancaire))
            )
        );

        var au17Septembre2024 = LocalDate.of(2024, 9, 17);
        var expectedValeurZetyPatrimoineAu17Septembre2024 = 3_578_848;
        var patrimoineZetyAu17Septembre2024 = patrimoineZetyAu3juillet2024.projectionFuture(au17Septembre2024);
        assertEquals(expectedValeurZetyPatrimoineAu17Septembre2024, patrimoineZetyAu17Septembre2024.getValeurComptable());
    }
}