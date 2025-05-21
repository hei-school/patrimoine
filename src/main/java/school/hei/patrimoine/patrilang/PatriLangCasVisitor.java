package school.hei.patrimoine.patrilang;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CasContext;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsDevise;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsText;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.patrilang.visitors.SectionVisitor;

@RequiredArgsConstructor
public class PatriLangCasVisitor implements Function<CasContext, Cas> {
  private final SectionVisitor sectionVisitor;

  @Override
  public Cas apply(CasContext ctx) {
    var nom = visitVariableAsText(ctx.sectionCasGeneral().ligneCasNom().variable());
    var devise = visitVariableAsDevise(ctx.sectionCasGeneral().ligneDevise().variable());
    var possesseurs = this.sectionVisitor.visitSectionPossesseurs(ctx.sectionPossesseurs());
  }
}
