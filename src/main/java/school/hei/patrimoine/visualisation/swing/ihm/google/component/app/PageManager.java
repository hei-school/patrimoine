package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import static java.util.stream.Collectors.toMap;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import javax.swing.*;

public class PageManager extends JPanel {
  private final CardLayout cardLayout;
  private final Map<String, Page> pages;

  public PageManager(String defaultPage, Set<Page> pages) {
    this.cardLayout = new CardLayout();
    this.pages = pages.stream().collect(toMap(Page::getName, p -> p));

    setLayout(cardLayout);

    pages.forEach(page -> add(page.getName(), page));

    navigate(defaultPage);
  }

  public static void navigateTo(String name) {
    AppContext.getDefault().app().getPageManager().navigate(name);
  }

  public void navigate(String name) {
    if (pages.containsKey(name)) {
      cardLayout.show(this, name);
      AppContext.getDefault().globalState().update("page-name", name);
    } else {
      throw new IllegalArgumentException("page " + name + " not found");
    }
  }
}
