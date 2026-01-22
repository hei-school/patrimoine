package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CasContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ToutCasContext;

import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PiecesJustificativesContext;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;

@Builder
@RequiredArgsConstructor
public class PatriLangVisitor extends PatriLangParserBaseVisitor<Object> {
  private final PatriLangToutCasVisitor toutCasVisitor;
  private final PatriLangCasVisitor casVisitor;
  private final PatriLangPieceJustificativeVisitor piecesJustificativeVisitor;

  @Override
  public Cas visitCas(CasContext ctx) {
    return casVisitor.apply(ctx);
  }

  @Override
  public CasSet visitToutCas(ToutCasContext ctx) {
    return toutCasVisitor.apply(ctx);
  }

  @Override
  public List<PieceJustificative> visitPiecesJustificatives(PiecesJustificativesContext ctx) {
    return piecesJustificativeVisitor.apply(ctx);
  }
}
