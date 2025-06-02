package school.hei.patrimoine.visualisation.web.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("hello")
public class HelloView  extends VerticalLayout {
  public HelloView() {
    add(new H1("Hello World!"));
  }
}
