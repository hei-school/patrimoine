package school.hei.patrimoine.modele;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
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
  private final Set<FluxImpossibles> fluxImpossibles;

  public EvolutionPatrimoine(String nom, Patrimoine patrimoine, LocalDate debut, LocalDate fin) {
    this.nom = nom;
    this.patrimoine = patrimoine;
    this.debut = debut;
    this.fin = fin;
    this.evolutionJournaliere = evolutionJournaliere();
    this.fluxImpossibles = fluxImpossibles();
    log.info("FLUX IMPOSSIBLES: {} --> {}\n{}\n\n", debut, fin, fluxImpossiblesStr());
  }

  private Set<FluxImpossibles> fluxImpossibles() {
    var res = new HashSet<FluxImpossibles>();
    evolutionJournaliere.forEach(
        (date, patrimoine) ->
            patrimoine
                .possessions()
                .forEach(
                    p -> {
                      if (p instanceof Argent argent
                          && !(p instanceof Dette)
                          && p.getValeurComptable() < 0) {
                        var fluxImpossibles =
                            argent.getFluxArgents().stream()
                                .filter(f -> f.getDateOperation() == date.getDayOfMonth())
                                .filter(
                                    f -> f.getDebut().isBefore(date) || f.getDebut().isEqual(date))
                                .filter(f -> f.getFin().isAfter(date) || f.getFin().isEqual(date))
                                .collect(toSet());
                        if (!fluxImpossibles.isEmpty()) {
                          res.add(
                              new FluxImpossibles(
                                  date,
                                  argent.getNom(),
                                  argent.getValeurComptable(),
                                  fluxImpossibles));
                        }
                      }
                    }));
    return res;
  }

  public String fluxImpossiblesStr() {
    return fluxImpossibles.stream()
        .sorted(comparing(FluxImpossibles::date))
        .map(FluxImpossibles::toString)
        .collect(joining("\n\n"));
  }

  private Map<LocalDate, Patrimoine> evolutionJournaliere() {
    Map<LocalDate, Patrimoine> evolutionJournaliere = new HashMap<>();
    dates().forEach(date -> evolutionJournaliere.put(date, patrimoine.projectionFuture(date)));
    return evolutionJournaliere;
  }

  public Map<Possession, List<Integer>> serieValeursComptablesParPossession() {
    var map = new HashMap<Possession, List<Integer>>();

    for (var possession : patrimoine.possessions()) {
      if (possession instanceof FluxArgent) {
        continue; // valeur comptable toujours 0
      }
      var serie = new ArrayList<Integer>();

      dates()
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

  public List<Integer> serieValeursComptablesPatrimoine() {
    var serie = new ArrayList<Integer>();
    dates().forEach(d -> serie.add(evolutionJournaliere.get(d).getValeurComptable()));
    return serie;
  }

  public Stream<LocalDate> dates() {
    return debut.datesUntil(fin.plusDays(1));
  }
}
