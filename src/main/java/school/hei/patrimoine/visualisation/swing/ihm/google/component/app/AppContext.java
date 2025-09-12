package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import java.util.*;
import java.util.function.Supplier;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public record AppContext(String id, App app, State globalState) {
  private static final String DEFAULT_CONTEXT_ID_VALUE = "default";

  public AppContext(String id, App app) {
    this(id, app, new State(new HashMap<>()));
  }

  public <T> T getData(String key) {
    return globalState.get(key);
  }

  public static AppContext getDefault() {
    return get(DEFAULT_CONTEXT_ID_SUPPLIER.get());
  }

  public static AppContext get(String contextId) {
    if (!CONTEXTS.containsKey(contextId)) {
      throw new IllegalArgumentException("Context does not exist");
    }

    return CONTEXTS.get(contextId);
  }

  public static AppContext create(String id, App app) {
    if (CONTEXTS.containsKey(id)) {
      throw new IllegalArgumentException("Context already exists");
    }

    var newContext = new AppContext(id, app);
    CONTEXTS.put(id, newContext);
    return newContext;
  }

  public static AppContext createAsDefault(String id, App app) {
    DEFAULT_CONTEXT_ID_SUPPLIER = () -> id;
    return create(id, app);
  }

  private static final Map<String, AppContext> CONTEXTS = new HashMap<>();
  private static Supplier<String> DEFAULT_CONTEXT_ID_SUPPLIER = () -> DEFAULT_CONTEXT_ID_VALUE;
}
