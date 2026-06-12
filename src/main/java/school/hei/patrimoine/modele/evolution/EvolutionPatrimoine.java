package school.hei.patrimoine.modele.evolution;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.modele.series.DateSeries;

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
    this.serieComptableTemporelle = new SerieComptableTemporelle(this, patrimoine.getDevise());
    this.fluxJournaliers = fluxJournaliers();
    this.fluxJournaliersImpossibles = fluxJournaliersImpossibles();
  }

  private static boolean estDateDOperation(FluxArgent f, LocalDate t) {
    return t.getDayOfMonth() == Math.min(f.getDateOperation(), t.lengthOfMonth())
        && (f.getDebut().isBefore(t) || f.getDebut().isEqual(t))
        && (f.getFin().isAfter(t) || f.getFin().isEqual(t));
  }

  private Set<FluxJournalier> fluxJournaliers() {
    return fluxJournaliers(true);
  }

  public Set<FluxJournalier> fluxJournaliers(boolean withCorrection) {
    var res = new HashSet<FluxJournalier>();
    evolutionJournaliere.forEach(
        (date, patrimoine) -> {
          var possessions =
              withCorrection
                  ? patrimoine.getPossessions()
                  : withoutCorrections(patrimoine.getPossessions());
          possessions.forEach(p -> fluxDuJour(date, p, res));
        });
    return res;
  }

  private Set<Possession> withoutCorrections(Set<Possession> possessions) {
    return possessions.stream()
        .filter(
            not(
                possession ->
                    possession instanceof FluxArgentCorrection
                        || possession instanceof Correction
                        || possession instanceof CompteCorrection))
        .collect(toSet());
  }

  private static void fluxDuJour(LocalDate date, Possession p, HashSet<FluxJournalier> res) {
    if (p instanceof Compte compte) {
      fluxDuJour(date, compte, res);
    } else if (p instanceof CompteCorrection compteCorrection) {
      fluxDuJour(date, compteCorrection.getCompte(), res);
    }
  }

  private static void fluxDuJour(LocalDate date, Compte compte, HashSet<FluxJournalier> res) {
    var fluxJournalierADate =
        compte.getFluxArgents().stream().filter(f -> estDateDOperation(f, date)).collect(toSet());
    if (!fluxJournalierADate.isEmpty()) {
      res.add(new FluxJournalier(date, compte, fluxJournalierADate));
    }
  }

  public Set<FluxJournalier> fluxJournaliersImpossibles() {
    return fluxJournaliersImpossibles(true);
  }

  public Set<FluxJournalier> fluxJournaliersImpossibles(boolean withCorrection) {
    var flux = withCorrection ? fluxJournaliers : fluxJournaliers(false);
    return flux.stream()
        .filter(fj -> !(fj.compte() instanceof Dette) && fj.compte().valeurComptable().lt(0))
        .collect(toSet());
  }

  public List<LocalDate> serieDates() {
    return DateSeries.byInterval(debut, fin);
  }

  private Map<LocalDate, Patrimoine> evolutionJournaliere() {
    Map<LocalDate, Patrimoine> evolutionJournaliere = new HashMap<>();
    DateSeries.byInterval(debut, fin)
        .forEach(date -> evolutionJournaliere.put(date, patrimoine.projectionFuture(date)));
    return evolutionJournaliere;
  }
}
