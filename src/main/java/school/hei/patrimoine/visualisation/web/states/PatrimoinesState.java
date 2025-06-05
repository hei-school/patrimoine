package school.hei.patrimoine.visualisation.web.states;

import lombok.Getter;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.evolution.EvolutionPatrimoine;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;

public final class PatrimoinesState extends State {
  @Getter
  private final List<Patrimoine> patrimoines;
  private Patrimoine selectedPatrimoine;
  private LocalDate evolutionStart;
  private LocalDate evolutionEnd;

  public PatrimoinesState(List<Patrimoine> patrimoines) {
    this.patrimoines = patrimoines;
    this.evolutionStart = now();
    this.evolutionEnd = now().plusYears(1);
    this.selectedPatrimoine = patrimoines.getFirst();
  }

  public void setEvolutionStart(LocalDate evolutionStart) {
    this.evolutionStart = evolutionStart;
    change();
  }

  public void setEvolutionEnd(LocalDate evolutionEnd) {
    this.evolutionEnd = evolutionEnd;
    change();
  }

  public void setSelectedPatrimoine(Patrimoine patrimoine) {
    this.selectedPatrimoine = patrimoine;
    change();
  }

  public EvolutionPatrimoine getEvolutionPatrimoine() {
    return new EvolutionPatrimoine(
        this.selectedPatrimoine.getNom(),
        this.selectedPatrimoine,
        evolutionStart,
        evolutionEnd
    );
  }
}
