package school.hei.patrimoine.visualisation.xchart;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import javax.imageio.ImageIO;
import java.io.File;
import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VisualiseurTest {
  private final Visualiseur visualiseur = new Visualiseur();

  @SneakyThrows
  public static boolean areImagesEqual(File image1, File image2) {
    // https://stackoverflow.com/questions/8567905/how-to-compare-images-for-similarity-using-java
    var biA = ImageIO.read(image1);
    var dbA = biA.getData().getDataBuffer();
    int sizeA = dbA.getSize();
    var biB = ImageIO.read(image2);
    var dbB = biB.getData().getDataBuffer();
    int sizeB = dbB.getSize();

    if (sizeA == sizeB) {
      for (int i = 0; i < sizeA; i++) {
        if (dbA.getElem(i) != dbB.getElem(i)) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  private EvolutionPatrimoine evolutionPatrimoineEtudiant() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 400_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur,
        au13mai24.minusDays(100),
        au13mai24.plusDays(100),
        -100_000,
        15);

    var mac = new Materiel(
        "MacBook Pro",
        au13mai24,
        500_000,
        -0.9);

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie, mac));

    return new EvolutionPatrimoine(
        "Nom",
        patrimoineIloAu13mai24,
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2024, MAY, 17));

  }

  @Test
  void visualise() {
    var imageGeneree = visualiseur.apply(evolutionPatrimoineEtudiant());
    assertTrue(areImagesEqual(
        new File("src/test/resources/patrimoine-etudiant.png"),
        imageGeneree));
  }
}