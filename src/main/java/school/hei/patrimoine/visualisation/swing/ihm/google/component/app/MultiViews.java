package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class PageManager extends JPanel {
    private final CardLayout cardLayout;
    private final Map<String, JComponent> dialogs;

    public PageManager(String defaultPage, Set<JComponent> pages) {
        this.cardLayout = new CardLayout();
        this.dialogs = pages.stream().collect(toMap(JComponent::getName, p -> p));

        setLayout(cardLayout);

        pages.forEach(page -> add(page.getName(), page));

        navigate(defaultPage);
    }

    public static void navigateTo(PageManager manager, String name) {
        manager.navigate(name);
    }

    public void navigate(String name) {
        if (dialogs.containsKey(name)) {
            cardLayout.show(this, name);
            AppContext.getDefault().globalState().update("dialog-page", name);
        } else {
            throw new IllegalArgumentException("page " + name + " not found in PageManager");
        }
    }
}
