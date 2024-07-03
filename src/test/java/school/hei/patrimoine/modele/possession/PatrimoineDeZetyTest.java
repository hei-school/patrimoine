package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static java.time.Month.*;

public class PatrimoineDeZetyTest {
    @Test
    public void patrimoine_de_zety_17_septembre_2024() {
        var Zety = new Personne("Zety");
        var au3Juillet24 = LocalDate.of(2024, JULY, 3);
        var au17Sept24 = LocalDate.of(2024, SEPTEMBER, 17);
        var ordinateur = new Materiel(
                "ordinateur de zety",
                au3Juillet24,
                1_200_000,
                au3Juillet24,
                -0.10
        );
        var vetements = new Materiel(
                "vetements de zety",
                au3Juillet24,
                1_500_000,
                au3Juillet24,
                -0.50
        );
        var argent_en_espece = new Argent("Argent en espeve de Zety", au3Juillet24, 800_000);
        var argent_ecollage_de_zety = new Argent("Ecollage de Zety", au3Juillet24, 200_000);
        var argent_dans_le_compte = new Argent("Argent dans le compte de Zety", au3Juillet24, 100_000);
        var frais_de_scolarite = new FluxArgent("Frais de scolarite de Zety", argent_ecollage_de_zety, LocalDate.of(2023, NOVEMBER, 1), LocalDate.of(2024, AUGUST, 31), -200_000, 27);
        var compte_bancaire = new FluxArgent("Compte bancaire de Zety", argent_dans_le_compte, au3Juillet24, au17Sept24, -20_000, 25);
        var patrimoine_Zety_le_3_juillet_2024 = new Patrimoine(
                "patrimoine de zety 17 septembre 2024",
                Zety,
                au3Juillet24,
                Set.of(new GroupePossession(
                                "possession de Zety",
                                au3Juillet24,
                                Set.of(ordinateur,
                                        vetements,
                                        argent_en_espece,
                                        frais_de_scolarite,
                                        compte_bancaire)
                        )
                )

        );
        assertEquals(3_318_848, patrimoine_Zety_le_3_juillet_2024.projectionFuture(au17Sept24).getValeurComptable());
    }

    @Test
    public void dette_de_zety_17_et_18_septembre(){
        var Zety = new Personne("Zety");
        var au17Sept24 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18Sept24 = au17Sept24.plusDays(1);
        var au18Sept25 = au18Sept24.plusYears(1);
        var argent_prete = new Argent("Argent prete a la banque",au18Sept24,10_000_000);
        var argent_a_rendre = new Argent("Argent a rendre a la banque",au18Sept25,11_000_000);
        var patrimoine_le_17sept24 = new Patrimoine(
                "patrimoine initial",
                Zety,
                au17Sept24,
                Set.of(
                        argent_prete
                )
        );
        var patrimoine_le_18sept24 = new Patrimoine(
                "patrimoine avec pret",
                Zety,
                au18Sept24,
                Set.of(
                        argent_a_rendre
                )
        );
        var diminution = patrimoine_le_18sept24.getValeurComptable() - patrimoine_le_17sept24.getValeurComptable();
        assertEquals(1_000_000,diminution);
    }
}
