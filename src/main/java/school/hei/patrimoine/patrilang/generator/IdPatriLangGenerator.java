package school.hei.patrimoine.patrilang.generator;

public class IdPatriLangGenerator implements PatriLangGenerator<String> {
  @Override
  public String apply(String s) {
    return s.trim().replaceAll(" ", "_");
  }
}
