package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatrimoineZetyTest {
    public static Patrimoine patrimoineZetyAu3Juillet2024(){
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var debutFraisScolarite = LocalDate.of(2023, NOVEMBER, 1);
        var finFraisScolarite = LocalDate.of(2024, AUGUST, 31);

        var ordinateur = new Materiel("ordinateur",au3juillet2024,1_200_000, au3juillet2024,-0.10);
        var vetements = new Materiel("vetements",au3juillet2024,1_500_000, au3juillet2024,-0.50);
        var argentEspeces = new Argent("argentEspeces", au3juillet2024, 800_000);
        new FluxArgent(
            "zetyFluxDArgentSurScolarite",
            argentEspeces,
            debutFraisScolarite,
            finFraisScolarite,
            -200_000,
            27
        );

        var compteBancaire = new Argent("zetyCompteBancaire", au3juillet2024, 100_000);
        new FluxArgent("zetyFluxDArgentBancaire", compteBancaire, au3juillet2024, LocalDate.MAX, -20_000, 25);

        return new Patrimoine(
            "patrimoineZetyAu3juillet2024",
            zety,
            au3juillet2024,
            new HashSet<>(Set.of(
                ordinateur,
                vetements,
                argentEspeces,
                compteBancaire
            ))
        );
    }

    @Test
    void zety_etudie_2023_2024(){
        var patrimoineZetyAu3Juillet2024 = patrimoineZetyAu3Juillet2024();
        var au17Septembre2024 = LocalDate.of(2024, SEPTEMBER, 17);
        var expectedValeurZetyPatrimoineAu17Septembre2024 = 2_978_848;
        var patrimoineZetyAu17Septembre2024 = patrimoineZetyAu3Juillet2024.projectionFuture(au17Septembre2024);

        assertEquals(expectedValeurZetyPatrimoineAu17Septembre2024, patrimoineZetyAu17Septembre2024.getValeurComptable());
    }

    @Test
    void zety_s_endette(){
        var patrimoineZetyAu3Juillet2024 = patrimoineZetyAu3Juillet2024();
        var au17Septembre2024 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18Septembre2024 = LocalDate.of(2024, SEPTEMBER, 18);
        var au18Septembre2025 = LocalDate.of(2025, SEPTEMBER, 18);

        new FluxArgent("detteFraisScolarite", (Argent) patrimoineZetyAu3Juillet2024.possessionParNom("zetyCompteBancaire"), au18Septembre2024, au18Septembre2024.plusDays(1), 10_000_000, 18);
        var detteBancaire = new Dette("detteBancaire", au18Septembre2024, -11_000_000);
        var finDetteBancaire = new FluxArgent("finDette", detteBancaire, au18Septembre2025, au18Septembre2025.plusDays(1), 11_000_000, 18);
        patrimoineZetyAu3Juillet2024.possessions().add(new GroupePossession("detteBancaire", au18Septembre2024, Set.of(detteBancaire, finDetteBancaire)));

        var patrimoineZetyAu17Septembre2024 = patrimoineZetyAu3Juillet2024.projectionFuture(au17Septembre2024);
        var patrimoineZetyAu18Septembre2024 = patrimoineZetyAu3Juillet2024.projectionFuture(au18Septembre2024);
        var actualDiminutionValeurComptable = patrimoineZetyAu17Septembre2024.getValeurComptable() - patrimoineZetyAu18Septembre2024.getValeurComptable();

        assertEquals(1_002_384, actualDiminutionValeurComptable);
    }

    @Test
    void zety_en_allemagne() {
        var patrimoineZetyAu3Juillet2024 = patrimoineZetyAu3Juillet2024();
        var argentEspecesAu3Juillet2024 = (Argent) patrimoineZetyAu3Juillet2024.possessionParNom("argentEspeces");
        var au21septembre2024 = LocalDate.of(2024, SEPTEMBER, 21);
        var au1Janvier2024 = LocalDate.of(2024, JANUARY, 1);
        var au1ctobre2024 = LocalDate.of(2024, JANUARY, 1);
        var au13Fevrier2025 = LocalDate.of(2025, FEBRUARY, 13);

        new FluxArgent("fraisScolariteEnUneSeulFois", argentEspecesAu3Juillet2024, au21septembre2024, au21septembre2024.plusDays(1), -2_500_000, 21);
        new FluxArgent(
            "argentDepuisParent",
            argentEspecesAu3Juillet2024,
            au1Janvier2024,
            LocalDate.MAX,
            100_000,
            15
        );
        new FluxArgent(
            "trainDeVie",
            argentEspecesAu3Juillet2024,
            au1ctobre2024,
            au13Fevrier2025,
            250_000,
            1
        );

        var au21Septembre2024 = LocalDate.of(2024, SEPTEMBER, 21);
        var argentEspecesAu19Septembre2024 = argentEspecesAu3Juillet2024.projectionFuture(au21Septembre2024);
        assertTrue(argentEspecesAu19Septembre2024.getValeurComptable() <= 0);
    }
}