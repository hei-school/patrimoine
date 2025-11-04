package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import javax.swing.JPanel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class Page extends JPanel {
  protected final String name;
  boolean initialized;

  public Page(String name) {
    this.name = name;
    this.initialized = false;

    globalState().subscribe("page-name", this::draw);
  }

  public MultipleView pageManager() {
    return AppContext.getDefault().app().getPageView();
  }

  public State globalState() {
    return AppContext.getDefault().globalState();
  }

  public boolean isActive() {
    return name.equals(globalState().get("page-name"));
  }

  protected boolean destroyOnLeave() {
    return false;
  }

  final void handleUnmount() {
    if (isActive()) {
      return;
    }

    if (initialized && destroyOnLeave()) {
      removeAll();
      revalidate();
      repaint();
      initialized = false;
    }
  }

  void draw() {
    handleUnmount();

    if (initialized) {
      return;
    }

    if (isActive()) {
      initialized = true;
      this.update();
    }
  }

  protected void update() {
    revalidate();
    repaint();
  }
}
