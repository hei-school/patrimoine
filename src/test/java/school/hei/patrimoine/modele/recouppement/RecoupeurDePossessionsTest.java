package school.hei.patrimoine.modele.recouppement;

import static java.time.Month.*;
import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.recouppement.RecoupementStatus.IMPREVU;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.CompteCorrection;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.CompteGetterFactory.CompteGetter;

class RecoupeurDePossessionsTest {
  private static Compte COMPTE_PREVU;
  private static Compte COMPTE_REALISE;
  private static final LocalDate DEBUT = LocalDate.of(2025, 1, 1);
  private static final LocalDate FIN = LocalDate.of(2025, 12, 31);

  private static final CompteGetter COMPTE_GETTER = ignored -> COMPTE_PREVU;
  private static final LocalDate AU_01_JANVIER_2025 = LocalDate.of(2025, JANUARY, 1);
  private static final LocalDate AU_12_DECEMBRE_2025 = LocalDate.of(2025, DECEMBER, 12);

  @BeforeEach
  void setUp(){
    COMPTE_PREVU = new Compte("comptePersonnel", AU_01_JANVIER_2025, ariary(200));
    COMPTE_REALISE = new Compte("comptePersonnel", AU_01_JANVIER_2025, ariary(200));
  }

  @Test
  void sans_correction_si_prevu_et_realise_sont_pareil() {
    Set<Possession> prevus = Set.of(
      COMPTE_PREVU,
      new FluxArgent("salaire", COMPTE_PREVU,  AU_01_JANVIER_2025, ariary(200))
    );

    Set<Possession> realises = Set.of(
      COMPTE_REALISE ,
      new FluxArgent("salaire", COMPTE_REALISE,  AU_01_JANVIER_2025, ariary(200))
    );

    var subject = subject(prevus, realises);

    assertTrue(subject.getCorrections().isEmpty());
    assertEquals(2, subject.getPossessionsExecutes().size());
  }

  @Test
  void sans_correction_si_prevu_et_realise_sont_pareil_avec_interval() {
   Set<Possession> prevus = Set.of(
     COMPTE_PREVU,
     new FluxArgent("salaire", COMPTE_PREVU, AU_01_JANVIER_2025, AU_12_DECEMBRE_2025, 31, ariary(200))
   );

   Set<Possession> realises = Set.of(
     COMPTE_REALISE ,
     new FluxArgent("salaire", COMPTE_REALISE, AU_01_JANVIER_2025, AU_12_DECEMBRE_2025, 31, ariary(200))
   );

   var subject = subject(prevus, realises);

   assertTrue(subject.getCorrections().isEmpty());
   assertEquals(12, subject.getPossessionsExecutes().size());
   assertEquals(12, subject.getPossessionsExecutesSansCorrections().size());
  }

  @Test
  void imprevu_corrections() {
    Set<Possession> prevus = Set.of(COMPTE_PREVU);
    Set<Possession> realises = Set.of(
      COMPTE_REALISE ,
      new FluxArgent("salaire", COMPTE_REALISE, AU_01_JANVIER_2025, ariary(200))
    );

    var subject = subject(prevus, realises);

    assertEquals(1, subject.getCorrections().size());
    assertEquals(1, subject.getPossessionsExecutesAvecCorrections().size());
    assertEquals(1, subject.getByStatus(IMPREVU).size());
    assertEquals(2, subject.getPossessionsExecutes().size());

    var imprevu = new ArrayList<>(subject.getByStatus(IMPREVU)).getFirst();
    assertEquals(1, imprevu.corrections().size());
    assertEquals("salaire", imprevu.possession().nom());
  }


  private static RecoupeurDePossessions subject(Set<Possession> prevus, Set<Possession> realises){
    return new RecoupeurDePossessions(DEBUT, FIN, prevus, realises, COMPTE_GETTER);
  }

  private static List<Possession> sortedWithoutCompteCorrectionsPossessions(
      Set<Possession> possessions) {
    return possessions.stream()
        .filter(not(p -> p instanceof CompteCorrection))
        .sorted(Comparator.comparing(Possession::nom))
        .toList();
  }
}
