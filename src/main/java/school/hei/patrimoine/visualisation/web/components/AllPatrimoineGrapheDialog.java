package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import school.hei.patrimoine.modele.evolution.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;
import school.hei.patrimoine.visualisation.web.states.GrapheConfState;
import school.hei.patrimoine.visualisation.web.states.PatrimoinesState;

public class AllPatrimoineGrapheDialog extends Dialog {
  public AllPatrimoineGrapheDialog(
      WebGrapheurService grapheurService,
      PatrimoinesState patrimoinesState,
      GrapheConfState grapheConfState) {
    setWidth("100%");
    setHeight("100%");
    setHeaderTitle("Tous patrimoines");
    var closeButton = new Button(new Icon("lumo", "cross"));
    closeButton.addClickListener(e -> close());
    getHeader().add(closeButton);

    var grapheContainer = grapheGridContainer();
    var evolutionPatrimoine = patrimoinesState.getEvolutionPatrimoine();
    patrimoinesState
        .getPatrimoines()
        .forEach(
            patrimoine -> {
              var withTitleConf =
                  grapheConfState.getGrapheConf().toBuilder().avecTitre(true).build();
              try {
                var gridWrapper = grapheGridWrapper();
                gridWrapper.add(
                    new GrapheWrapper(
                        grapheurService,
                        withTitleConf,
                        new EvolutionPatrimoine(
                            patrimoine.nom(),
                            patrimoine,
                            evolutionPatrimoine.getDebut(),
                            evolutionPatrimoine.getFin())));
                grapheContainer.add(gridWrapper);
              } catch (Exception e) {
                var notification =
                    Notification.show(
                        String.format(
                            "An error occured while trying to create the graphe of %s",
                            patrimoine.nom()));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
              }
            });

    add(grapheContainer);
  }

  private Div grapheGridContainer() {
    var container = new Div();
    container.setWidth("100%");
    container.addClassName("graphe-grid-container");

    return container;
  }

  private Div grapheGridWrapper() {
    var wrapper = new Div();
    wrapper.setWidth("50%");
    wrapper.addClassName("all-graphe-wrapper");
    return wrapper;
  }
}
