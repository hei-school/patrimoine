package school.hei.patrimoine.visualisation.web.layouts;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import school.hei.patrimoine.visualisation.web.components.GraphConfSelector;
import school.hei.patrimoine.visualisation.web.components.GrapheContainer;
import school.hei.patrimoine.visualisation.web.components.PatrimoineSelector;
import school.hei.patrimoine.visualisation.web.components.PeriodSelector;
import school.hei.patrimoine.visualisation.web.components.Separator;

public class PatrimoineEvolutionLayout extends HorizontalLayout {
  public PatrimoineEvolutionLayout() {
    getStyle().setAlignItems(Style.AlignItems.CENTER);
    setWidthFull();
    var controlsLayout = new VerticalLayout();
    controlsLayout.add(new PatrimoineSelector());
    controlsLayout.add(new GraphConfSelector());
    controlsLayout.add(new PeriodSelector());
    controlsLayout.add(new Separator());
    controlsLayout.add(new FluxLayout());
    add(controlsLayout, new GrapheContainer());
  }
}
