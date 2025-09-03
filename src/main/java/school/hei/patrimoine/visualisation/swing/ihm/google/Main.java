package school.hei.patrimoine.visualisation.swing.ihm.google;

import com.formdev.flatlaf.FlatLightLaf;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.App;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.login.LoginPage;

import java.util.Set;

import static javax.swing.SwingUtilities.invokeLater;

public class Main {
    private static Set<Page> pages(){
        return Set.of(new LoginPage()) ;
    }

    public static void main(String[] args) {
        App.setup();
        FlatLightLaf.setup();
        invokeLater(() -> new App("Patrimoine", "login", 1_200, 700, pages()));;
    }
}
