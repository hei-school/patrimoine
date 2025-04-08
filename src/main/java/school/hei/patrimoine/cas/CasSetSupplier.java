package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import static school.hei.patrimoine.modele.Argent.ariary;
import school.hei.patrimoine.cas.pro3.EtudiantCas;

public class CasSetSupplier implements Supplier<CasSet> {
  @Override
  public CasSet get() {
            EtudiantCas etudiantCas = new EtudiantCas(
                  LocalDate.of(2025, Month.MARCH,25),
                  LocalDate.of(2026, Month.DECEMBER,31),
                  Map.of(new Personne("Etudiant"),1.));

            return  new CasSet(Set.of(etudiantCas),ariary(2_500_000));
  }
}