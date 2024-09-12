package school.hei.patrimoine.serialisation;

public final class PatrilangSerialiseur<T> extends Serialiseur<T> {
  @Override
  public String serialise(T object) {
    throw new RuntimeException(
        "Ne sera pas implémenté. En effet, les patrimoines spécifiés via PatriLang ont vocation à"
            + " être modifiés uniquement par l'utilisateur, directement dans son texte PatriLang."
            + " Ainsi, aucune manipulation interne ne devrait résulter en une sérialisation"
            + " PatriLang qui diffèrerait de la dernière modification apportée manuellement par"
            + " l'utilisateur dans son texte PatriLang.");
  }

  @Override
  public T deserialise(String serialisé) {
    throw new RuntimeException("TODO");
  }
}
