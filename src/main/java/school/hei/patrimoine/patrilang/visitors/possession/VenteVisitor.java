package school.hei.patrimoine.patrilang.visitors.possession;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.vente.Vente;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.VenteContext;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class VenteVisitor implements SimpleVisitor<VenteContext, Vente> {
  private final VariableVisitor variableVisitor;

  @Override
  public Vente apply(VenteContext ctx) {
    LocalDate dateVente = variableVisitor.asDate(ctx.dateValue);
    Possession possession = variableVisitor.asPossession(ctx.possessionNom);
    Argent prixVente = variableVisitor.asArgent(ctx.montant);
    Compte compteBeneficiaire = variableVisitor.asCompte(ctx.compteBeneficiaire);

    return new Vente(possession, dateVente, prixVente, compteBeneficiaire);
  }
}
