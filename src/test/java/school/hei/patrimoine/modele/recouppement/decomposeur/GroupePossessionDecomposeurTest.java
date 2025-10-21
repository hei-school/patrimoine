package school.hei.patrimoine.modele.recouppement.decomposeur;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.GroupePossession;

class GroupePossessionDecomposeurTest {
  private static final LocalDate au12Janvier2025 = LocalDate.of(2025, JANUARY, 12);

  private static final GroupePossessionDecomposeur subject =
      new GroupePossessionDecomposeur(au12Janvier2025);

  @Test
  void decompose_groupe_ok() {
    var groupePossession =
        new GroupePossession(
            "groupe",
            MGA,
            au12Janvier2025,
            Set.of(
                new Compte("BMOI", au12Janvier2025, ariary(500_000)),
                new Compte("BOA", au12Janvier2025, ariary(100_000))));

    var actual = subject.apply(groupePossession);

    assertEquals(2, actual.size());
  }
}
