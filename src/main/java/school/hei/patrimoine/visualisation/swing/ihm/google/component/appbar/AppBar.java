package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import lombok.Getter;

@Getter
public class AppBar extends JPanel {
  private final List<Component> leftComponents;
  private final List<Component> rightComponents;

  public AppBar(List<Component> leftComponents, List<Component> rightComponents) {
    super(new BorderLayout());

    this.leftComponents = leftComponents;
    this.rightComponents = rightComponents;

    add(leftControls(), BorderLayout.WEST);
    add(rightControls(), BorderLayout.EAST);
  }

  private JPanel leftControls() {
    var leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftComponents.forEach(leftControls::add);

    return leftControls;
  }

  private JPanel rightControls() {
    var rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightComponents.forEach(rightControls::add);

    return rightControls;
  }
}
