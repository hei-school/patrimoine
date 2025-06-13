package school.hei.patrimoine.visualisation.web.components.flux.renderers;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableBiConsumer;
import school.hei.patrimoine.modele.evolution.FluxJournalier;

public class CompteRenderer implements SerializableBiConsumer<HorizontalLayout, FluxJournalier> {

  @Override
  public void accept(HorizontalLayout layout, FluxJournalier fluxJournalier) {
    layout.setWidth("100%");
    layout.add(new Text(fluxJournalier.compte().nom()));
  }
}
