package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.RecoupeurDePossessions;
import school.hei.patrimoine.modele.recouppement.model.CompteGetter;
import school.hei.patrimoine.modele.recouppement.model.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.model.RecoupementStatus;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.model.Pagination;

// TODO: refactor
@RequiredArgsConstructor
public class PossessionRecoupeeProvider {
  private final Set<Compte> casSetComptes;

  @Builder(toBuilder = true)
  public record Meta(Cas planned, Cas done) {}

  @Builder(toBuilder = true)
  public record Filter(
      LocalDate debut,
      LocalDate fin,
      Set<RecoupementStatus> statuses,
      Pagination pagination,
      String filterName) {}

  @Builder(toBuilder = true)
  public record Result(List<PossessionRecoupee<Possession>> possessionRecoupees, int totalPage) {}

  public Result getList(Meta meta, Filter filter) {
    var recoupeur =
        RecoupeurDePossessions.of(
            filter.debut(),
            filter.fin(),
            meta.planned(),
            meta.done(),
            CompteGetter.make(meta.done(), casSetComptes));

    var all = recoupeur.getPossessionsRecoupees();
    var filterName = filter.filterName() == null ? "" : filter.filterName().trim().toLowerCase();

    var filtered =
        all.stream()
            .filter(p -> filter.statuses().contains(p.status()))
            .filter(
                p ->
                    filterName.isBlank()
                        || p.possession().nom().toLowerCase().contains(filterName.toLowerCase()))
            .sorted(
                Comparator.comparing((PossessionRecoupee<Possession> p) -> p.possession().t())
                    .reversed())
            .collect(Collectors.toList());

    int from = (filter.pagination().page() - 1) * filter.pagination().size();
    int to = Math.min(from + filter.pagination().size(), filtered.size());

    if (from >= filtered.size()) {
      return new Result(
          List.of(), (int) Math.ceil((double) filtered.size() / filter.pagination().size()));
    }

    int totalPage = (int) Math.ceil((double) filtered.size() / filter.pagination().size());
    return new Result(filtered.subList(from, to), totalPage);
  }
}
