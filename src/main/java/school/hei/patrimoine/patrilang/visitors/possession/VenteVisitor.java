package school.hei.patrimoine.patrilang.visitors.possession;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.Vente;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.VenteContext;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class VenteVisitor implements SimpleVisitor<VenteContext, Vente> {
  private final VariableVisitor variableVisitor;

  @Override
  public Vente apply(VenteContext ctx) {
    LocalDate tVente = this.variableVisitor.asDate(ctx.dateValue);
    Possession possession = this.variableVisitor.asPossession(ctx.possession);
    Compte compteBeneficiaire = this.variableVisitor.asCompte(ctx.compteBeneficiaire);
    Argent prixVente = this.variableVisitor.asArgent(ctx.prixVente);
    return new Vente(tVente, possession, compteBeneficiaire, prixVente);
  }
}
