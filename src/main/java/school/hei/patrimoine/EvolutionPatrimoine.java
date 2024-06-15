package school.hei.patrimoine;

import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
public class EvolutionPatrimoine {
  private final String nom;
  private final Patrimoine patrimoine;
  private final LocalDate debut;
  private final LocalDate fin;
  private final Map<LocalDate, Patrimoine> evolutionJournaliere;

  public EvolutionPatrimoine(String nom, Patrimoine patrimoine, LocalDate debut, LocalDate fin) {
    this.nom = nom;
    this.patrimoine = patrimoine;
    this.debut = debut;
    this.fin = fin;
    this.evolutionJournaliere = evolutionJournaliere(patrimoine, debut, fin);
  }

  private Map<LocalDate, Patrimoine> evolutionJournaliere(Patrimoine patrimoine, LocalDate debut, LocalDate fin) {
    Map<LocalDate, Patrimoine> evolutionJournaliere = new HashMap<>();
    debut
        .datesUntil(fin.plusDays(1))
        .forEach(date -> evolutionJournaliere.put(date, patrimoine.projectionFuture(date)));

    return evolutionJournaliere;
  }
}
