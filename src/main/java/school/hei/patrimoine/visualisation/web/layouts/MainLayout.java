package school.hei.patrimoine.visualisation.web.layouts;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import school.hei.patrimoine.cas.example.EtudiantPireCas;
import school.hei.patrimoine.cas.example.PatrimoineCresusSupplier;
import school.hei.patrimoine.cas.example.PatrimoineRicheSupplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;

import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.Devise.EUR;

public class MainLayout extends VerticalLayout {
  public MainLayout(WebGrapheurService grapheurService) {
    var patrimoines =
        new ArrayList<>(List.of(
            new EtudiantPireCas().patrimoine(),
            new PatrimoineRicheSupplier().get(),
            new PatrimoineCresusSupplier().get()));
    patrimoines.addAll(getPersonalPatrimoines(patrimoines));
    setWidthFull();
    add(new H1("Patrimoine"), new PatrimoineEvolutionLayout(grapheurService, patrimoines));
  }

  private Set<Patrimoine> getPersonalPatrimoines(List<Patrimoine> patrimoines) {
    return patrimoines.stream()
        .flatMap(e -> e.getPossesseurs()
            .keySet()
            .stream()
            .map(personne -> personne.patrimoine(EUR, now())))
        .collect(toSet());
  }
}
