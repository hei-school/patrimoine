package school.hei.patrimoine.modele.recouppement;

import static java.time.Month.*;
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
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

class RecoupeurDeCasSetTest {
  @Test
  void sans_correction_si_prevu_et_réel_sont_identiques() {
    var debut = LocalDate.of(2025, JANUARY, 1);
    var fin = LocalDate.of(2025, MARCH, 1);

    var doneCompte = new Compte("comptePersonnel", debut, ariary(0));
    var plannedCompte = new Compte("comptePersonnel", debut, ariary(0));

    var plannedCas =
        new Cas(debut, fin, Map.of()) {
          @Override
          protected void init() {}

          @Override
          protected void suivi() {}

          @Override
          public Set<Possession> possessions() {
            return Set.of(
                plannedCompte, new FluxArgent("salaire", plannedCompte, fin, ariary(200)));
          }

          @Override
          protected Devise devise() {
            return MGA;
          }

          @Override
          protected String nom() {
            return "Zety";
          }
        };

    var doneCas =
        new Cas(debut, fin, Map.of()) {
          @Override
          protected void init() {}

          @Override
          protected void suivi() {}

          @Override
          public Set<Possession> possessions() {
            return Set.of(doneCompte, new FluxArgent("salaire", doneCompte, fin, ariary(200)));
          }

          @Override
          protected Devise devise() {
            return MGA;
          }

          @Override
          protected String nom() {
            return "Zety";
          }
        };

    var plannedSet = new CasSet(Set.of(plannedCas), ariary(0));
    var doneSet = new CasSet(Set.of(doneCas), ariary(0));
    var donePatrimoine = doneSet.set().iterator().next().patrimoine();

    var subject = RecoupeurDeCasSet.of(plannedSet, doneSet);
    var recouped = subject.getRecouped();
    var recoupedCas = recouped.set().iterator().next();

    assertEquals(
        donePatrimoine.getPossessions().size(), recoupedCas.patrimoine().getPossessions().size());
  }

  @Test
  void ajoute_correction_si_valeurs_différentes() {
    var debut = LocalDate.of(2025, JANUARY, 1);
    var fin = LocalDate.of(2025, MARCH, 1);

    var doneCompte = new Compte("comptePersonnel", debut, ariary(100));
    var plannedCompte = new Compte("comptePersonnel", debut, ariary(100));

    var plannedCas =
        new Cas(debut, fin, Map.of()) {
          @Override
          protected void init() {}

          @Override
          protected void suivi() {}

          @Override
          public Set<Possession> possessions() {
            return Set.of(
                plannedCompte, new FluxArgent("salaire", plannedCompte, fin, ariary(300)));
          }

          @Override
          protected Devise devise() {
            return MGA;
          }

          @Override
          protected String nom() {
            return "Zety";
          }
        };

    var doneCas =
        new Cas(debut, fin, Map.of()) {
          @Override
          protected void init() {}

          @Override
          protected void suivi() {}

          @Override
          public Set<Possession> possessions() {
            return Set.of(doneCompte, new FluxArgent("salaire", doneCompte, fin, ariary(200)));
          }

          @Override
          protected Devise devise() {
            return MGA;
          }

          @Override
          protected String nom() {
            return "Zety";
          }
        };

    var plannedSet = new CasSet(Set.of(plannedCas), ariary(0));
    var doneSet = new CasSet(Set.of(doneCas), ariary(0));
    var donePatrimoine = doneSet.set().iterator().next().patrimoine();

    var subject = RecoupeurDeCasSet.of(plannedSet, doneSet);
    var recouped = subject.getRecouped();
    var recoupedCas = recouped.set().iterator().next();

    assertEquals(
        donePatrimoine.getPossessions().size() + 2,
        recoupedCas.patrimoine().getPossessions().size());
  }
}
