package school.hei.patrimoine.modele.normalizer;

public class PossessionNomNormalizer {
  private PossessionNomNormalizer() {}

  public static String normalize(String nom) {
    return nom.trim().replaceAll("[- ]+", "_");
  }
}
