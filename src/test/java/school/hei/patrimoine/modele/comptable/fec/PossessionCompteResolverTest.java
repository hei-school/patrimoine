package school.hei.patrimoine.modele.comptable.fec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.fec.PossessionCompteResolver.resolve;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

class PossessionCompteResolverTest {
  private Compte compte;

  @BeforeEach
  void setup() {
    compte = new Compte("Compte principal", LocalDate.of(2026, 1, 1), ariary(5_000_000));
  }

  @Test
  void should_handle_flux_argent_operation() {
    var flux =
        new FluxArgent(
            "Entrée argent",
            compte,
            LocalDate.of(2026, 3, 10),
            LocalDate.of(2026, 3, 10),
            5,
            ariary(100_000));

    var comptes = resolve(flux);
    var debiteur = comptes.compteDébiteur().compte();
    var crediteur = comptes.compteCréditeur().compte();

    assertEquals(flux.getCompte(), debiteur);
    assertEquals("Compte d'attente", crediteur.nom());
  }

  @Test
  void should_handle_transfert_argent_operation() {
    var destinataire = new Compte("Compte épargne", LocalDate.of(2025, 1, 1), ariary(700_000));
    var transfert =
        new TransfertArgent(
            "Transfert",
            compte,
            destinataire,
            LocalDate.of(2026, 3, 10),
            LocalDate.of(2026, 3, 10),
            5,
            ariary(1_000_000));

    var comptes = resolve(transfert);
    var debiteur = comptes.compteDébiteur().compte();
    var crediteur = comptes.compteCréditeur().compte();

    assertEquals(transfert.getVersCompte(), debiteur);
    assertEquals(transfert.getDepuisCompte(), crediteur);
  }

  @Test
  void should_handle_compte_operation() {
    var compte = new Compte("Compte épargne", LocalDate.of(2025, 1, 1), ariary(700_000));

    var comptes = resolve(compte);
    var debiteur = comptes.compteDébiteur().compte();

    assertEquals(compte, debiteur);
  }

  @Test
  void should_handle_remboursement_dette_operation() {
    var compteRembourseur = new Compte("Rembourseur", LocalDate.of(2025, 6, 1), ariary(8_000_000));
    var dette = new Dette("Dette", LocalDate.of(2024, 7, 1), ariary(-500_000));
    var creance = new Creance("Creance", LocalDate.of(2024, 7, 1), ariary(500_000));
    var remboursement =
        new RemboursementDette(
            "Rembourserment dette",
            compteRembourseur,
            compte,
            dette,
            creance,
            LocalDate.of(2026, 1, 31),
            ariary(500_000));

    var comptes = resolve(remboursement);
    var debiteur = comptes.compteDébiteur().compte();
    var crediteur = comptes.compteCréditeur().compte();

    assertEquals(remboursement.getRemboursé(), debiteur);
    assertEquals(remboursement.getRembourseur(), crediteur);
  }

  @Test
  void should_handle_achat_materiel_au_comptant_operation() {
    var achat =
        new AchatMaterielAuComptant(
            "Achat ordinateur", LocalDate.of(2026, 1, 31), ariary(500_000), 2.4, compte);
    var comptes = resolve(achat);
    var debiteur = comptes.compteDébiteur().compte();
    var crediteur = comptes.compteCréditeur().compte();

    assertEquals("Matériel", debiteur.nom());
    assertEquals(achat.getFinanceur(), crediteur);
  }
}
