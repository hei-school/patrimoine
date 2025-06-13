package school.hei.patrimoine.visualisation.web.components.flux;

import static java.util.Comparator.comparing;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import java.util.Observable;
import java.util.Observer;
import school.hei.patrimoine.modele.evolution.FluxJournalier;
import school.hei.patrimoine.visualisation.web.components.RoundedGrid;
import school.hei.patrimoine.visualisation.web.components.flux.renderers.CompteRenderer;
import school.hei.patrimoine.visualisation.web.components.flux.renderers.FluxRenderer;
import school.hei.patrimoine.visualisation.web.states.PatrimoinesState;

public class FluxImpossiblesGrid extends RoundedGrid<FluxJournalier> implements Observer {
  private final PatrimoinesState patrimoinesState;

  public FluxImpossiblesGrid(PatrimoinesState patrimoinesState) {
    super(FluxJournalier.class);
    this.patrimoinesState = patrimoinesState;
    this.patrimoinesState.addObserver(this);
    addColumn("Date", FluxJournalier::date);
    addColumn("Compte", new ComponentRenderer<>(HorizontalLayout::new, new CompteRenderer()));
    addColumn("Flux", new ComponentRenderer<>(HorizontalLayout::new, new FluxRenderer()));
  }

  @Override
  public void update(Observable observable, Object o) {
    setItems(
        this.patrimoinesState.getEvolutionPatrimoine().getFluxJournaliersImpossibles().stream()
            .sorted(comparing(FluxJournalier::date))
            .toList());
  }
}
