package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import java.util.List;

public record GoogleLinkList<T>(List<T> planned, List<T> done, List<T> justificative) {
  public record NamedID(String name, String id) {}

  public record NamedLink(String name, String link) {}
}
