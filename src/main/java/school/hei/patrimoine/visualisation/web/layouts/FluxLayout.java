package school.hei.patrimoine.visualisation.web.layouts;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import school.hei.patrimoine.visualisation.web.components.FluxJournalierGrid;

public class FluxLayout extends Div {
  public FluxLayout() {
    var fluxTab = new TabSheet();
    fluxTab.add("Flux journalier", new FluxJournalierGrid());
    fluxTab.add("Flux impossibles", new FluxJournalierGrid());
    fluxTab.addThemeVariants(TabSheetVariant.LUMO_TABS_CENTERED);
    setWidthFull();
    add(fluxTab);
  }
}
