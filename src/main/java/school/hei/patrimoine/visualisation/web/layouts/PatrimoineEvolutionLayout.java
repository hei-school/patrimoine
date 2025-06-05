package school.hei.patrimoine.visualisation.web.layouts;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.web.components.GraphConfSelector;
import school.hei.patrimoine.visualisation.web.components.GrapheContainer;
import school.hei.patrimoine.visualisation.web.components.PatrimoineSelector;
import school.hei.patrimoine.visualisation.web.components.PeriodSelector;
import school.hei.patrimoine.visualisation.web.components.Separator;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;
import school.hei.patrimoine.visualisation.web.states.PatrimoinesState;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class PatrimoineEvolutionLayout extends HorizontalLayout{
  private final PatrimoinesState patrimoinesState;
  public PatrimoineEvolutionLayout(WebGrapheurService grapheurService, List<Patrimoine> patrimoines) {
    this.patrimoinesState = new PatrimoinesState(patrimoines);
    getStyle().setAlignItems(Style.AlignItems.CENTER);
    setWidthFull();
    var controlsLayout = new VerticalLayout();
    controlsLayout.add(new PatrimoineSelector(patrimoinesState));
    controlsLayout.add(new GraphConfSelector());
    controlsLayout.add(new PeriodSelector(patrimoinesState));
    controlsLayout.add(new Separator());
    controlsLayout.add(new FluxLayout());
    add(controlsLayout, new GrapheContainer(grapheurService, patrimoinesState));
  }
}
