package school.hei.patrimoine.patrilang.generator.possession;

import school.hei.patrimoine.patrilang.generator.PatriLangGenerator;

public class CommentPatriLangGenerator implements PatriLangGenerator<String> {
  @Override
  public String apply(String content) {
    return String.format("`/* %s */`", content);
  }
}
