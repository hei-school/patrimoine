package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.RecoupementStatus;
import school.hei.patrimoine.modele.recouppement.RecoupeurDePossessions;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.model.Pagination;

public class PossessionRecoupeeProvider {
  @Builder
  public record Meta(Cas planned, Cas done) {}

  @Builder
  public record Filter(Set<RecoupementStatus> statuses, Pagination pagination) {}

  public List<PossessionRecoupee> getList(Meta meta, Filter filter) {
    var recoupeur =
        RecoupeurDePossessions.of(
            meta.planned().getFinSimulation(),
            meta.planned().patrimoine(),
            meta.done().patrimoine());

    var all = recoupeur.getPossessionsRecoupees();

    var filtered =
        all.stream()
            .filter(p -> filter.statuses().contains(p.status()))
            .sorted(Comparator.comparing((PossessionRecoupee p) -> p.possession().t()).reversed())
            .collect(Collectors.toList());

    int from = (filter.pagination().page() - 1) * filter.pagination().size();
    int to = Math.min(from + filter.pagination().size(), filtered.size());

    if (from >= filtered.size()) return List.of();
    return filtered.subList(from, to);
  }

  public int getTotalPages(Meta meta, Filter filter) {
    var recoupeur =
        RecoupeurDePossessions.of(
            meta.planned().getFinSimulation(),
            meta.planned().patrimoine(),
            meta.done().patrimoine());

    var all = recoupeur.getPossessionsRecoupees();
    var filteredCount = all.stream().filter(p -> filter.statuses().contains(p.status())).count();

    return (int) Math.ceil((double) filteredCount / filter.pagination().size());
  }
}
