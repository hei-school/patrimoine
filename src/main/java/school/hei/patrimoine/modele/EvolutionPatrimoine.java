package school.hei.patrimoine.modele;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

@Getter
@Slf4j
public class EvolutionPatrimoine {
  private final String nom;
  private final Patrimoine patrimoine;
  private final LocalDate debut;
  private final LocalDate fin;
  private final Map<LocalDate, Patrimoine> evolutionJournaliere;
  private final Set<Argent.OperationImpossible> operationsImpossibles;

  public EvolutionPatrimoine(String nom, Patrimoine patrimoine, LocalDate debut, LocalDate fin) {
    this.nom = nom;
    this.patrimoine = patrimoine;
    this.debut = debut;
    this.fin = fin;
    this.evolutionJournaliere = evolutionJournaliere();
    this.operationsImpossibles = operationsImpossibles();
    log.info("OPERATIONS IMPOSSIBLES: {} --> {}\n{}\n\n", debut, fin, operationsImpossibleStr());
  }

  private Set<Argent.OperationImpossible> operationsImpossibles() {
    return patrimoine.possessions().stream()
        .filter(p -> p instanceof Argent)
        .flatMap(p -> ((Argent) p).getOperationsImpossibles().stream())
        .collect(toSet());
  }

  public String operationsImpossibleStr() {
    return operationsImpossibles.stream()
        .sorted(comparing(Argent.OperationImpossible::date))
        .map(Argent.OperationImpossible::toString)
        .collect(joining("\n"));
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

      dates().forEach(d -> serie.add(
          evolutionJournaliere.get(d).possessionParNom(possession.getNom()).getValeurComptable()));
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
