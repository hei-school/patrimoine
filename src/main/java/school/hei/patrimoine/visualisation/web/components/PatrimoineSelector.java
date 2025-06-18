package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;
import school.hei.patrimoine.visualisation.web.states.GrapheConfState;
import school.hei.patrimoine.visualisation.web.states.PatrimoinesState;

public class PatrimoineSelector extends HorizontalLayout {
  public PatrimoineSelector(
      WebGrapheurService webGrapheurService,
      PatrimoinesState patrimoinesState,
      GrapheConfState grapheConfState) {
    setAlignItems(Alignment.BASELINE);
    setWidthFull();
    var patrimoineSelect = new Select<Patrimoine>();
    patrimoineSelect.setLabel("Patrimoine");
    patrimoineSelect.setPlaceholder("Sélectionnez un Patrimoine");
    patrimoineSelect.setItems(patrimoinesState.getPatrimoines());
    patrimoineSelect.setRenderer(
        new ComponentRenderer<>(patrimoine -> new Text(patrimoine.getNom())));
    patrimoineSelect.getStyle().set("--vaadin-input-field-border-width", "1px");
    patrimoineSelect.setWidth("50%");
    patrimoineSelect.addValueChangeListener(
        e -> patrimoinesState.setSelectedPatrimoine(e.getValue()));

    var allPatrimoineButton = new Button("Tous");
    var allPatrimoinesDialog =
        new AllPatrimoineGrapheDialog(webGrapheurService, patrimoinesState, grapheConfState);
    allPatrimoineButton.addClickListener(
        e -> {
          allPatrimoinesDialog.open();
        });

    add(patrimoineSelect, allPatrimoineButton);
  }
}
