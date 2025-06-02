package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import school.hei.patrimoine.cas.example.EtudiantPireCas;
import school.hei.patrimoine.cas.example.PatrimoineCresusSupplier;
import school.hei.patrimoine.cas.example.PatrimoineRicheSupplier;
import school.hei.patrimoine.modele.Patrimoine;

import java.util.List;

public class PatrimoineSelector extends HorizontalLayout {
  public PatrimoineSelector() {
    setWidthFull();
    var patrimoineSelect =  new Select<Patrimoine>();
    patrimoineSelect.setLabel("Patrimoine");
    patrimoineSelect.setPlaceholder("Sélectionnez un Patrimoine");
    patrimoineSelect.setItems(List.of(
        new EtudiantPireCas().patrimoine(),
        new PatrimoineRicheSupplier().get(),
        new PatrimoineCresusSupplier().get()));
    patrimoineSelect.setRenderer(new ComponentRenderer<>(patrimoine -> new Text(patrimoine.getNom())));
    patrimoineSelect.getStyle().set("--vaadin-input-field-border-width", "1px");
    patrimoineSelect.setWidth("50%");
    add(patrimoineSelect);
  }
}
