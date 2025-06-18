package school.hei.patrimoine.visualisation.web.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import school.hei.patrimoine.visualisation.web.layouts.MainLayout;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;

@Route("")
public class HomeView extends Div {

  public HomeView(WebGrapheurService webGrapheurService) {
    setClassName("home-view");
    add(new MainLayout(webGrapheurService));
  }
}
