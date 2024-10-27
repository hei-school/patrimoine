package school.hei.patrimoine.modele.evolution;

import static java.util.stream.Collectors.joining;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;

public record FluxJournalier(LocalDate date, Argent argent, Set<FluxArgent> flux) {
  @Override
  public String toString() {
    return String.format(
        "[%s][%s=%d%s] %s",
        date,
        argent.getNom(),
        argent.valeurComptable(),
        argent.getDevise().symbole(),
        toFluxJournalierString(flux));
  }

  private String toFluxJournalierString(Set<FluxArgent> flux) {
    return flux.stream().map(this::toFluxJournalierString).collect(joining(" "));
  }

  private String toFluxJournalierString(FluxArgent flux) {
    return String.format("(%s, %d)", flux.getNom(), flux.getFluxMensuel());
  }
}
