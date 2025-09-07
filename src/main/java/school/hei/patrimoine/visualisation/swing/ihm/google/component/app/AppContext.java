package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import java.util.*;
import java.util.function.Supplier;

public record AppContext(
    String id, App app, Map<String, Object> data, Map<String, Set<AppContextObserver>> observers) {
  private static final String DEFAULT_CONTEXT_ID_VALUE = "default";

  public AppContext(String id, App app) {
    this(id, app, new HashMap<>(), new HashMap<>());
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> findData(String key) {
    return (Optional<T>) Optional.ofNullable(data.get(key));
  }

  @SuppressWarnings("unchecked")
  public <T> T getData(String key) {
    var optionalData = findData(key);

    if (optionalData.isEmpty()) {
      throw new IllegalArgumentException("No data found for key " + key);
    }

    return (T) optionalData.get();
  }

  public void addObserver(String dataKey, AppContextObserver observer) {
    observers.computeIfAbsent(dataKey, k -> new HashSet<>()).add(observer);
  }

  public void setData(String key, Object value) {
    data.put(key, value);

    if (observers.containsKey(key)) {
      observers.get(key).forEach(o -> o.update(this));
    }
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
