package school.hei.patrimoine.visualisation.web.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import school.hei.patrimoine.visualisation.web.layouts.MainLayout;

@Route("")
public class HomeView extends Div {
    public HomeView() {
        setClassName("home-view");
        add(new MainLayout());
    }
}
