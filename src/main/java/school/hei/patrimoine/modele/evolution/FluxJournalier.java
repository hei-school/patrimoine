package school.hei.patrimoine.modele.evolution;

import static java.util.stream.Collectors.joining;
import static school.hei.patrimoine.modele.evolution.SerieComptableTemporelle.parseMontant;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

public record FluxJournalier(LocalDate date, Compte compte, Set<FluxArgent> flux) {
  @Override
  public String toString() {
    var valeurComptable = compte.valeurComptable();
    return String.format(
        "[%s][%s=%d%s] %s",
        date,
        compte.nom(),
        parseMontant(valeurComptable),
        valeurComptable.devise().symbole(),
        toFluxJournalierString(flux));
  }

  private String toFluxJournalierString(Set<FluxArgent> flux) {
    return flux.stream().map(this::toFluxJournalierString).collect(joining(" "));
  }

  private String toFluxJournalierString(FluxArgent flux) {
    var fluxMensuel = flux.getFluxMensuel();
    return String.format(
        "(%s, %d%s)", flux.nom(), parseMontant((fluxMensuel)), fluxMensuel.devise());
  }
}
