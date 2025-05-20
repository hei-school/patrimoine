package school.hei.patrimoine.patrilang;

import static java.util.Objects.isNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.visitors.VariableVisitor.visitVariableAsArgent;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;
import school.hei.patrimoine.patrilang.visitors.*;

@RequiredArgsConstructor
public class PatriLangTranspileVisitor extends PatriLangParserBaseVisitor<Object> {
  private final CompteVisitor compteVisitor;
  private final CreanceVisitor creanceVisitor;
  private final DetteVisitor detteVisitor;
  private final MaterielVisitor materielVisitor;
  private final AchatMaterielVisitor achatMaterielVisitor;
  private final FluxArgentVisitor fluxArgentVisitor;
  private final TransferArgentVisitor transferArgentVisitor;
  private final GroupPossessionVisitor groupPossessionVisitor;

  @Override
  public Object visitDocument(DocumentContext ctx) {
    if (!isNull(ctx.toutCas())) {
      return this.visitToutCas(ctx.toutCas());
    }

    return this.visitCas(ctx.cas());
  }

  @Override
  public CasSet visitToutCas(ToutCasContext ctx) {
    Argent objectifFinal =
        visitVariableAsArgent(ctx.sectionToutCasGeneral().ligneObjectifFinal().variable());
    return new CasSet(Set.of(), objectifFinal);
  }

  Set<Possession> visitPossessions(CasContext ctx) {
    Set<Possession> possessions = new HashSet<>();

    if (ctx.sectionTresoreries() != null) {
      possessions.addAll(visitSectionTresoreries(ctx.sectionTresoreries()));
    }

    if (ctx.sectionCreances() != null) {
      possessions.addAll(visitSectionCreances(ctx.sectionCreances()));
    }

    if (ctx.sectionDettes() != null) {
      possessions.addAll(visitSectionDettes(ctx.sectionDettes()));
    }

    if (ctx.sectionOperations() != null) {
      possessions.addAll(visitSectionOperations(ctx.sectionOperations()));
    }

    return possessions;
  }
}
