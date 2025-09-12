package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

public abstract class LazyPage extends Page {
  public LazyPage(String name) {
    super(name);
  }

  protected abstract void init();

  @Override
  void draw() {
    handleUnmount();

    if (!isActive() || initialized) {
      return;
    }

    init();
    update();
    initialized = true;
  }
}
