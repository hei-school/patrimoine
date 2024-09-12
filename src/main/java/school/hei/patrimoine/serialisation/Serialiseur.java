package school.hei.patrimoine.serialisation;

public abstract sealed class Serialiseur<T> permits ByteSerialiseur, PatrilangSerialiseur {
  public abstract String serialise(T object);

  public abstract T deserialise(String serialisé);
}
