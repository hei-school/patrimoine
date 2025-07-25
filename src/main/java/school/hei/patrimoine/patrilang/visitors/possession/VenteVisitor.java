package school.hei.patrimoine.patrilang.visitors.possession;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.Vente;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.VentePossessionContext;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.util.Arrays;

@RequiredArgsConstructor
public class VenteVisitor implements SimpleVisitor<VentePossessionContext, Vente> {
  private final VariableVisitor variableVisitor;

  @Override
  public Vente apply(VentePossessionContext ctx) {
    var possessionTypeStr = Arrays.stream(ctx.possessionAVendre.VARIABLE().toString().split(":")).toList().getFirst();
    var possessionAVendre = switch (possessionTypeStr) {
      case "Trésoreries" -> variableVisitor.asCompte(ctx.possessionAVendre);
      case "Créances" -> variableVisitor.asCreance(ctx.possessionAVendre);
      case "Dettes" -> variableVisitor.asDette(ctx.possessionAVendre);
      default -> throw new UnsupportedOperationException("Can't sell: " + possessionTypeStr);
    };

    var t = variableVisitor.asDate(ctx.dateDeVente);
    var valeurDeVente = variableVisitor.asArgent(ctx.prixDeVente);
    var dateDeVente = variableVisitor.asDate(ctx.dateDeVente);
    var compteBeneficiaire = variableVisitor.asCompte(ctx.compteBeneficiaire);

    return new Vente(t, possessionAVendre, valeurDeVente, dateDeVente, compteBeneficiaire);
  }
}
