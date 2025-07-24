package school.hei.patrimoine;

public record Pair<F, S>(F first, S second) {
  public static <F, S> Pair<F, S> of(F first, S second) {
    return new Pair<>(first, second);
  }

  // new ValeurMarche(possession, t, valeurComptable
  // possession.ajouterValeurMarche(this);

  // new Vente(possesion, ...)
  // possession.vendre(
}
