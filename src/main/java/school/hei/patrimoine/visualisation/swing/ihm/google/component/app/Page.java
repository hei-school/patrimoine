package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import java.awt.*;
import javax.swing.JPanel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class Page extends JPanel implements AppContextObserver {
  private final String name;

  public Page(String name) {
    this.name = name;
  }

  @Override
  public void update(AppContext appContext) {
    revalidate();
    repaint();
  }

  @Override
  public void subscribe(String contextId, String dataKey) {
    AppContext.get(contextId).addObserver(dataKey, this);
  }

  @Override
  public void subscribe(String dataKey) {
    AppContext.getDefault().addObserver(dataKey, this);
  }
}
