package school.hei.patrimoine.patrilang.generator.possession;

import static school.hei.patrimoine.modele.normalizer.PossessionNomNormalizer.normalize;

import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.patrilang.generator.PatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.VariableTypePatriLangGenerator;

public class ComptePatriLangGenerator implements PatriLangGenerator<Compte> {
  private final VariableTypePatriLangGenerator variableTypeGenerator;

  public ComptePatriLangGenerator() {
    this.variableTypeGenerator = new VariableTypePatriLangGenerator();
  }

  @Override
  public String apply(Compte compte) {
    var nom = normalize(compte.nom());
    var type = variableTypeGenerator.apply(compte);
    return String.format("%s:%s", type, nom);
  }
}
