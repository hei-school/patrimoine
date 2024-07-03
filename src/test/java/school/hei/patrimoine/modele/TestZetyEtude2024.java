package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestZetyEtude2024 {

    @Test
    public void testDateSansEspece() {
        Personne zety = new Personne("Zety");

        LocalDate debut2024 = LocalDate.of(2024, 1, 1);
        LocalDate au21septembre24 = LocalDate.of(2024, 9, 21);
        LocalDate debutOctobre24 = LocalDate.of(2024, 10, 1);

        Argent compteBancaire = new Argent("Compte bancaire", debut2024, 0);
        Argent espèces = new Argent("Espèces", debut2024, 0);

        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité 2024-2025",
                compteBancaire,
                au21septembre24,
                au21septembre24,
                -2_500_000,
                21);

        FluxArgent donMensuel = new FluxArgent(
                "Don mensuel des parents",
                espèces,
                debut2024,
                LocalDate.of(2025, 2, 15),
                100_000,
                15);

        FluxArgent trainDeVie = new FluxArgent(
                "Train de vie mensuel",
                espèces,
                debutOctobre24,
                LocalDate.of(2025, 2, 1),
                -250_000,
                1);

        Set<Possession> possessionsZety = new HashSet<>();
        possessionsZety.add(compteBancaire);
        possessionsZety.add(espèces);
        possessionsZety.add(fraisScolarite);
        possessionsZety.add(donMensuel);
        possessionsZety.add(trainDeVie);

        Patrimoine patrimoineZety = new Patrimoine("Patrimoine de Zety", zety, debut2024, possessionsZety);

        LocalDate dateSansEspece = LocalDate.of(2024, 10, 1);
        while (patrimoineZety.getValeurComptable() > 0) {
            Patrimoine patrimoineProjection = patrimoineZety.projectionFuture(dateSansEspece);
            if (patrimoineProjection.getValeurComptable() > 0) {
                patrimoineZety = patrimoineProjection;
                dateSansEspece = dateSansEspece.plusDays(1);
            } else {
                break;
            }
        }

        assertEquals(LocalDate.of(2024, 10, 1), dateSansEspece);
    }
}
