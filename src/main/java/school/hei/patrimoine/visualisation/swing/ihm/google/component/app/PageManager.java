package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class PageManager extends JPanel {
    private final String currentPage;
    private final CardLayout cardLayout;
    private final Map<String, Page> pages;

    public PageManager(String defaultPage, Set<Page> pages) {
        this.currentPage = defaultPage;
        this.cardLayout = new CardLayout();
        this.pages = pages.stream().collect(toMap(Page::getName, p -> p));

        for (var page : pages) {
            add(page.getName(), page);
        }

        setLayout(cardLayout);
        navigate(defaultPage);
    }

    public Page getCurrentPage(){
        return pages.get(currentPage);
    }

    public void navigate(String name) {
        if (pages.containsKey(name)) {
            cardLayout.show(this, name);
        } else {
            throw new IllegalArgumentException("page " + name + " not found");
        }
    }
}
