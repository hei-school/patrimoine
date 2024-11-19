package school.hei.patrimoine.conf;

import static school.hei.patrimoine.modele.Argent.ariary;

import java.util.Set;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.cas.EtudiantCas;
import school.hei.patrimoine.modele.Personne;

public class TestUtils {
  public static Cas etudiantCas1() {
    return new EtudiantCas(2024, 2024, new Personne("Jean"), 0);
  }

  public static CasSet casSet1() {
    return new CasSet(Set.of(etudiantCas1()), ariary(500000));
  }

  public static CasSet casSet2() {
    return new CasSet(Set.of(etudiantCas1()), ariary(1200000));
  }
}
