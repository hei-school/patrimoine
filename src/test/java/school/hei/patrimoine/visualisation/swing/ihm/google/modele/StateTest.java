package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StateTest {
  private State state;

  @BeforeEach
  void setUp() {
    state = new State(Map.of("name", "test", "value", 25));
  }

  @Test
  void get() {
    assertEquals("test", state.get("name"));
    assertEquals(25, (int) state.get("value"));
    assertNull(state.get("unknown"));
  }

  @Test
  void update() {
    AtomicInteger callbackCount = new AtomicInteger(0);

    state.subscribe("name", callbackCount::incrementAndGet);
    state.update(Map.of("name", "updated", "value", 30));

    assertEquals("updated", state.get("name"));
    assertEquals(30, (int) state.get("value"));
    assertEquals(1, callbackCount.get(), "Callback should be called once for 'name' update");
  }

  @Test
  void invalidate() {
    AtomicInteger callbackCount = new AtomicInteger(0);

    state.subscribe("value", callbackCount::incrementAndGet);
    state.invalidate(Set.of("value"));

    assertNull(state.get("value"));
    assertEquals(1, callbackCount.get(), "Callback should be called once for 'value' invalidation");
  }

  @Test
  void testUpdate() {
    AtomicInteger callbackCount = new AtomicInteger(0);

    state.subscribe("value", callbackCount::incrementAndGet);
    state.update("value", 50);

    assertEquals(50, (int) state.get("value"));
    assertEquals(1, callbackCount.get(), "Callback should be called once for 'value' update");
  }

  @Test
  void subscribe() {
    AtomicInteger callbackCount = new AtomicInteger(0);

    state.subscribe(Set.of("name", "value"), callbackCount::incrementAndGet);

    state.update("name", "newName");
    state.update("value", 100);

    assertEquals(
        2, callbackCount.get(), "Callback should be called twice for 'name' and 'value' updates");
  }

  @Test
  void getData() {
    Map<String, Object> data = state.getData();
    assertEquals("test", data.get("name"));
    assertEquals(25, data.get("value"));
    assertEquals(2, data.size());
  }

  @Test
  void getSubscriptions() {
    state.subscribe("name", () -> {});
    var subscriptions = state.getSubscriptions();

    assertTrue(subscriptions.containsKey("name"), "Subscriptions should contain key 'name'");
    assertEquals(
        1, subscriptions.get("name").size(), "There should be one subscription for 'name'");
    assertFalse(subscriptions.isEmpty(), "Subscriptions should not be empty");
  }
}
