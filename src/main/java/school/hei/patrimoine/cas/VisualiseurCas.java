package school.hei.patrimoine.cas;

import static java.awt.EventQueue.invokeLater;

import java.util.List;
import school.hei.patrimoine.visualisation.swing.ihm.MainIHM;

public class VisualiseurCas {

  public static void main(String[] args) {
    invokeLater(
        () ->
            new MainIHM(
                List.of(
                    new PatrimoineEtudiantPireCas().get(),
                    new PatrimoineRichePireCas().get(),
                    new PatrimoineRicheMoyenCas().get())));
  }
}
