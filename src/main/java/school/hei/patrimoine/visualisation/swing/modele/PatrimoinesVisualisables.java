package school.hei.patrimoine.visualisation.swing.modele;

import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.Patrimoine;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import static java.time.LocalDate.now;

public class PatrimoinesVisualisables extends Observable {

  private final Map<String, Patrimoine> patrimoinesParNom;
  private Patrimoine patrimoineSélectionné;
  private LocalDate débutEvolution;
  private LocalDate finEvolution;

  public PatrimoinesVisualisables(List<Patrimoine> patrimoines) {
    super();
    this.patrimoinesParNom = new HashMap<>();
    patrimoines.forEach(patrimoine -> this.patrimoinesParNom.put(patrimoine.nom(), patrimoine));

    this.patrimoineSélectionné = patrimoines.get(0);
    this.débutEvolution = now();
    this.finEvolution = this.débutEvolution.plusMonths(1);
  }

  public Set<String> noms() {
    return patrimoinesParNom.keySet();
  }

  public void setDébutEvolution(LocalDate date) {
    this.débutEvolution = date;
    change();
  }

  public void setFinEvolution(LocalDate date) {
    this.finEvolution = date;
    change();
  }

  public Patrimoine selectionne(String nom) {
    this.patrimoineSélectionné = this.patrimoinesParNom.get(nom);

    change();
    return this.patrimoineSélectionné;
  }

  private void change() {
    setChanged();
    notifyObservers();
  }

  public EvolutionPatrimoine getEvolutionPatrimoine() {
    return new EvolutionPatrimoine(
        patrimoineSélectionné.nom(),
        patrimoineSélectionné,
        débutEvolution,
        finEvolution);
  }
}
