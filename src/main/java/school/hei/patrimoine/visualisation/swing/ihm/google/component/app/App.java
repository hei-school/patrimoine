package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import java.util.Set;
import lombok.Getter;

@Getter
public abstract class App extends Screen {
  private final AppContext context;
  private final MultipleView pageView;

  public App(String contextId, String windowTitle, int width, int height) {
    super(windowTitle, width, height);

    context = AppContext.createAsDefault(contextId, this);
    pageView = new MultipleView(defaultPageName(), pages());

    add(pageView);
  }

  protected abstract Set<Page> pages();

  protected abstract String defaultPageName();

  public static void setup() {
    System.setProperty("awt.useSystemAAFontSettings", "on");
    System.setProperty("swing.aatext", "true");
  }
}
