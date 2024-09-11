package school.hei.patrimoine.modele;

import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

@Getter
@Slf4j
public class EvolutionPatrimoine {
  private final String nom;
  private final Patrimoine patrimoine;
  private final LocalDate debut;
  private final LocalDate fin;
  private final Map<LocalDate, Patrimoine> evolutionJournaliere;
  private final Set<FluxJournalier> fluxJournaliers;
  private final Set<FluxJournalier> fluxJournaliersImpossibles;

  public EvolutionPatrimoine(String nom, Patrimoine patrimoine, LocalDate debut, LocalDate fin) {
    this.nom = nom;
    this.patrimoine = patrimoine;
    this.debut = debut;
    this.fin = fin;
    this.evolutionJournaliere = evolutionJournaliere();
    this.fluxJournaliers = fluxJournaliers();
    this.fluxJournaliersImpossibles = fluxJournaliersImpossibles();
  }

  private Set<FluxJournalier> fluxJournaliers() {
    var res = new HashSet<FluxJournalier>();
    evolutionJournaliere.forEach(
        (date, patrimoine) ->
            patrimoine
                .possessions()
                .forEach(
                    p -> {
                      if (p instanceof Argent argent) {
                        var fluxJournalierADate =
                            argent.getFluxArgents().stream()
                                .filter(f -> estDateDOperation(f, date))
                                .collect(toSet());
                        if (!fluxJournalierADate.isEmpty()) {
                          res.add(new FluxJournalier(date, argent, fluxJournalierADate));
                        }
                      }
                    }));
    return res;
  }

  private static boolean estDateDOperation(FluxArgent f, LocalDate t) {
    return f.getDateOperation() == t.getDayOfMonth()
        && (f.getDebut().isBefore(t) || f.getDebut().isEqual(t))
        && (f.getFin().isAfter(t) || f.getFin().isEqual(t));
  }

  public Set<FluxJournalier> fluxJournaliersImpossibles() {
    return fluxJournaliers.stream()
        .filter(fj -> !(fj.argent() instanceof Dette) && fj.argent().getValeurComptable() < 0)
        .collect(toSet());
  }

  private Map<LocalDate, Patrimoine> evolutionJournaliere() {
    Map<LocalDate, Patrimoine> evolutionJournaliere = new HashMap<>();
    serieDates().forEach(date -> evolutionJournaliere.put(date, patrimoine.projectionFuture(date)));
    return evolutionJournaliere;
  }

  public List<LocalDate> serieDates() {
    return debut.datesUntil(fin.plusDays(1)).toList();
  }

  public Map<Possession, List<Integer>> serieValeursComptablesParPossession() {
    var map = new HashMap<Possession, List<Integer>>();

    for (var possession : patrimoine.possessions()) {
      if (possession instanceof FluxArgent) {
        continue; // valeur comptable toujours 0
      }
      var serie = new ArrayList<Integer>();

      serieDates()
          .forEach(
              d ->
                  serie.add(
                      evolutionJournaliere
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

  private static int zeroIfNull(Integer integer) {
    return integer == null ? 0 : integer;
  }

  public List<Integer> serieValeursComptablesPatrimoine() {
    var serie = new ArrayList<Integer>();
    serieDates().forEach(d -> serie.add(evolutionJournaliere.get(d).getValeurComptable()));
    return serie;
  }
}
