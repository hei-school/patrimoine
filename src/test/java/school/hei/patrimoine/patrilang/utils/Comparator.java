package school.hei.patrimoine.patrilang.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import school.hei.patrimoine.modele.possession.FluxArgent;

public class Comparator {
  public static void assertFluxArgentEquals(FluxArgent expected, FluxArgent actual) {
    assertEquals(expected.nom(), actual.nom());
    assertEquals(expected.getDebut(), actual.getDebut());
    assertEquals(expected.getFin(), actual.getFin());
    assertEquals(expected.getCompte(), actual.getCompte());
    assertEquals(expected.getFluxMensuel(), actual.getFluxMensuel());
    assertEquals(expected.getDateOperation(), actual.getDateOperation());
  }
}
