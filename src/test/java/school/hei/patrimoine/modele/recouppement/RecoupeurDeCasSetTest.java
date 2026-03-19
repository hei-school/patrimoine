package school.hei.patrimoine.modele.recouppement;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

class RecoupeurDeCasSetTest {
  private static final Personne POSSESSEUR = new Personne("testeur");
  private static final LocalDate DATE = LocalDate.of(2025, JANUARY, 1);
  private static final LocalDate FIN = LocalDate.of(2025, JANUARY, 31);

  /** Fabrique un Cas minimal avec le nom et les possessions fournis. */
  private static Cas casWith(String nom, Set<Possession> possessions) {
    return new Cas(DATE, FIN, Map.of(POSSESSEUR, 1.)) {
      @Override
      protected Devise devise() {
        return MGA;
      }

      @Override
      protected String nom() {
        return nom;
      }

      @Override
      protected void init() {}

      @Override
      protected void suivi() {}

      @Override
      public Set<Possession> possessions() {
        return possessions;
      }
    };
  }

  private static CasSet casSetOf(Cas... cas) {
    return new CasSet(Set.of(cas), ariary(0));
  }

  @Test
  void getRecouped_retourne_autant_de_cas_que_done() {
    var compte = new Compte("banque", DATE, ariary(1000));
    var done = casSetOf(casWith("patrimoine-A", Set.of(compte)));
    var planned = casSetOf(casWith("patrimoine-A", Set.of(compte)));

    var result = RecoupeurDeCasSet.of(DATE, FIN, planned, done).getRecouped();

    assertEquals(1, result.set().size());
  }

  @Test
  void getRecouped_preserve_objectifFinal_de_done() {
    var objectif = ariary(999_000);
    var compte = new Compte("banque", DATE, ariary(500));
    var done = new CasSet(Set.of(casWith("patrimoine-A", Set.of(compte))), objectif);
    var planned = new CasSet(Set.of(casWith("patrimoine-A", Set.of(compte))), ariary(0));

    var result = RecoupeurDeCasSet.of(DATE, FIN, planned, done).getRecouped();

    assertEquals(objectif, result.objectifFinal());
  }

  @Test
  void getRecouped_preserve_nom_du_patrimoine() {
    var compte = new Compte("banque", DATE, ariary(0));
    var done = casSetOf(casWith("mon-patrimoine", Set.of(compte)));
    var planned = casSetOf(casWith("mon-patrimoine", Set.of(compte)));

    var result = RecoupeurDeCasSet.of(DATE, FIN, planned, done).getRecouped();
    var nomRetourne = result.set().iterator().next().patrimoine().getNom();

    assertEquals("mon-patrimoine", nomRetourne);
  }

  @Test
  void getRecouped_preserve_devise_du_patrimoine() {
    var compte = new Compte("banque", DATE, ariary(0));
    var done = casSetOf(casWith("patrimoine-A", Set.of(compte)));
    var planned = casSetOf(casWith("patrimoine-A", Set.of(compte)));

    var result = RecoupeurDeCasSet.of(DATE, FIN, planned, done).getRecouped();
    var devise = result.set().iterator().next().patrimoine().getDevise();

    assertEquals(MGA, devise);
  }

  @Test
  void getRecouped_possessions_sont_sans_compteCorrections() {
    var compte = new Compte("banque", DATE, ariary(1000));
    var done = casSetOf(casWith("patrimoine-A", Set.of(compte)));
    var planned = casSetOf(casWith("patrimoine-A", Set.of(compte)));

    var result = RecoupeurDeCasSet.of(DATE, FIN, planned, done).getRecouped();

    var possessions = result.set().iterator().next().possessions();
    assertTrue(
        possessions.stream()
            .noneMatch(p -> p instanceof school.hei.patrimoine.modele.possession.CompteCorrection),
        "Les CompteCorrection doivent être filtrées par withoutCompteCorrections");
  }

  @Test
  void getRecouped_fonctionne_avec_plusieurs_cas() {
    var compteA = new Compte("banqueA", DATE, ariary(100));
    var compteB = new Compte("banqueB", DATE, ariary(200));
    var casA_done = casWith("patrimoine-A", Set.of(compteA));
    var casB_done = casWith("patrimoine-B", Set.of(compteB));
    var casA_planned = casWith("patrimoine-A", Set.of(compteA));
    var casB_planned = casWith("patrimoine-B", Set.of(compteB));

    var done = casSetOf(casA_done, casB_done);
    var planned = casSetOf(casA_planned, casB_planned);

    var result = RecoupeurDeCasSet.of(DATE, FIN, planned, done).getRecouped();

    assertEquals(2, result.set().size());
  }

  @Test
  void getRecouped_leve_exception_si_cas_planned_introuvable() {
    var compte = new Compte("banque", DATE, ariary(0));
    var done = casSetOf(casWith("patrimoine-X", Set.of(compte)));
    var planned = casSetOf(casWith("patrimoine-Y", Set.of(compte)));

    var subject = RecoupeurDeCasSet.of(DATE, FIN, planned, done);
    var ex = assertThrows(IllegalArgumentException.class, subject::getRecouped);

    assertTrue(
        ex.getMessage().contains("patrimoine-X"),
        "Le message doit mentionner le nom du cas introuvable");
  }

  @Test
  void getRecouped_appele_deux_fois_retourne_des_cassets_equivalents() {
    var compte = new Compte("banque", DATE, ariary(0));
    var done = casSetOf(casWith("patrimoine-A", Set.of(compte)));
    var planned = casSetOf(casWith("patrimoine-A", Set.of(compte)));

    var subject = RecoupeurDeCasSet.of(DATE, FIN, planned, done);
    var first = subject.getRecouped();
    var second = subject.getRecouped();

    assertEquals(first.set().size(), second.set().size());
    assertEquals(first.objectifFinal(), second.objectifFinal());
  }

  @Test
  void getRecouped_cas_avec_fluxArgent_ne_leve_pas_dexception() {
    var compte = new Compte("banque", DATE, ariary(0));
    var flux = new FluxArgent("salaire", compte, DATE, ariary(200));

    var done = casSetOf(casWith("patrimoine-A", Set.of(compte, flux)));
    var planned = casSetOf(casWith("patrimoine-A", Set.of(compte, flux)));

    assertDoesNotThrow(() -> RecoupeurDeCasSet.of(DATE, FIN, planned, done).getRecouped());
  }

  @Test
  void of_retourne_instance_non_nulle() {
    var compte = new Compte("banque", DATE, ariary(0));
    var done = casSetOf(casWith("patrimoine-A", Set.of(compte)));
    var planned = casSetOf(casWith("patrimoine-A", Set.of(compte)));

    assertNotNull(RecoupeurDeCasSet.of(DATE, FIN, planned, done));
  }
}
