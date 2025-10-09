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
  public record Filter(Set<RecoupementStatus> statuses, Pagination pagination, String filterName) {}

  public record Result(List<PossessionRecoupee> possessionRecoupees, int totalPage) {}

  public Result getList(Meta meta, Filter filter) {
    var recoupeur =
        RecoupeurDePossessions.of(
            meta.planned().getFinSimulation(),
            meta.planned().patrimoine(),
            meta.done().patrimoine());

    var all = recoupeur.getPossessionsRecoupees();
    var filterName = filter.filterName() == null ? "" : filter.filterName().trim().toLowerCase();

    var filtered =
        all.stream()
            .filter(p -> filter.statuses().contains(p.status()))
            .filter(
                p ->
                    filterName.isBlank()
                        || p.possession().nom().toLowerCase().contains(filterName.toLowerCase()))
            .sorted(Comparator.comparing((PossessionRecoupee p) -> p.possession().t()).reversed())
            .collect(Collectors.toList());

    int from = (filter.pagination().page() - 1) * filter.pagination().size();
    int to = Math.min(from + filter.pagination().size(), filtered.size());

    int totalPage = (int) Math.ceil((double) filtered.size() / filter.pagination().size());
    return new Result(filtered.subList(from, to), totalPage);
  }
}
