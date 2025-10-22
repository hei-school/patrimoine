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
    var callbackCount = new AtomicInteger(0);

    state.subscribe("name", callbackCount::incrementAndGet);
    state.update(Map.of("name", "updated", "value", 30));

    assertEquals("updated", state.get("name"));
    assertEquals(30, (int) state.get("value"));
    assertEquals(1, callbackCount.get());
  }

  @Test
  void invalidate() {
    var callbackCount = new AtomicInteger(0);

    state.subscribe("value", callbackCount::incrementAndGet);
    state.invalidate(Set.of("value"));

    assertNull(state.get("value"));
    assertEquals(1, callbackCount.get());
  }

  @Test
  void test_update() {
    var callbackCount = new AtomicInteger(0);

    state.subscribe("value", callbackCount::incrementAndGet);
    state.update("value", 50);

    assertEquals(50, (int) state.get("value"));
    assertEquals(1, callbackCount.get());
  }

  @Test
  void subscribe() {
    var callbackCount = new AtomicInteger(0);

    state.subscribe(Set.of("name", "value"), callbackCount::incrementAndGet);

    state.update("name", "newName");
    state.update("value", 100);

    assertEquals(2, callbackCount.get());
  }

  @Test
  void get_data() {
    Map<String, Object> data = state.getData();
    assertEquals("test", data.get("name"));
    assertEquals(25, data.get("value"));
    assertEquals(2, data.size());
  }

  @Test
  void get_subscriptions() {
    state.subscribe("name", () -> {});
    var subscriptions = state.getSubscriptions();

    assertTrue(subscriptions.containsKey("name"));
    assertEquals(1, subscriptions.get("name").size());
    assertFalse(subscriptions.isEmpty());
  }
}
