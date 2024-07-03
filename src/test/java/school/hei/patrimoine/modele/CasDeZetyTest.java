package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CasDeZetyTest {
    @Test
    void valeur_patrimoine_de_zety_le_17_semptembre_2024(){
        var euro = new Devise("euro", 4_821);
        var ariary = new Devise("ariary", 1);

        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var zety = new Personne("Zety");
        var ordinateur = new Materiel(
                "ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24,
                -0.1,
                ariary
        );
        var vetements = new Materiel(
                "vetements",
                au3juillet24,
                1_500_000,
                au3juillet24,
                -0.5,
                ariary
        );
        var espece = new Argent("Espèces", au3juillet24, 800_000, ariary);
        var au27nov23 = LocalDate.of(2023, NOVEMBER, 27);
        var au27aout24 = LocalDate.of(2024, AUGUST, 27);
        var fraisDeScolarite = new FluxArgent(
                "frais de scolarité flux",
                espece,
                au27nov23,
                au27aout24,
                -200_000,
                27,
                ariary);
        var compteBancaire = new Argent("compte bancaire", au3juillet24, 100_000, ariary);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
        var fluxCompteBancaire = new FluxArgent(
                "compte bancaire flux",
                compteBancaire,
                au3juillet24,
                au17septembre24,
                -20_000,
                25,
                ariary
        );
        var patrimoineDeZety = new Patrimoine(
                "patrimoine de Zety",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, espece, fraisDeScolarite, fluxCompteBancaire)
        );

        var actual = patrimoineDeZety.projectionFuture(au17septembre24, ariary).getValeurComptable();
        assertEquals(2_918_848, actual);
    }

    @Test
    void zety_s_endette(){
        var euro = new Devise("euro", 4_821);
        var ariary = new Devise("ariary", 1);

        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var zety = new Personne("Zety");
        var ordinateur = new Materiel(
                "ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24,
                -0.1,
                ariary
        );
        var vetements = new Materiel(
                "vetements",
                au3juillet24,
                1_500_000,
                au3juillet24,
                -0.5,
                ariary
        );
        var espece = new Argent("Espèces", au3juillet24, 800_000, ariary);
        var au27nov23 = LocalDate.of(2023, NOVEMBER, 27);
        var au27aout24 = LocalDate.of(2024, AUGUST, 27);
        var fraisDeScolarite = new FluxArgent(
                "frais de scolarité flux",
                espece,
                au27nov23,
                au27aout24,
                -200_000,
                27,
                ariary);
        var compteBancaire = new Argent("compte bancaire", au3juillet24, 100_000, ariary);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
        var fluxCompteBancaire = new FluxArgent(
                "compte bancaire flux",
                compteBancaire,
                au3juillet24,
                au17septembre24,
                -20_000,
                25,
                ariary
        );
        var patrimoineDeZetyLe17Septembre24 = new Patrimoine(
                "patrimoine de Zety",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, espece, fraisDeScolarite, fluxCompteBancaire)
        );
        var valeurDuPatrimoineDeZetyle17Septembre24 = patrimoineDeZetyLe17Septembre24.projectionFuture(au17septembre24, ariary).getValeurComptable();


        var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);
        var pret = new Creance("pret", au18septembre24, 10_000_000, ariary);

        var dette = new Dette("dette", au18septembre24, -11_000_000, ariary);
        var patrimoineDeZetyLe18Septembre24 = new Patrimoine(
                "patrimoine de Zety",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, espece, fraisDeScolarite, fluxCompteBancaire, dette, pret)
        );

        var valeurDuPatrimoineDeZetyLe18Septembre24 = patrimoineDeZetyLe18Septembre24.projectionFuture(au18septembre24, ariary).getValeurComptable();

        var actual = valeurDuPatrimoineDeZetyle17Septembre24 - valeurDuPatrimoineDeZetyLe18Septembre24;
        assertEquals(1_002_384 , actual);
    }

    /**
     * 2_978_848 : 1
     * 1_002_384
     * le 1 janv 2025
     * -1_528_686
     */

    @Test
    void date_ou_zety_n_a_pas_d_espece(){
    }
}
