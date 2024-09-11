package school.hei.patrimoine.visualisation.swing.ihm;

import static java.awt.EventQueue.invokeLater;

import java.util.List;
import school.hei.patrimoine.cas.EtudiantPireCas;
import school.hei.patrimoine.cas.PatrimoineCresusSupplier;
import school.hei.patrimoine.cas.PatrimoineRicheSupplier;

public class VisualiseurCas {

  public static void main(String[] args) {
    invokeLater(
        () ->
            new MainIHM(
                List.of(
                    new EtudiantPireCas().patrimoine(),
                    new PatrimoineRicheSupplier().get(),
                    new PatrimoineCresusSupplier().get())));
  }
}
