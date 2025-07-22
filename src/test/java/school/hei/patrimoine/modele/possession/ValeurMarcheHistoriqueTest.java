package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.vente.ValeurMarche;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValeurMarcheHistoriqueTest {
    private Materiel materiel;
    private LocalDate aujourdhui;
    private Argent valeurInitiale;

    @BeforeEach
    void setUp() {
        aujourdhui = LocalDate.now();
        valeurInitiale = new Argent(1000, Devise.EUR);
        materiel = new Materiel("Ordinateur", aujourdhui, aujourdhui, valeurInitiale, 0.05);
    }

    @Test
    void historique_valeur_marche_retourne_copie_defensive() {
        Set<ValeurMarche> copie = materiel.historiqueValeurMarche();
        copie.clear();

        assertEquals(1, materiel.historiqueValeurMarche().size());
        assertTrue(copie.isEmpty());
    }

    @Test
    void historique_valeur_marche_contient_valeur_initiale() {
        Set<ValeurMarche> historique = materiel.historiqueValeurMarche();

        assertEquals(1, historique.size());
        ValeurMarche premiereValeur = historique.iterator().next();
        assertEquals(aujourdhui, premiereValeur.date());
        assertEquals(valeurInitiale, premiereValeur.valeur());
    }

    @Test
    void historique_valeur_marche_apres_ajout_nouvelle_valeur() {
        LocalDate demain = aujourdhui.plusDays(1);
        Argent nouvelleValeur = new Argent(1050, Devise.EUR);
        materiel.ajouterValeurMarche(new ValeurMarche(demain, nouvelleValeur));

        Set<ValeurMarche> historique = materiel.historiqueValeurMarche();

        assertEquals(2, historique.size());
        assertTrue(historique.contains(new ValeurMarche(aujourdhui, valeurInitiale)));
        assertTrue(historique.contains(new ValeurMarche(demain, nouvelleValeur)));
    }

    @Test
    void historique_valeur_marche_pour_type_non_eligible() {
        Compte compte = new Compte("Compte Courant", aujourdhui, valeurInitiale);

        Set<ValeurMarche> historique = compte.historiqueValeurMarche();

        assertEquals(1, historique.size());
        assertEquals(valeurInitiale, historique.iterator().next().valeur());
    }

    @Test
    void vendre_possession_sans_compte_beneficiaire_doit_echouer() {
        var materiel = new Materiel("Voiture", LocalDate.now(), LocalDate.now(),
                new Argent(20_000, Devise.EUR), 0.0);

        assertThrows(IllegalArgumentException.class, () ->
                materiel.vendre(LocalDate.now(), new Argent(25_000, Devise.EUR), null));
    }

    @Test
    void vendre_possession_sans_date_vente_doit_echouer() {
        var materiel = new Materiel("Voiture", LocalDate.now(), LocalDate.now(),
                new Argent(20_000, Devise.EUR), 0.0);
        var compte = new Compte("Compte courant", LocalDate.now(), new Argent(0, Devise.EUR));

        assertThrows(IllegalArgumentException.class, () ->
                materiel.vendre(null, new Argent(25_000, Devise.EUR), compte));
    }

    @Test
    void vendre_possession_sans_prix_vente_doit_echouer() {
        var materiel = new Materiel("Voiture", LocalDate.now(), LocalDate.now(),
                new Argent(20_000, Devise.EUR), 0.0);
        var compte = new Compte("Compte courant", LocalDate.now(), new Argent(0, Devise.EUR));

        assertThrows(IllegalArgumentException.class, () ->
                materiel.vendre(LocalDate.now(), null, compte));
    }
}