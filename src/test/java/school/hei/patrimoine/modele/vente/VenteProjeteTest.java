package school.hei.patrimoine.modele.vente;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.possession.TypeAgregat.FLUX;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Vente;

public class VenteProjeteTest {
  private static final LocalDate TODAY = LocalDate.of(2024, 6, 10);
  private static final LocalDate TOMORROW = TODAY.plusDays(1);
  private static final LocalDate NEXT_WEEK = TODAY.plusWeeks(1);
  private static final LocalDate NEXT_MONTH = TODAY.plusMonths(1);

  private Materiel materielAVendre;
  private Compte compteBeneficiaire;
  private Argent prixVente;

  @BeforeEach
  void setUp() {
    materielAVendre =
        new Materiel(
            "Voiture",
            TODAY.minusYears(1),
            TODAY,
            Argent.euro(10_000),
            0.05 // 5% d'appréciation annuelle
            );
    compteBeneficiaire = new Compte("Compte courant", TODAY, Argent.euro(5_000));
    prixVente = Argent.euro(12_000);
  }

  @Test
  void vente_doit_avoir_type_flux() {
    var vente =
        new Vente(
            "Vente voiture",
            TODAY,
            Argent.euro(10_000),
            NEXT_WEEK,
            materielAVendre,
            prixVente,
            compteBeneficiaire);

    assertEquals(FLUX, vente.typeAgregat());
  }

  @Test
  void projection_avant_vente_doit_conserver_les_informations() {
    var vente =
        new Vente(
            "Vente voiture",
            TODAY,
            Argent.euro(10_000),
            NEXT_WEEK,
            materielAVendre,
            prixVente,
            compteBeneficiaire);

    var projection = vente.projectionFuture(TOMORROW);

    assertNotNull(projection);
    assertEquals("Vente voiture", projection.nom());
  }

  @Test
  void projection_apres_vente_doit_retourner_null() {
    var vente =
        new Vente(
            "Vente voiture",
            TODAY,
            Argent.euro(10_000),
            NEXT_WEEK,
            materielAVendre,
            prixVente,
            compteBeneficiaire);

    var projection = vente.projectionFuture(NEXT_MONTH);

    assertNull(projection);
  }

  @Test
  void projection_doit_projeter_aussi_la_possession_sous_jacente() {
    var vente =
        new Vente(
            "Vente voiture",
            TODAY,
            Argent.euro(10_000),
            NEXT_WEEK,
            materielAVendre,
            prixVente,
            compteBeneficiaire);

    var projection = vente.projectionFuture(TOMORROW);
    System.out.println(projection);
    assertNotNull(projection);
    assertEquals("Vente voiture", projection.nom());
    assertEquals(Argent.euro(10_000), projection.valeurComptable());
  }

  @Test
  void constructeur_principal_doit_marquer_la_possession_comme_vendue() {
    assertFalse(materielAVendre.estVendu(TODAY));

    new Vente(
        "Vente voiture",
        TODAY,
        Argent.euro(10_000),
        NEXT_WEEK,
        materielAVendre,
        prixVente,
        compteBeneficiaire);

    assertTrue(materielAVendre.estVendu(NEXT_WEEK));
    assertEquals(NEXT_WEEK, materielAVendre.getDateVente().get());
  }

//  @Test
//  void constructeur_principal_doit_crediter_le_compte_beneficiaire() {
//    new Vente(
//        "Vente voiture",
//        TODAY,
//        Argent.euro(10_000),
//        NEXT_WEEK,
//        materielAVendre,
//        prixVente,
//        compteBeneficiaire);
//
//    assertEquals(17_000, compteBeneficiaire.valeurComptable()); // 5_000 + 12_000
//  }

  @Test
  void constructeur_prive_ne_doit_pas_affecter_la_possession() {
    var projection =
        new Vente(
            "Vente voiture",
            TODAY,
            Argent.euro(10_000),
            NEXT_WEEK,
            materielAVendre.projectionFuture(TODAY),
            prixVente,
            compteBeneficiaire);

    assertFalse(materielAVendre.estVendu(TODAY));
    assertFalse(materielAVendre.estVendu(NEXT_WEEK));
    assertEquals("Vente voiture", projection.nom());
    assertEquals(Argent.euro(10_000), projection.valeurComptable());
  }

  @Test
  void vente_de_possession_deja_vendue_doit_echouer() {
    new Vente(
        "Vente voiture",
        TODAY,
        Argent.euro(10_000),
        NEXT_WEEK,
        materielAVendre,
        prixVente,
        compteBeneficiaire);

    assertThrows(
        IllegalStateException.class,
        () ->
            new Vente(
                "Vente voiture",
                TODAY,
                Argent.euro(10_000),
                NEXT_WEEK,
                materielAVendre,
                prixVente,
                compteBeneficiaire));
  }
}
