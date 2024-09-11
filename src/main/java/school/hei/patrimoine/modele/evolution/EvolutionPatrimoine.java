package school.hei.patrimoine.modele.evolution;

import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;

@Getter
@Slf4j
public class EvolutionPatrimoine {
  private final String nom;
  private final Patrimoine patrimoine;
  private final LocalDate debut;
  private final LocalDate fin;
  private final Map<LocalDate, Patrimoine> evolutionJournaliere;
  private final SerieComptableTemporelle serieComptableTemporelle;
  private final Set<FluxJournalier> fluxJournaliers;
  private final Set<FluxJournalier> fluxJournaliersImpossibles;

  public EvolutionPatrimoine(String nom, Patrimoine patrimoine, LocalDate debut, LocalDate fin) {
    this.nom = nom;
    this.patrimoine = patrimoine;
    this.debut = debut;
    this.fin = fin;
    this.evolutionJournaliere = evolutionJournaliere();
    this.serieComptableTemporelle = new SerieComptableTemporelle(this);
    this.fluxJournaliers = fluxJournaliers();
    this.fluxJournaliersImpossibles = fluxJournaliersImpossibles();
  }

  private static boolean estDateDOperation(FluxArgent f, LocalDate t) {
    return f.getDateOperation() == t.getDayOfMonth()
        && (f.getDebut().isBefore(t) || f.getDebut().isEqual(t))
        && (f.getFin().isAfter(t) || f.getFin().isEqual(t));
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

  public Set<FluxJournalier> fluxJournaliersImpossibles() {
    return fluxJournaliers.stream()
        .filter(fj -> !(fj.argent() instanceof Dette) && fj.argent().getValeurComptable() < 0)
        .collect(toSet());
  }

  public List<LocalDate> serieDates() {
    return debut.datesUntil(fin.plusDays(1)).toList();
  }

  private Map<LocalDate, Patrimoine> evolutionJournaliere() {
    Map<LocalDate, Patrimoine> evolutionJournaliere = new HashMap<>();
    serieDates().forEach(date -> evolutionJournaliere.put(date, patrimoine.projectionFuture(date)));
    return evolutionJournaliere;
  }
}
