package school.hei.patrimoine.cas;

import school.hei.patrimoine.cas.pro3Exam.BakoScenario;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Calendar.APRIL;
import static java.util.Calendar.DECEMBER;
import static school.hei.patrimoine.modele.Argent.ariary;

public class BakoCasSetSupplier implements Supplier<CasSet> {
    @Override
    public CasSet get() {
        var bakoScenario = new BakoScenario(
                LocalDate.of(2025, APRIL, 8),
                LocalDate.of(2025, DECEMBER, 30),
                Map.of(new Personne("Bako"), 1.));
        return new CasSet(Set.of(bakoScenario), ariary(13_111_657));
    }
} 