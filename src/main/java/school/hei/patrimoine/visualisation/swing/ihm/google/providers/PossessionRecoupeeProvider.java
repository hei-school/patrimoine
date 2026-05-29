package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import static java.util.Comparator.comparing;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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

@RequiredArgsConstructor
public class PossessionRecoupeeProvider {
  private final Set<Compte> casSetComptes;

  @Builder(toBuilder = true)
  public record Meta(Cas planned, Cas done) {}

  @Builder(toBuilder = true)
  public record Filter(
      String nom,
      LocalDate debut,
      LocalDate fin,
      Pagination pagination,
      Set<RecoupementStatus> statuses) {}

  @Builder(toBuilder = true)
  public record Result(List<PossessionRecoupee<Possession>> data, int totalPage) {}

  public Result getList(Meta meta, Filter filter) {
    var recoupeur =
        RecoupeurDePossessions.of(
            filter.debut(),
            filter.fin(),
            meta.planned(),
            meta.done(),
            CompteGetter.make(meta.done(), casSetComptes));

    var all = recoupeur.getPossessionsRecoupees();
    var filtered = getFilteredByFilter(all, filter);
    return getResultByPagination(filtered, filter.pagination());
  }

  private static Result getResultByPagination(
      List<PossessionRecoupee<Possession>> filtered, Pagination pagination) {
    var fromTo = PaginationValue.from(pagination, filtered.size());
    if (fromTo.from() >= filtered.size()) {
      return new Result(List.of(), (int) Math.ceil((double) filtered.size() / pagination.size()));
    }

    int totalPage = (int) Math.ceil((double) filtered.size() / pagination.size());
    return new Result(filtered.subList(fromTo.from(), fromTo.to()), totalPage);
  }

  private static List<PossessionRecoupee<Possession>> getFilteredByFilter(
      Collection<PossessionRecoupee<Possession>> all, Filter filter) {
    return all.stream()
        .filter(
            recoupee -> {
              if (!isIncludeInStatuses(recoupee, filter.statuses())) {
                return false;
              }
              return includeNom(recoupee, filter.nom());
            })
        .sorted(
            comparing((PossessionRecoupee<Possession> recoupee) -> recoupee.possession().t())
                .reversed())
        .toList();
  }

  private static boolean includeNom(PossessionRecoupee<Possession> recoupee, String nom) {
    return nom == null
        || nom.isBlank()
        || recoupee.possession().nom().toLowerCase().contains(nom.toLowerCase());
  }

  private static boolean isIncludeInStatuses(
      PossessionRecoupee<Possession> recoupee, Set<RecoupementStatus> statuses) {
    return statuses == null || statuses.isEmpty() || statuses.contains(recoupee.status());
  }

  private record PaginationValue(int from, int to) {
    static PaginationValue from(Pagination pagination, int totalSize) {
      int from = (pagination.page() - 1) * pagination.size();
      int to = Math.min(from + pagination.size(), totalSize);
      return new PaginationValue(from, to);
    }
  }
}
