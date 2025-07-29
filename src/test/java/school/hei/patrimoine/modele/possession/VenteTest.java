package school.hei.patrimoine.modele.possession;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.vente.Vente;

class VenteTest {

  @Test
  void vente_d_un_materiel_met_a_jour_les_donnees_correctement() {
    var dateAchat = LocalDate.of(2023, 1, 1);
    var dateVente = LocalDate.of(2024, 1, 1);
    var prixVente = new Argent(1_000_000, Devise.MGA);
    var compte = new Compte("Compte principal", dateAchat, new Argent(500_000, Devise.MGA));
    var materiel =
        new Materiel("Imprimante", dateAchat, dateAchat, new Argent(2_000_000, Devise.MGA), -0.05);

    new Vente(materiel, dateVente, prixVente, compte);

    assertTrue(materiel.estVendue());
    assertEquals(dateVente, materiel.getDateVente());
    assertEquals(prixVente, materiel.getPrixVente());
    assertEquals(Argent.ariary(0), materiel.valeurActuelle(dateVente.plusDays(1)));
    assertEquals(Argent.ariary(1_500_000), compte.valeurActuelle());
  }

  @Test
  void vente_possession_deja_vendue_declenche_exception() {
    var date = LocalDate.of(2024, 1, 1);
    var prix = new Argent(1_000_000, Devise.MGA);
    var compte = new Compte("Compte test", date, new Argent(0, Devise.MGA));
    var materiel = new Materiel("Téléphone", date, date, new Argent(500_000, Devise.MGA), 0.0);

    new Vente(materiel, date, prix, compte);

    var exception =
        assertThrows(
            IllegalStateException.class, () -> new Vente(materiel, date.plusDays(1), prix, compte));
    assertTrue(exception.getMessage().contains("déjà vendue"));
  }

  @Test
  void vente_avec_date_invalide_declenche_exception() {
    var dateAchat = LocalDate.of(2024, 5, 1);
    var dateVente = LocalDate.of(2024, 4, 30); // avant achat
    var prix = new Argent(1_000_000, Devise.MGA);
    var compte = new Compte("Compte invalide", dateAchat, new Argent(0, Devise.MGA));
    var materiel = new Materiel("TV", dateAchat, dateAchat, new Argent(2_000_000, Devise.MGA), 0.0);

    var exception =
        assertThrows(
            IllegalArgumentException.class, () -> new Vente(materiel, dateVente, prix, compte));
    assertTrue(exception.getMessage().contains("antérieure à l'acquisition"));
  }
}
