package school.hei.patrimoine.Zety;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class test_zety_etudie_en_2023_2024 {
    @Test
    void test_valeur_ordinateur(){
        var dateInitiale = LocalDate.of(2024, JULY, 3);
        var dateCible = LocalDate.of(2024, SEPTEMBER, 17);
        var nombreJours = ChronoUnit.DAYS.between(dateInitiale, dateCible);
        var valeurInitiale = 1_200_000;
        var tauxAppréciationJournalier = Math.pow(1 - 0.1, 1.0 / 365);

        var valeurFinale = valeurInitiale * Math.pow(tauxAppréciationJournalier, nombreJours);

        System.out.println("Valeur finale calculée : " + valeurFinale);
        assertEquals(1_173_961, Math.round(valeurFinale));
    }

    @Test
    void testVetements() {
        var dateInitiale = LocalDate.of(2024, JULY, 3);
        var dateCible = LocalDate.of(2024, SEPTEMBER, 17);
        var nombreJours = ChronoUnit.DAYS.between(dateInitiale, dateCible);
        var valeurInitiale = 1_500_000;
        var tauxAppréciationJournalier = Math.pow(1 - 0.5, 1.0 / 365);

        var valeurFinale = valeurInitiale * Math.pow(tauxAppréciationJournalier, nombreJours);

        System.out.println("Valeur finale calculée : " + valeurFinale);
        assertEquals(1_298_408, Math.round(valeurFinale));
    }


    }
