package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import static java.util.UUID.randomUUID;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class State {
  private final Map<String, Object> data;
  private final Map<String, Set<StateObserverCallback>> subscriptions;

  public State(Map<String, Object> data) {
    this.data = new ConcurrentHashMap<>(data);
    this.subscriptions = new ConcurrentHashMap<>();
  }

  @SuppressWarnings("unchecked")
  public <T> T get(String key) {
    return (T) data.get(key);
  }

  public void update(Map<String, Object> newData) {
    data.putAll(newData);
    notifyObservers(newData.keySet());
  }

  public void invalidate(Set<String> keys) {
    keys.forEach(data::remove);
    notifyObservers(keys);
  }

  public void update(String key, @Nullable Object value) {
    data.put(key, value);
    notifyObservers(Set.of(key));
  }

  public void subscribe(Set<String> keys, StateObserverCallbackRunnable runnable) {
    var callback = new StateObserverCallback(runnable);
    for (var key : keys) {
      subscriptions
          .computeIfAbsent(key, k -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
          .add(callback);
    }
  }

  public void subscribe(String key, StateObserverCallbackRunnable callback) {
    subscribe(Set.of(key), callback);
  }

  private void notifyObservers(Set<String> keys) {
    Set<String> notified = ConcurrentHashMap.newKeySet();
    for (var key : keys) {
      var subs = subscriptions.getOrDefault(key, Set.of());
      for (var observerCallback : subs) {
        if (notified.add(observerCallback.id())) {
          observerCallback.callback().run();
        }
      }
    }
  }

  public interface StateObserverCallbackRunnable extends Runnable {}

  private record StateObserverCallback(String id, StateObserverCallbackRunnable callback) {
    public StateObserverCallback(StateObserverCallbackRunnable callback) {
      this(randomUUID().toString(), callback);
    }
  }
}
