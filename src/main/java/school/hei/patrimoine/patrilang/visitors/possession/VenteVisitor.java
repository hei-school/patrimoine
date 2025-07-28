package school.hei.patrimoine.patrilang.visitors.possession;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.vente.VentePossession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.VentePossessionContext;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class VenteVisitor implements SimpleVisitor<VentePossessionContext, VentePossession> {
    private final VariableVisitor variableVisitor;

    @Override
    public VentePossession apply(VentePossessionContext ctx) {
        LocalDate dateVente = variableVisitor.asDate(ctx.dateValue);
        Possession possession = variableVisitor.asPossession(ctx.possessionNom);
        Argent prixVente = variableVisitor.asArgent(ctx.prix);
        Compte compteBeneficiaire = variableVisitor.asCompte(ctx.compteBeneficiaire);

        return new VentePossession(possession, dateVente, prixVente, compteBeneficiaire);
    }

}
