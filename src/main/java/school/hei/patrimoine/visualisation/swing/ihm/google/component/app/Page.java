package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.swing.JPanel;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Page extends JPanel {
    private final String name;

    public Page(String name) {
        this.name = name;
    }
}
