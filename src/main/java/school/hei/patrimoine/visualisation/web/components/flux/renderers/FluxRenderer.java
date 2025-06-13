package school.hei.patrimoine.visualisation.web.components.flux.renderers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableBiConsumer;
import school.hei.patrimoine.modele.evolution.FluxJournalier;
import school.hei.patrimoine.modele.possession.FluxArgent;

public class FluxRenderer implements SerializableBiConsumer<HorizontalLayout, FluxJournalier> {
  @Override
  public void accept(HorizontalLayout layout, FluxJournalier fluxJournalier) {
    var fluxSize = fluxJournalier.flux().size();
    layout.add(createFluxTag(fluxJournalier.flux().iterator().next()));
    if (fluxSize > 1) {
      layout.add(moreTag(fluxSize));
    }
  }

  private Component createFluxTag(FluxArgent fluxArgent) {
    var container = new Div();
    var montant = fluxArgent.getFluxMensuel().ppMontant();
    container.addClassName("flux-tag");
    if (montant.contains("-")) {
      container.addClassName("neg-flux-tag");
    }
    container.add(new Text(montant + " " + fluxArgent.getFluxMensuel().devise().symbole()));
    return container;
  }

  private Component moreTag(int size) {
    var container = new Div();
    container.addClassName("more-flux-tag");
    container.add(new Text("+" + (size - 1)));
    return container;
  }
}
