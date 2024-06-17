package school.hei.patrimoine.visualisation.swing.ihm;

import school.hei.patrimoine.modele.FluxJournalier;
import school.hei.patrimoine.visualisation.swing.modele.PatrimoinesVisualisables;

import java.util.Set;

public final class FluxJournaliersIHM extends FluxIHM {

  public FluxJournaliersIHM(PatrimoinesVisualisables patrimoinesVisualisables) {
    super(patrimoinesVisualisables);
  }

  @Override
  protected String titre() {
    return "FLUX JOURNALIERS";
  }

  @Override
  protected Set<FluxJournalier> flux() {
    return patrimoinesVisualisables.getEvolutionPatrimoine().getFluxJournaliers();
  }
}
