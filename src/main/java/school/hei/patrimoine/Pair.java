package school.hei.patrimoine;

public record Pair<F, S>(F first, S second) {
  public static <F, S> Pair of(F first, S second) {
    return new Pair(first, second);
  }
}
