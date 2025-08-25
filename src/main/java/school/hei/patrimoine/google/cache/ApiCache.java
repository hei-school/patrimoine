package school.hei.patrimoine.google.cache;

import java.util.*;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;

public class ApiCache {
  private final Map<String, Map<String, Object>> caches;
  private static final ApiCache INSTANCE = new ApiCache();

  public static ApiCache getInstance() {
    return INSTANCE;
  }

  private ApiCache() {
    this.caches = new HashMap<>();
  }

  @SuppressWarnings("unchecked")
  public <T> ThrowingSupplier<T> wrap(String type, String key, ThrowingSupplier<T> provider)
      throws GoogleIntegrationException {
    return () -> {
      var cache = caches.computeIfAbsent(type, k -> new HashMap<>());

      if (cache.containsKey(key) && cache.get(key) != null) {
        return (T) cache.get(key);
      }

      T newData = provider.get();
      cache.put(key, newData);
      return newData;
    };
  }

  public void invalidate(String type, String key) {
    caches.getOrDefault(type, Map.of()).remove(key);
  }
}
