package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import school.hei.patrimoine.visualisation.web.states.GrapheConfState;

public class GraphConfSelector extends HorizontalLayout {
  private final GrapheConfState grapheConfState;

  public GraphConfSelector(GrapheConfState grapheConfState) {
    this.grapheConfState = grapheConfState;
    CheckboxGroup<String> graphConfCheckboxGroup = new CheckboxGroup<>();
    graphConfCheckboxGroup.setLabel("Configuration Graphe");
    graphConfCheckboxGroup.setItems("Agrégat", "Trésorerie", "Immobilisations", "Obligations");
    graphConfCheckboxGroup.setValue(
        new HashSet<>(List.of("Agrégat", "Trésorerie", "Immobilisations", "Obligations")));
    graphConfCheckboxGroup.addValueChangeListener(e -> handleGrapheConfChange(e.getValue()));
    add(graphConfCheckboxGroup);
  }

  private void handleGrapheConfChange(Set<String> values) {
    grapheConfState.setGrapheConf(values);
  }
}
