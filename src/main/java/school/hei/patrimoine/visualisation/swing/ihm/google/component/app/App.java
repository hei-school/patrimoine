package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import java.util.Set;
import java.util.function.Supplier;
import lombok.Getter;

@Getter
public abstract class App extends Screen {
  private final AppContext context;
  private final PageManager pageManager;

  public App(
      String contextId,
      String windowTitle,
      int width,
      int height,
      String defaultPageName,
      Supplier<Set<Page>> pages) {
    super(windowTitle, width, height);

    context = AppContext.createAsDefault(contextId, this);

    /** Creating pages after context */
    pageManager = new PageManager(defaultPageName, pages.get());

    add(pageManager);
  }

  public static void setup() {
    System.setProperty("awt.useSystemAAFontSettings", "on");
    System.setProperty("swing.aatext", "true");
  }
}
