package school.hei.patrimoine.patrilang.visitors.possession;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.vente.VentePossession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class VentePossessionVisitor
        implements SimpleVisitor<PatriLangParser.VentePossessionContext, VentePossession> {

    private final VariableVisitor variableVisitor;
    private final IdVisitor idVisitor;

    @Override
    public VentePossession apply(PatriLangParser.VentePossessionContext ctx) {
        String id = idVisitor.apply(ctx.id());
        LocalDate dateVente = variableVisitor.asDate(ctx.dateValue);
        Possession possession = variableVisitor.asPossession(ctx.possessionNom);
        Argent prixVente = variableVisitor.asArgent(ctx.prix);
        Compte compteBeneficiaire = variableVisitor.asCompte(ctx.compteBeneficiaire);

        return new VentePossession(possession, dateVente, prixVente, compteBeneficiaire);
    }
}
