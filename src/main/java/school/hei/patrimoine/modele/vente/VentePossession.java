package school.hei.patrimoine.modele.vente;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;

public record VentePossession(Possession possession, LocalDate date, Argent prix, Compte compteBeneficiaire) {

    public VentePossession(Possession possession, LocalDate date, Argent prix, Compte compteBeneficiaire) {
        this.possession = possession;
        this.date = date;
        this.prix = prix;
        this.compteBeneficiaire = compteBeneficiaire;
    }

    public void execute() {
        if (possession.estVendue()) {
            throw new IllegalStateException("Déjà vendue");
        }
        possession.vendre(date, prix, compteBeneficiaire);
    }
}
