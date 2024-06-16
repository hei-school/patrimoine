package school.hei.patrimoine.visualisation.swing.modele;

import lombok.Getter;
import school.hei.patrimoine.modele.EvolutionPatrimoine;

import java.util.Observable;

public class EvolutionPatrimoineObservable extends Observable {
  @Getter
  private EvolutionPatrimoine evolutionPatrimoine;

  public void setEvolutionPatrimoine(EvolutionPatrimoine evolutionPatrimoine) {
    this.evolutionPatrimoine = evolutionPatrimoine;
    setChanged();
    notifyObservers();
  }
}
