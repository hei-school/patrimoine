package school.hei.patrimoine.modele.possession;


import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;


import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EntrepriseTest {

    @Test
    void entreprise_est_bien_initialisee() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        Argent valeurComptable = Argent.euro(100_000);
        Argent valeurMarche = Argent.euro(120_000);
        Entreprise entreprise = new Entreprise("POJA", date, valeurComptable, valeurMarche, 0.10);

        assertEquals("POJA", entreprise.nom());
        assertEquals(valeurComptable, entreprise.valeurComptable());
        assertEquals(valeurMarche, entreprise.getValeurMarche());
        assertEquals(TypeAgregat.ENTREPRISE, entreprise.typeAgregat());
        assertEquals(Map.of(date, valeurMarche), entreprise.getHistoriqueValeurMarche());
    }

    @Test
    void entreprise_valeur_marche_egale_a_valeur_comptable_si_null() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        Argent valeurComptable = Argent.euro(50_000);
        Entreprise entreprise = new Entreprise("HEI", date, valeurComptable, null, 0.05);

        assertEquals(valeurComptable, entreprise.getValeurMarche());
    }

    @Test
    void entreprise_projection_dans_le_passe_donne_valeur_zero() {
        LocalDate dateInitiale = LocalDate.of(2025, 1, 1);
        LocalDate datePassee = LocalDate.of(2024, 1, 1);
        Argent valeurComptable = Argent.euro(100_000);
        Argent valeurMarche = Argent.euro(120_000);

        Entreprise entreprise = new Entreprise("HEI", dateInitiale, valeurComptable, valeurMarche, 0.1);
        Entreprise projection = entreprise.projectionFuture(datePassee);

        assertEquals(Argent.euro(0), projection.getValeurMarche());
        assertTrue(projection.getHistoriqueValeurMarche().containsKey(dateInitiale));
    }

    @Test
    void entreprise_projection_dans_le_futur_valeur_augmente_selon_taux() {
        LocalDate dateInitiale = LocalDate.of(2025, 1, 1);
        LocalDate dateFutur = LocalDate.of(2026, 1, 1); // +365 jours
        Argent valeurInitiale = Argent.euro(100_000);

        Entreprise entreprise = new Entreprise("numer", dateInitiale, valeurInitiale, valeurInitiale, 0.10);
        Entreprise projetee = entreprise.projectionFuture(dateFutur);

        Argent attendu = Argent.euro(110_000);

        assertTrue(projetee.getValeurMarche().equals(attendu));
    }

    @Test
    void entreprise_valeur_ne_peut_pas_devenir_negative() {
        LocalDate dateInitiale = LocalDate.of(2025, 1, 1);
        LocalDate dateFutur = LocalDate.of(2030, 1, 1);
        Argent valeurInitiale = Argent.euro(100_000);

        Entreprise entreprise = new Entreprise("YAS", dateInitiale, valeurInitiale, valeurInitiale, -1.0);
        Entreprise projetee = entreprise.projectionFuture(dateFutur);

        assertFalse(projetee.getValeurMarche().lt(0));
    }

    @Test
    void historique_apres_projection() {
        LocalDate dateInitiale = LocalDate.of(2025, 1, 1);
        LocalDate dateFutur = LocalDate.of(2025, 6, 1);
        Argent valeur = Argent.euro(80_000);

        Entreprise entreprise = new Entreprise("ITU", dateInitiale, valeur, valeur, 0.10);
        Entreprise projetee = entreprise.projectionFuture(dateFutur);

        assertTrue(projetee.getHistoriqueValeurMarche().containsKey(dateInitiale));
        assertTrue(projetee.getHistoriqueValeurMarche().containsKey(dateFutur));
    }

    @Test
    void vente_entreprise_et_valeur_comptable_devient_nulle() {
        // GIVEN
        LocalDate dateInitiale = LocalDate.of(2024, 1, 1);
        Argent valeurComptable = new Argent(100_000, Devise.EUR);
        Argent valeurMarche = new Argent(120_000, Devise.EUR);
        double tauxEvolution = 0.05; // 5%/an

        Entreprise entreprise = new Entreprise(
                "Ma startup",
                dateInitiale,
                valeurComptable,
                valeurMarche,
                tauxEvolution
        );


    }
}
