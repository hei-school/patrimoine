package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import lombok.Getter;

import java.util.Set;

@Getter
public class App extends Screen {
    private final AppContext context;
    private final PageManager pageManager;

    public App(String windowTitle, String defaultPage,  int width, int height, Set<Page> pages) {
        super(windowTitle, width, height);

        pageManager = new PageManager(defaultPage, pages);
        context = AppContext.createAsDefault(AppContext.DEFAULT_CONTEXT_ID_VALUE, this);

        add(pageManager);
    }

    public App(String contextId, String windowTitle, String defaultPage,  int width, int height, Set<Page> pages) {
        super(windowTitle, width, height);

        pageManager = new PageManager(defaultPage, pages);
        context = AppContext.create(contextId, this);
    }

    public static void setup(){
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }
}
