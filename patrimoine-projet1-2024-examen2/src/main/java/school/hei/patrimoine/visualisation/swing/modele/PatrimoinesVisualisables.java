package school.hei.patrimoine.visualisation.swing.modele;

import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.Patrimoine;

import java.time.LocalDate;
import java.util.List;
import java.util.Observable;

import static java.time.LocalDate.now;

public class PatrimoinesVisualisables extends Observable {

  private final List<Patrimoine> patrimoines;
  private Patrimoine patrimoineSélectionné;
  private LocalDate débutEvolution;
  private LocalDate finEvolution;

  public PatrimoinesVisualisables(List<Patrimoine> patrimoines) {
    super();
    this.patrimoines = patrimoines;
    this.patrimoineSélectionné = patrimoines.get(0);
    this.débutEvolution = now();
    this.finEvolution = this.débutEvolution.plusMonths(1);
  }

  public List<String> noms() {
    return patrimoines.stream().map(Patrimoine::nom).toList();
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
    this.patrimoineSélectionné = patrimoines.stream()
        .filter(patrimoine -> nom.equals(patrimoine.nom()))
        .findFirst()
        .get();

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
