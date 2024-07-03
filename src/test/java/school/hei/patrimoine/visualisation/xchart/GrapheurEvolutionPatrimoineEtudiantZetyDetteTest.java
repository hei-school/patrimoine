package school.hei.patrimoine.visualisation.xchart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.PatrimoineEtudiantZetyDetteCas;
import school.hei.patrimoine.modele.Patrimoine;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrapheurEvolutionPatrimoineEtudiantZetyDetteTest {

  private Patrimoine patrimoineZety;

  @BeforeEach
  void setUp() {
    PatrimoineEtudiantZetyDetteCas zetyCas = new PatrimoineEtudiantZetyDetteCas();
    patrimoineZety = zetyCas.get();
  }

  @Test
  void testDiminutionPatrimoineApresEmprunt() {
    LocalDate avantEmprunt = LocalDate.of(2024, 9, 17);
    LocalDate apresEmprunt = LocalDate.of(2024, 9, 18);

    int valeurAvantEmprunt = patrimoineZety.projectionFuture(avantEmprunt).getValeurComptable();
    int valeurApresEmprunt = patrimoineZety.projectionFuture(apresEmprunt).getValeurComptable();

    int diminution = valeurAvantEmprunt - valeurApresEmprunt;

    // La diminution devrait être de 1,000,000 Ar (le coût du prêt)
    assertEquals(1_000_000, diminution, 1); // Tolérance de 1 Ar pour les arrondis

    System.out.println("Diminution du patrimoine : " + diminution + " Ar");
  }
}
