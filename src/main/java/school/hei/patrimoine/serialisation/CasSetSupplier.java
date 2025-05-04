package school.hei.patrimoine.serialisation;

import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.cas.pro3Exam.EtudiantCas;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Calendar.APRIL;
import static java.util.Calendar.DECEMBER;
import static school.hei.patrimoine.modele.Argent.ariary;

public class CasSetSupplier implements Supplier<CasSet> {
  @Override
  public CasSet get() {
    var etudiantCas= new EtudiantCas(
            LocalDate.of(2025, APRIL, 7),
            LocalDate.of(2026, DECEMBER, 30),
            Map.of(new Personne("Etudiant"), 1.));
    return new CasSet(Set.of(etudiantCas), ariary(11_900_000 ));
  }
}
