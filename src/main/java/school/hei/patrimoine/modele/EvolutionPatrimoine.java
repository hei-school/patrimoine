package school.hei.patrimoine.modele;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

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
    evolutionJournaliere.forEach((date, patrimoine) ->
        patrimoine.possessions().forEach(p -> {
              if (p instanceof Argent argent) {
                var fluxJournalierADate = argent.getFluxArgents().stream()
                    .filter(f -> estDateDOperation(f, date))
                    .collect(toSet());
                if (!fluxJournalierADate.isEmpty()) {
                  res.add(new FluxJournalier(
                      date,
                      argent,
                      fluxJournalierADate));
                }
              }
            }
        ));
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
    dates().forEach(date -> evolutionJournaliere.put(date, patrimoine.projectionFuture(date)));
    return evolutionJournaliere;
  }

  public Stream<LocalDate> dates() {
    return debut.datesUntil(fin.plusDays(1));
  }
}
