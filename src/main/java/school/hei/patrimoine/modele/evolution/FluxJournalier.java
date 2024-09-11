package school.hei.patrimoine.modele.evolution;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;

public record FluxJournalier(LocalDate date, Argent argent, Set<FluxArgent> flux) {
  @Override
  public String toString() {
    return String.format(
        "[%s][%s=%d] %s)", date, argent.getNom(), argent.getValeurComptable(), flux);
  }
}
