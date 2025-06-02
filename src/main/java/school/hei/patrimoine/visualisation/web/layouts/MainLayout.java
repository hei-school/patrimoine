package school.hei.patrimoine.visualisation.web.layouts;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import school.hei.patrimoine.visualisation.web.components.PeriodSelector;

public class MainLayout extends VerticalLayout {
  public MainLayout() {
    setWidthFull();
    add(new H1("Patrimoine"),  new PatrimoineEvolutionLayout());
  }
}
