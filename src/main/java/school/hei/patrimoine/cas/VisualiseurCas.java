package school.hei.patrimoine.cas;

import school.hei.patrimoine.visualisation.swing.ihm.MainIHM;

import java.util.List;

import static java.awt.EventQueue.invokeLater;

public class VisualiseurCas {

  public static void main(String[] args) {
    invokeLater(() -> new MainIHM(List.of(
        new PatrimoineEtudiantPireCas().get(),
        new PatrimoineRichePireCas().get(),
        new PatrimoineRicheMoyenCas().get(),
        new PatrimoineEtudiantZetyCas().get(),
        new PatrimoineEtudiantZetyDetteCas().get()
    )));
  }
}
