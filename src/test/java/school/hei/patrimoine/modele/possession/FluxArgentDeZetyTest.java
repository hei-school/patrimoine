package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FluxArgentDeZetyTest {
    @Test
    void train_de_vie_de_zety() {
        var debut = LocalDate.of(2023, NOVEMBER, 1);
        var au3Juillet2024 = LocalDate.of(2024,JULY,3);
        var au17Sept2024 = LocalDate.of(2024,SEPTEMBER,17);
        var espece = new Argent("Espece", au3Juillet2024, 800_000);
        var fraisScolarite = new Argent("Frais de Scolarité", debut, 0);
        var compteBancaire = new Argent("Compte Bancaire" ,au3Juillet2024,100_000);
        var fin = LocalDate.of(2024, AUGUST, 30);
        var ArgentEnEspece = new FluxArgent(
                "L'Argent de Zety en Espece",
                espece, au3Juillet2024, au17Sept2024, 0,
                1);
        var fraisDeScolaritePonctionneParEspece = new FluxArgent(
                "Les Frais de Scolarité de Zety allant de Novembre 2023 à Aout 2024",
                fraisScolarite, debut, fin, -200_000,
                27);
        var SoldeRestantSurCompteBancaire = new FluxArgent(
                "Compte Bancaire",
                compteBancaire, au3Juillet2024, au17Sept2024, -20_000,
                25);

        assertEquals(800_000, ArgentEnEspece.projectionFuture(au17Sept2024).valeurComptable);
        assertEquals(-2_000_000, fraisDeScolaritePonctionneParEspece.projectionFuture(fin).valeurComptable);
        assertEquals(60_000, SoldeRestantSurCompteBancaire.projectionFuture(au17Sept2024).valeurComptable);
    }
}
