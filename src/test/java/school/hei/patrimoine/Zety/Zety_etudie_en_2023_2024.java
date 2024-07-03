package school.hei.patrimoine.Zety;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Zety_Test {
    @Test
    void test_valeur_ordinateur() {
        var dateInitiale = LocalDate.of(2024, JULY, 3);
        var dateCible = LocalDate.of(2024, SEPTEMBER, 17);
        var nombreJours = ChronoUnit.DAYS.between(dateInitiale, dateCible);
        var valeurInitiale = 1_200_000;
        var tauxAppréciationJournalier = Math.pow(1 - 0.1, 1.0 / 365);

        var valeurFinale = valeurInitiale * Math.pow(tauxAppréciationJournalier, nombreJours);

        System.out.println("Valeur ordinateur finale calculée : " + valeurFinale);
        assertEquals(1_173_961, Math.round(valeurFinale));
    }

    @Test
    void test_valeur_Vetements() {
        var dateInitiale = LocalDate.of(2024, JULY, 3);
        var dateCible = LocalDate.of(2024, SEPTEMBER, 17);
        var nombreJours = ChronoUnit.DAYS.between(dateInitiale, dateCible);
        var valeurInitiale = 1_500_000;
        var tauxAppréciationJournalier = Math.pow(1 - 0.5, 1.0 / 365);

        var valeurFinale = valeurInitiale * Math.pow(tauxAppréciationJournalier, nombreJours);

        System.out.println("Valeur vetements finale calculée : " + valeurFinale);
        assertEquals(1_298_408, Math.round(valeurFinale));
    }


    @Test
    void test_argent_Especes() {
        var valeurInitiale = 800_000;
        var fraisScolariteMensuel = 200_000;
        var nombreMois = 2;

        var valeurFinale = valeurInitiale - (fraisScolariteMensuel * nombreMois);
        System.out.println("Valeur finale argent en especes calculée : " + valeurFinale);
        assertEquals(400_000, valeurFinale);
    }

    @Test
    void test_argent_CompteBancaire() {
        var valeurInitiale = 100_000;
        var fraisTenueCompteMensuel = 20_000;
        var nombreMois = 2;

        var valeurFinale = valeurInitiale - (fraisTenueCompteMensuel * nombreMois);
        System.out.println("Valeur finale argent sur le compte bancaire calculée : " + valeurFinale);
        assertEquals(60_000, valeurFinale);
    }

    @Test
    void test_Patrimoine_Total_qui_etudie_en_2023_2024() {
        var dateInitiale = LocalDate.of(2024, JULY, 3);
        var dateCible = LocalDate.of(2024, SEPTEMBER, 17);
        var nombreJours = ChronoUnit.DAYS.between(dateInitiale, dateCible);

        // Ordinateur
        var valeurOrdinateurInitiale = 1_200_000;
        var tauxAppréciationJournalierOrdinateur = Math.pow(1 - 0.1, 1.0 / 365);
        var valeurOrdinateurFinale = valeurOrdinateurInitiale * Math.pow(tauxAppréciationJournalierOrdinateur, nombreJours);

        // Vêtements
        var valeurVetementsInitiale = 1_500_000;
        var tauxAppréciationJournalierVetements = Math.pow(1 - 0.5, 1.0 / 365);
        var valeurVetementsFinale = valeurVetementsInitiale * Math.pow(tauxAppréciationJournalierVetements, nombreJours);

        // Espèces
        var valeurEspecesInitiale = 800_000;
        var fraisScolariteMensuel = 200_000;
        var nombreMois = 2;
        var valeurEspecesFinale = valeurEspecesInitiale - (fraisScolariteMensuel * nombreMois);

        // Compte bancaire
        var valeurCompteInitiale = 100_000;
        var fraisTenueCompteMensuel = 20_000;
        var valeurCompteFinale = valeurCompteInitiale - (fraisTenueCompteMensuel * nombreMois);

        // Valeur totale
        var valeurTotale = Math.round(valeurOrdinateurFinale) + Math.round(valeurVetementsFinale) + valeurEspecesFinale + valeurCompteFinale;
        System.out.println("Valeur totale finale calculée : " + valeurTotale);
        assertEquals(2_932_369, valeurTotale);
    }
}
