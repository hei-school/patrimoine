package school.hei.patrimoine.visualisation.swing.ihm;

import static java.awt.EventQueue.invokeLater;
import static org.antlr.v4.runtime.CharStreams.fromFileName;

import java.io.IOException;
import java.util.List;

import school.hei.patrimoine.cas.example.EtudiantPireCas;
import school.hei.patrimoine.cas.example.PatrimoineCresusSupplier;
import school.hei.patrimoine.cas.example.PatrimoineRicheSupplier;
import school.hei.patrimoine.patrilang.PatriLangTranspiler;

public class VisualiseurCas {
  public static void main(String[] args) throws IOException {
    var transpiler = new PatriLangTranspiler();
    var charStream = fromFileName("/home/ricka/Ricka/Projects/patrimoine/src/main/java/school/hei/patrimoine/patrilang/examples/Zety.patri");

    invokeLater(
        () ->
            new MainIHM(
                List.of(
                    transpiler.apply(charStream),
                    new EtudiantPireCas().patrimoine(),
                    new PatrimoineRicheSupplier().get(),
                    new PatrimoineCresusSupplier().get())));
  }
}
