package school.hei.patrimoine.modele.vente;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.*;

class ValeurMarchePossessionProjectionTest {
  private Compte compte;

  private static final Argent VALEUR_COMPTABLE = ariary(10_000_000);
  private static final Argent VALEUR_MARCHE = ariary(12_000_000);

  private static final LocalDate T0 = LocalDate.of(2024, JANUARY, 1);
  private static final LocalDate T_VM = LocalDate.of(2024, JANUARY, 15);
  private static final LocalDate T_FUT = LocalDate.of(2025, JANUARY, 1);

  @BeforeEach
  void setUp() {
    compte = new Compte("compte", T0, ariary(0));
  }

  @Test
  void materiel_projectionFuture_conserve_valeursMarche() {
    var materiel = new Materiel("Voiture", T0, T0, VALEUR_COMPTABLE, 0.0);
    new ValeurMarche(materiel, T_VM, VALEUR_MARCHE);

    var projection = (Materiel) materiel.projectionFuture(T_FUT);

    assertEquals(VALEUR_MARCHE, projection.getValeurMarche(T_FUT));
    assertEquals(1, projection.historiqueValeurMarche().size());
  }

  @Test
  void materiel_projectionFuture_conserve_plusieursValeursMarche() {
    var materiel = new Materiel("Voiture", T0, T0, VALEUR_COMPTABLE, 0.0);
    new ValeurMarche(materiel, T_VM, VALEUR_MARCHE);
    new ValeurMarche(materiel, T_VM.plusMonths(3), ariary(14_000_000));

    var projection = (Materiel) materiel.projectionFuture(T_FUT);

    assertEquals(2, projection.historiqueValeurMarche().size());
  }

  @Test
  void compte_projectionFuture_conserve_valeursMarche() {
    var compteAvecVm = new Compte("compteVm", T0, ariary(5_000_000));

    new ValeurMarche(compteAvecVm, T_VM, VALEUR_MARCHE);
    var projection = compteAvecVm.projectionFuture(T_FUT);

    assertEquals(VALEUR_MARCHE, projection.getValeurMarche(T_FUT));
    assertEquals(1, projection.historiqueValeurMarche().size());
  }

  @Test
  void creance_ajouterValeurMarche_leveUnsupportedOperationException() {
    var creance = new Creance("maCreance", T0, ariary(3_000_000));

    assertThrows(
        UnsupportedOperationException.class, () -> new ValeurMarche(creance, T_VM, VALEUR_MARCHE));
  }

  @Test
  void creance_projectionFuture_historiqueVideParDefaut() {
    var creance = new Creance("maCreance", T0, ariary(3_000_000));

    assertTrue(creance.projectionFuture(T_FUT).historiqueValeurMarche().isEmpty());
  }

  @Test
  void dette_ajouterValeurMarche_leveUnsupportedOperationException() {
    var dette = new Dette("maDette", T0, ariary(-3_000_000));

    assertThrows(
        UnsupportedOperationException.class, () -> new ValeurMarche(dette, T_VM, VALEUR_MARCHE));
  }

  @Test
  void dette_projectionFuture_historiqueVideParDefaut() {
    var dette = new Dette("maDette", T0, ariary(-3_000_000));

    assertTrue(dette.projectionFuture(T_FUT).historiqueValeurMarche().isEmpty());
  }

  @Test
  void fluxArgent_projectionFuture_conserve_valeursMarche() {
    var compteFlux = new Compte("compteFlux", T0, ariary(1_000_000));
    var flux = new FluxArgent("Loyer", compteFlux, T0, T_FUT, 1, ariary(-200_000));

    var projection = flux.projectionFuture(T_FUT);
    assertTrue(projection.historiqueValeurMarche().isEmpty());
  }

