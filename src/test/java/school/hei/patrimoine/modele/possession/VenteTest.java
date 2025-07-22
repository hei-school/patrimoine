package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.vente.ValeurMarche;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class VenteTest {

    @Test
    void valeur_marche_doitvalider_les_parametres() {
        assertThrows(IllegalArgumentException.class,
                () -> new ValeurMarche(null, new Argent(100, Devise.EUR)));

        assertThrows(IllegalArgumentException.class,
                () -> new ValeurMarche(LocalDate.now(), null));
    }

    @Test
    void valeur_marche_doit_stocker_correctement_les_valeurs() {
        var date = LocalDate.of(2025, 1, 1);
        var argent = new Argent(300_000, Devise.EUR);
        var vm = new ValeurMarche(date, argent);

        assertEquals(date, vm.t());
        assertEquals(argent, vm.valeur());
    }

    @Test
    void vente_doit_marquer_possession_comme_vendue() {
        var materiel = new Materiel("Voiture", LocalDate.now(), LocalDate.now(),
                new Argent(20_000, Devise.EUR), 0.0);
        var compte = new Compte("Compte courant", LocalDate.now(), new Argent(0, Devise.EUR));

        materiel.vendre(LocalDate.now(), new Argent(25_000, Devise.EUR), compte);

        assertTrue(materiel.estVendu());
        assertEquals(LocalDate.now(), materiel.getDateVente().get());
        assertEquals(new Argent(25_000, Devise.EUR), materiel.getPrixVente().get());
    }

    @Test
    void vente_doit_transferer_argent_vers_compte() {
        var dateVente = LocalDate.now();
        var materiel = new Materiel("Voiture", dateVente, dateVente,
                new Argent(20_000, Devise.EUR), 0.0);
        var compte = new Compte("Compte courant", dateVente, new Argent(0, Devise.EUR));

        materiel.vendre(dateVente, new Argent(25_000, Devise.EUR), compte);

        // Vérifier la valeur après projection à la date de vente
        assertEquals(new Argent(25_000, Devise.EUR), compte.projectionFuture(dateVente).valeurComptable());
    }

    @Test
    void valeur_comptable_apres_vente_doit_etre_nulle() {
        var materiel = new Materiel("Voiture", LocalDate.now(), LocalDate.now(),
                new Argent(20_000, Devise.EUR), 0.0);
        var compte = new Compte("Compte courant", LocalDate.now(), new Argent(0, Devise.EUR));

        materiel.vendre(LocalDate.now(), new Argent(25_000, Devise.EUR), compte);

        assertEquals(new Argent(0, Devise.EUR), materiel.valeurComptable());
    }

    @Test
    void valeur_marche_historique_doit_etre_conservee() {
        var materiel = new Materiel("Bâtiment", LocalDate.now(), LocalDate.now(),
                new Argent(200_000, Devise.EUR), 0.0);
        var date1 = LocalDate.of(2025, 1, 1);
        var date2 = LocalDate.of(2025, 6, 1);

        materiel.ajouterValeurMarche(new ValeurMarche(date1, new Argent(250_000, Devise.EUR)));
        materiel.ajouterValeurMarche(new ValeurMarche(date2, new Argent(300_000, Devise.EUR)));

        assertEquals(new Argent(250_000, Devise.EUR), materiel.getValeurMarche(date1));
        assertEquals(new Argent(300_000, Devise.EUR), materiel.getValeurMarche(date2));
    }

    @Test
    void vendre_possession_deja_vendue_doit_echouer() {
        var materiel = new Materiel("Voiture", LocalDate.now(), LocalDate.now(),
                new Argent(20_000, Devise.EUR), 0.0);
        var compte = new Compte("Compte courant", LocalDate.now(), new Argent(0, Devise.EUR));

        materiel.vendre(LocalDate.now(), new Argent(25_000, Devise.EUR), compte);

        assertThrows(IllegalStateException.class, () ->
                materiel.vendre(LocalDate.now(), new Argent(30_000, Devise.EUR), compte));
    }
}
