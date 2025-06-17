package school.hei.patrimoine.visualisation.web.layouts;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.List;
import school.hei.patrimoine.cas.example.EtudiantPireCas;
import school.hei.patrimoine.cas.example.PatrimoineCresusSupplier;
import school.hei.patrimoine.cas.example.PatrimoineRicheSupplier;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;

public class MainLayout extends VerticalLayout {
  public MainLayout(WebGrapheurService grapheurService) {
    var patrimoines =
        List.of(
            new EtudiantPireCas().patrimoine(),
            new PatrimoineRicheSupplier().get(),
            new PatrimoineCresusSupplier().get());
    setWidthFull();
    add(new H1("Patrimoine"), new PatrimoineEvolutionLayout(grapheurService, patrimoines));
  }
}