  @Test
  void groupePossession_projectionFuture_conserve_valeursMarche_du_groupe() {
    var materiel = new Materiel("Ordi", T0, T0, VALEUR_COMPTABLE, 0.0);
    var flux = new FluxArgent("Achat", compte, T0, T0, 1, VALEUR_COMPTABLE.mult(-1));
    var groupe =
        new GroupePossession("MonGroupe", VALEUR_COMPTABLE.devise(), T0, Set.of(materiel, flux));

    new ValeurMarche(groupe, T_VM, VALEUR_MARCHE);
    var projection = groupe.projectionFuture(T_FUT);

    assertEquals(VALEUR_MARCHE, projection.getValeurMarche(T_FUT));
    assertEquals(1, projection.historiqueValeurMarche().size());
  }

  @Test
  void groupePossession_projectionFuture_conserve_valeursMarche_des_membres() {
    var materiel = new Materiel("Ordi", T0, T0, VALEUR_COMPTABLE, 0.0);
    new ValeurMarche(materiel, T_VM, VALEUR_MARCHE);
    var flux = new FluxArgent("Achat", compte, T0, T0, 1, VALEUR_COMPTABLE.mult(-1));
    var groupe =
        new GroupePossession("MonGroupe", VALEUR_COMPTABLE.devise(), T0, Set.of(materiel, flux));

    var projection = groupe.projectionFuture(T_FUT);

    var materielProjecte =
        projection.getPossessions().stream()
            .filter(p -> p instanceof Materiel)
            .findFirst()
            .orElseThrow();
    assertEquals(1, materielProjecte.historiqueValeurMarche().size());
  }

  @Test
  void correction_projectionFuture_conserve_valeursMarche() {
    var compteCorr = new Compte("compteCorr", T0, ariary(5_000_000));
    var flux = new FluxArgent("FluxCorr", compteCorr, T0, T_FUT, 1, ariary(-100_000));

    var correction = new Correction(flux);
    var projection = correction.projectionFuture(T_FUT);
    assertTrue(projection.historiqueValeurMarche().isEmpty());
  }

  @Test
  void compteCorrection_projectionFuture_conserve_valeursMarche() {
    var materiel = new Materiel("Ordi", T0, T0, VALEUR_COMPTABLE, 0.0);

    var compteCorrection = materiel.getCompteCorrection();
    var projection = compteCorrection.projectionFuture(T_FUT);

    assertTrue(projection.historiqueValeurMarche().isEmpty());
  }

  @Test
  void transfertArgent_projectionFuture_conserve_valeursMarche() {
    var compteDest = new Compte("dest", T0, ariary(0));
    var compteSource = new Compte("source", T0, ariary(5_000_000));
    var transfert = new TransfertArgent("Transfert", compteSource, compteDest, T0, ariary(500_000));
    var projection = transfert.projectionFuture(T_FUT);
    assertTrue(projection.historiqueValeurMarche().isEmpty());
  }

  @Test
  void remboursementDette_projectionFuture_ne_leve_pas_exception() {
    var dette = new Dette("dette", T0, ariary(-500_000));
    var creance = new Creance("creance", T0, ariary(500_000));
    var rembourse = new Compte("rembourse", T0, ariary(0));
    var rembourseur = new Compte("rembourseur", T0, ariary(1_000_000));
    var remboursement =
        new RemboursementDette(
            "Remboursement", rembourseur, rembourse, dette, creance, T0, ariary(500_000));

    var projection = remboursement.projectionFuture(T_FUT);
    assertTrue(projection.historiqueValeurMarche().isEmpty());
  }

  @Test
  void achatMaterielAuComptant_projectionFuture_conserve_valeursMarche_du_materiel_interne() {
    var financeur = new Compte("financeur", T0, ariary(10_000_000));
    var achat = new AchatMaterielAuComptant("Voiture", T0, VALEUR_COMPTABLE, -0.1, financeur);

    var projection = achat.projectionFuture(T_FUT);
    assertTrue(projection.historiqueValeurMarche().isEmpty());
  }
}
