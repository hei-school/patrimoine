package school.hei.patrimoine.visualisation.swing.ihm.google.component.button;

import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.PageManager;

public class NavigateButton extends Button{
    public NavigateButton(String text, String to) {
        this(AppContext.getDefault().app().getPageManager(), text, to);
    }

    public NavigateButton(PageManager pageManager, String text, String to) {
        super(text);

        addActionListener(e -> pageManager.navigate(to));
    }
}
