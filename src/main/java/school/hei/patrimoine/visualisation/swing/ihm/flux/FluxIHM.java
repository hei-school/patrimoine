package school.hei.patrimoine.visualisation.swing.ihm.flux;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.swing.*;
import school.hei.patrimoine.modele.evolution.FluxJournalier;
import school.hei.patrimoine.visualisation.swing.ihm.FixedSizer;
import school.hei.patrimoine.visualisation.swing.modele.PatrimoinesVisualisables;

public abstract sealed class FluxIHM extends JPanel implements Observer
    permits FluxImpossiblesIHM, FluxJournaliersIHM {
  private final JTextPane fluxTextPane;
  protected final PatrimoinesVisualisables patrimoinesVisualisables;
  protected final JCheckBox withCorrectionCheckBox = new JCheckBox("Avec Correction");

  public FluxIHM(PatrimoinesVisualisables patrimoinesVisualisables) {
    super(new BorderLayout(0, 5));
    this.patrimoinesVisualisables = patrimoinesVisualisables;
    this.patrimoinesVisualisables.addObserver(this);

    withCorrectionCheckBox.addActionListener(e -> updateTextPane());
    this.add(withCorrectionCheckBox, BorderLayout.NORTH);

    fluxTextPane = new JTextPane();
    fixPanelSize();
    updateTextPane();

    var scrollPane = new JScrollPane(fluxTextPane);
    scrollPane.setPreferredSize(new Dimension(300, getIHMHeight()));
    this.add(scrollPane, BorderLayout.CENTER);
  }

  private void fixPanelSize() {
    new FixedSizer().accept(fluxTextPane, new Dimension(500, getIHMHeight()));
  }

  protected abstract int getIHMHeight();

  private void updateTextPane() {
    var flux =
        flux().stream()
            .sorted(comparing(FluxJournalier::date))
            .map(FluxJournalier::toString)
            .collect(joining("\n\n"));
    fluxTextPane.setText("".equals(flux) ? "" : (String.format("!! %s !!\n\n", titre()) + flux));
  }

  protected abstract String titre();

  protected abstract Set<FluxJournalier> flux();

  @Override
  public void update(Observable o, Object arg) {
    updateTextPane();
  }
}
