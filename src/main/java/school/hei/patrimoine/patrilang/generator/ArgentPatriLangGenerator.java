package school.hei.patrimoine.patrilang.generator;

import static school.hei.patrimoine.patrilang.mapper.DeviseMapper.deviseToString;

import school.hei.patrimoine.modele.Argent;

public class ArgentPatriLangGenerator implements PatriLangGenerator<Argent> {
  @Override
  public String apply(Argent argent) {
    return String.format(
        "%s%s", argent.ppMontant().replaceAll("000", "_000"), deviseToString(argent.devise()));
  }
}
