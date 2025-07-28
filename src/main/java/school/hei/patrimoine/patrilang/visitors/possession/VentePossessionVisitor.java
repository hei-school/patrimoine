package school.hei.patrimoine.patrilang.visitors.possession;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.vente.VentePossession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.VentePossessionContext;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class VentePossessionVisitor implements SimpleVisitor<VentePossessionContext, VentePossession> {
  private final VariableVisitor variableVisitor;

    @Override
    public VentePossession apply(VentePossessionContext ctx) {
        System.out.println("Montant ctx: " + ctx.possessionNom);  // Affiche ce qui est reçu
        if (ctx.montant == null) {
            throw new IllegalArgumentException("Le prix est null dans la ligne: " + ctx.getText());
        }

        if (ctx.montant == null) {
            System.out.println("ctx.prix est NULL ici !");
        } else {
            System.out.println("Prix ctx: " + ctx.compteBeneficiaire.getText());
            System.out.println("Prix ctx: " + ctx.dateValue.getText());
            System.out.println("Prix ctx: " + ctx.montant.getText());
            System.out.println("Prix ctx: " + ctx.possessionNom.getText());

        }
        LocalDate dateVente = variableVisitor.asDate(ctx.dateValue);
        Possession possession = variableVisitor.asPossession(ctx.possessionNom);
        Argent prixVente = variableVisitor.asArgent(ctx.montant);
        Compte compteBeneficiaire = variableVisitor.asCompte(ctx.compteBeneficiaire);

        return new VentePossession(possession, dateVente, prixVente, compteBeneficiaire);
    }

}
