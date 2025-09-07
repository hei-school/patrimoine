package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import static java.util.stream.Collectors.toMap;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import javax.swing.*;

public class PageManager extends JPanel {
  private String currentPageName;
  private final CardLayout cardLayout;
  private final Map<String, Page> pages;

  public PageManager(String defaultPage, Set<Page> pages) {
    this.currentPageName = defaultPage;
    this.cardLayout = new CardLayout();
    this.pages = pages.stream().collect(toMap(Page::getName, p -> p));

    setLayout(cardLayout);

    pages.forEach(page -> add(page.getName(), page));

    navigate(defaultPage);
  }

  public Page getCurrentPage() {
    return pages.get(currentPageName);
  }

  public static void navigateTo(String name) {
    AppContext.getDefault().app().getPageManager().navigate(name);
  }

  public void navigate(String name) {
    if (pages.containsKey(name)) {
      currentPageName = name;
      cardLayout.show(this, name);
    } else {
      throw new IllegalArgumentException("page " + name + " not found");
    }
  }
}
