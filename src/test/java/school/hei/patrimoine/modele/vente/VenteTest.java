package school.hei.patrimoine.modele.vente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Vente;

public class VenteTest {

  @Test
  void creation_vente_doit_modifier_possession_et_crediter_compte() {
    LocalDate dateVente = LocalDate.of(2025, 7, 30);
    Argent prixVente = Argent.ariary(5000);
    Compte compteBeneficiaire =
        new Compte(
            "Compte test", dateVente.minusMonths(1), dateVente.minusMonths(1), Argent.ariary(1000));
    Compte possession =
        new Compte(
            "Possession test",
            dateVente.minusYears(1),
            dateVente.minusYears(1),
            Argent.ariary(3000));

    new Vente(dateVente, possession, compteBeneficiaire, prixVente);

    assertTrue(possession.estVendu(dateVente), "La possession devrait être marquée comme vendue");
    assertEquals(
        Optional.of(dateVente),
        possession.getDateVente(),
        "La date de vente devrait être correctement enregistrée");
    assertEquals(
        Optional.of(prixVente),
        possession.getPrixVente(),
        "Le prix de vente devrait être correctement enregistré");
    assertEquals(
        1,
        compteBeneficiaire.getFluxArgents().size(),
        "Le compte bénéficiaire devrait contenir un flux financier");
  }
}
