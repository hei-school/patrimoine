package school.hei.patrimoine.modele.possession;

import static java.util.Calendar.JULY;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.vente.VentePossession;

class VentePossessionTest {

  @Test
  void vente_possession_met_a_jour_les_donnees_correctement() {
    var dateAchat = LocalDate.of(2024, JULY, 1);
    var dateVente = LocalDate.of(2025, JULY, 1);
    var prixVente = Argent.ariary(1_000_000);
    var compteBeneficiaire = new Compte("Compte courant", dateAchat, Argent.ariary(500_000));
    var materiel =
        new Materiel("Ordinateur", dateAchat, dateAchat, Argent.ariary(2_000_000), -0.05);
    var vente = new VentePossession(materiel, dateVente, prixVente, compteBeneficiaire);

    vente.execute();

    assertTrue(materiel.estVendue());
    assertTrue(materiel.estVendue(dateVente));
    assertEquals(dateVente, materiel.getDateVente());
    assertEquals(prixVente, materiel.getPrixVente());

    assertEquals(Argent.ariary(1_500_000), compteBeneficiaire.valeurActuelle());
    assertEquals(Argent.ariary(0), materiel.valeurActuelle(dateVente));
    assertEquals(Argent.ariary(0), materiel.valeurActuelle(dateVente.plusDays(10)));
  }

  @Test
  void vente_possession_deja_vendue_lance_exception() {
    // Given
    var dateAchat = LocalDate.of(2024, JULY, 1);
    var dateVente = LocalDate.of(2025, JULY, 1);
    var prixVente = Argent.ariary(1_000_000);
    var compte = new Compte("Compte principal", dateAchat, Argent.ariary(0));
    var materiel = new Materiel("Laptop", dateAchat, dateAchat, Argent.ariary(2_000_000), 0.0);
    new VentePossession(materiel, dateVente, prixVente, compte).execute();

    var vente2 = new VentePossession(materiel, dateVente.plusDays(1), prixVente, compte);

    assertThrows(IllegalStateException.class, vente2::execute);
  }
}
