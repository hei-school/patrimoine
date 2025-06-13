package school.hei.patrimoine.visualisation.web.layouts;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import school.hei.patrimoine.visualisation.web.components.flux.FluxImpossiblesGrid;
import school.hei.patrimoine.visualisation.web.components.flux.FluxJournalierGrid;
import school.hei.patrimoine.visualisation.web.states.PatrimoinesState;

public class FluxLayout extends Div {
  public FluxLayout(PatrimoinesState patrimoinesState) {
    var fluxTab = new TabSheet();
    fluxTab.add("Flux journalier", new FluxJournalierGrid(patrimoinesState));
    fluxTab.add("Flux impossibles", new FluxImpossiblesGrid(patrimoinesState));
    fluxTab.addThemeVariants(TabSheetVariant.LUMO_TABS_CENTERED);
    setWidthFull();
    add(fluxTab);
  }
}
