package school.hei.patrimoine.modele.possession;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MaterielTest {

  @Test
  public void testVenteMateriel() {

    LocalDate dateAchat = LocalDate.of(2024, 1, 1);
    LocalDate dateComptable = LocalDate.of(2024, 1, 1);
    LocalDate dateVente = LocalDate.of(2025, 7, 17);
    Argent valeurComptable = new Argent(1_000, Devise.EUR);
    Argent prixVente = new Argent(900, Devise.EUR);
    double tauxAppreciation = 0.0;
    Compte compteBeneficiaire = new Compte("Caisse", dateAchat, new Argent(0, Devise.EUR));


    Materiel materiel = new Materiel(
            "Ordinateur",
            dateAchat,
            dateComptable,
            valeurComptable,
            tauxAppreciation
    );

    // Assert avant vente
    assertFalse(materiel.estVendue(), "Le matériel ne doit pas être vendu avant appel à vendre()");
    assertTrue(materiel.estActiveALaDate(LocalDate.of(2024, 12, 31)));
    assertEquals(valeurComptable, materiel.valeurComptableFuture(dateVente.minusDays(1)));

    // Act
    materiel.vendre(dateVente, prixVente, compteBeneficiaire);

    // Assert après vente
    assertTrue(materiel.estVendue(), "Le matériel doit être marqué comme vendu après appel à vendre()");
    assertEquals(dateVente, materiel.getDateVente(), "La date de vente doit être correcte");
    assertEquals(prixVente, materiel.getPrixVente(), "Le prix de vente doit être correctement enregistré");

    // Vérification de la désactivation comptable : valeur comptable = 0 après la vente
    assertEquals(
            new Argent(0, Devise.EUR),
            materiel.projectionFuture(dateVente.plusDays(1)).valeurComptable()

    );

    // Vérifie que la possession est inactive après la vente
    assertFalse(materiel.estActiveALaDate(LocalDate.of(2025, 7, 18)), "Le matériel ne doit plus être actif après la date de vente");
  }
}
