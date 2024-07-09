package school.hei.patrimoine.visualisation.swing.ihm;

import school.hei.patrimoine.visualisation.swing.modele.PatrimoinesVisualisables;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import static java.awt.FlowLayout.LEFT;

public class OperationsImpossiblesIHM extends JPanel implements Observer {
  private final PatrimoinesVisualisables patrimoinesVisualisables;
  private final JTextPane operationsImpossiblesTextPane;

  public OperationsImpossiblesIHM(PatrimoinesVisualisables patrimoinesVisualisables) {
    super(new FlowLayout(LEFT));
    this.patrimoinesVisualisables = patrimoinesVisualisables;
    this.patrimoinesVisualisables.addObserver(this);

    operationsImpossiblesTextPane = new JTextPane();
    new FixedSizer().accept(operationsImpossiblesTextPane, new Dimension(500, 600));
    updateTextPane();
    this.add(new JScrollPane(operationsImpossiblesTextPane));
  }

  private void updateTextPane() {
    var operationsImpossibleStr = patrimoinesVisualisables.getEvolutionPatrimoine().operationsImpossibleStr();
    operationsImpossiblesTextPane.setText(operationsImpossibleStr);
  }

  @Override
  public void update(Observable o, Object arg) {
    updateTextPane();
  }
}
