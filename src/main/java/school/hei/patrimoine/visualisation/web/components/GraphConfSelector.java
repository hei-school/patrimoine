package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;


public class GraphConfSelector extends HorizontalLayout {
  public GraphConfSelector() {
    var graphConfCheckboxGroup = new CheckboxGroup<>();
    graphConfCheckboxGroup.setLabel("Configuration Graphe");
    graphConfCheckboxGroup.setItems("Agrégat", "Trésorerie", "Immobilisations", "Obligations");
    add(graphConfCheckboxGroup);
  }
}
