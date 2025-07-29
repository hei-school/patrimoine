package school.hei.patrimoine.patrilang.visitors.possession;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.Vente;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class VenteVisitor implements SimpleVisitor<VenteContext, Vente> {
    private final VariableVisitor variableVisitor;

    @Override
    public Vente apply(VenteContext ctx) {
        LocalDate tVente = variableVisitor.asVente(ctx.dateValue);
        Possession possession = variableVisitor.asVente(ctx.possession);
        Compte compteBeneficiaire = variableVisitor.asVente(ctx.compteBeneficiaire);
        Argent prixVente = variableVisitor.asVente(ctx.prixVente);
        return new Vente(tVente, possession, compteBeneficiaire, prixVente);
    }
}
