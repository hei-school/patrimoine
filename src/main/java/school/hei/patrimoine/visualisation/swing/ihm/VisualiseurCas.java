package school.hei.patrimoine.visualisation.swing.ihm;

import static java.awt.EventQueue.invokeLater;

import java.util.List;
import school.hei.patrimoine.cas.PatrimoineCresusCas;
import school.hei.patrimoine.cas.PatrimoineEtudiantPireCas;
import school.hei.patrimoine.cas.PatrimoineRicheCas;

public class VisualiseurCas {

  public static void main(String[] args) {
    invokeLater(
        () ->
            new MainIHM(
                List.of(
                    new PatrimoineEtudiantPireCas().get(),
                    new PatrimoineRicheCas().get(),
                    new PatrimoineCresusCas().get())));
  }
}
