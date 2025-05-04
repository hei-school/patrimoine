package school.hei.patrimoine.cas;

import school.hei.patrimoine.cas.pro3Exam.TianaScenario;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static school.hei.patrimoine.modele.Argent.ariary;

public class TianaCasSetSupplier implements Supplier<CasSet> {
    @Override
    public CasSet get() {
        var tianaScenario = new TianaScenario(
                LocalDate.of(2025, 4, 8),
                LocalDate.of(2026, 3, 31),
                Map.of(new Personne("Tiana"), 1.));
        return new CasSet(Set.of(tianaScenario), ariary(164_780_821));
    }
} 