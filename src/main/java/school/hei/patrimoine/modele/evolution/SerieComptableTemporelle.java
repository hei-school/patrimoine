package school.hei.patrimoine.modele.evolution;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

@AllArgsConstructor
public class SerieComptableTemporelle {
  private final EvolutionPatrimoine ep;

  private static int zeroIfNull(Integer integer) {
    return integer == null ? 0 : integer;
  }

  public List<LocalDate> serieDates() {
    return ep.serieDates();
  }

  public Map<Possession, List<Integer>> serieValeursComptablesParPossession() {
    var map = new HashMap<Possession, List<Integer>>();

    for (var possession : ep.getPatrimoine().possessions()) {
      if (possession instanceof FluxArgent) {
        continue; // valeur comptable toujours 0
      }
      var serie = new ArrayList<Integer>();

      serieDates()
          .forEach(
              d ->
                  serie.add(
                      ep.getEvolutionJournaliere()
                          .get(d)
                          .possessionParNom(possession.getNom())
                          .getValeurComptable()));
      map.put(possession, serie);
    }
    return map;
  }

  public List<Integer> serieParPossessionsFiltrées(Predicate<Possession> filtre) {
    var serieParPossession = serieValeursComptablesParPossession();
    List<List<Integer>> series = new ArrayList<>();
    serieParPossession.forEach(
        (p, serie) -> {
          if (filtre.test(p)) {
            series.add(serie);
          }
        });

    if (series.isEmpty()) {
      return List.of();
    }
    return series.stream()
        .reduce(
            Arrays.stream(new Integer[series.getFirst().size()]).toList(),
            this::ajouteMembreAMembre);
  }

  private List<Integer> ajouteMembreAMembre(List<Integer> l1, List<Integer> l2) {
    List<Integer> res = new ArrayList<>();
    for (int i = 0; i < l1.size(); i++) {
      res.add(zeroIfNull(l1.get(i)) + zeroIfNull(l2.get(i)));
    }
    return res;
  }

  public List<Integer> serieValeursComptablesPatrimoine() {
    var serie = new ArrayList<Integer>();
    serieDates().forEach(d -> serie.add(ep.getEvolutionJournaliere().get(d).getValeurComptable()));
    return serie;
  }
}
