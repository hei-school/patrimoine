package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CasDeZetyTest {
    @Test
    void valeur_patrimoine_de_zety_le_17_semptembre_2024(){
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var zety = new Personne("Zety");
        var ordinateur = new Materiel(
                "ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24,
                -0.1
        );
        var vetements = new Materiel(
                "vetements",
                au3juillet24,
                1_500_000,
                au3juillet24,
                -0.5
        );
        var espece = new Argent("Espèces", au3juillet24, 800_000);
        var au27nov23 = LocalDate.of(2023, NOVEMBER, 27);
        var au27aout24 = LocalDate.of(2024, AUGUST, 27);
        var fraisDeScolarite = new FluxArgent(
                "frais de scolarité flux",
                espece,
                au27nov23,
                au27aout24,
                -200_000,
                27);
        var compteBancaire = new Argent("compte bancaire", au3juillet24, 100_000);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
        var fluxCompteBancaire = new FluxArgent(
                "compte bancaire flux",
                compteBancaire,
                au3juillet24,
                au17septembre24,
                -20_000,
                25
        );
        var patrimoineDeZety = new Patrimoine(
                "patrimoine de Zety",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, espece, fraisDeScolarite, fluxCompteBancaire)
        );

        var actual = patrimoineDeZety.projectionFuture(au17septembre24).getValeurComptable();
        assertEquals(2_918_848, actual);
    }


}
