package school.hei.patrimoine.modele;

import lombok.Getter;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
    this.evolutionJournaliere = evolutionJournaliere();
  }

  public EvolutionPatrimoine nouveauDebut(LocalDate nouveauDebut) {
    return new EvolutionPatrimoine(nom, patrimoine, nouveauDebut, fin);
  }

  public EvolutionPatrimoine nouvelleFin(LocalDate nouvelleFin) {
    return new EvolutionPatrimoine(nom, patrimoine, debut, nouvelleFin);
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
