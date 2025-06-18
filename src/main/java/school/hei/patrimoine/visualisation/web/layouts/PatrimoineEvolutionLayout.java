package school.hei.patrimoine.visualisation.web.layouts;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import java.util.List;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.web.components.GraphConfSelector;
import school.hei.patrimoine.visualisation.web.components.GrapheContainer;
import school.hei.patrimoine.visualisation.web.components.PatrimoineSelector;
import school.hei.patrimoine.visualisation.web.components.PeriodSelector;
import school.hei.patrimoine.visualisation.web.components.Separator;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;
import school.hei.patrimoine.visualisation.web.states.GrapheConfState;
import school.hei.patrimoine.visualisation.web.states.PatrimoinesState;

public class PatrimoineEvolutionLayout extends HorizontalLayout {
  private final PatrimoinesState patrimoinesState;
  private final GrapheConfState grapheConfState;

  public PatrimoineEvolutionLayout(
      WebGrapheurService grapheurService, List<Patrimoine> patrimoines) {
    this.patrimoinesState = new PatrimoinesState(patrimoines);
    this.grapheConfState = new GrapheConfState();
    getStyle().setAlignItems(Style.AlignItems.CENTER);
    setWidthFull();
    var controlsLayout = new VerticalLayout();
    controlsLayout.add(new PatrimoineSelector(grapheurService, patrimoinesState, grapheConfState));
    controlsLayout.add(new GraphConfSelector(grapheConfState));
    controlsLayout.add(new PeriodSelector(patrimoinesState));
    controlsLayout.add(new Separator());
    controlsLayout.add(new FluxLayout(patrimoinesState));
    add(controlsLayout, new GrapheContainer(grapheurService, patrimoinesState, grapheConfState));
  }
}
