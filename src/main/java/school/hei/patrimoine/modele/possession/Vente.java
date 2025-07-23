package school.hei.patrimoine.modele.possession;

import school.hei.patrimoine.modele.Argent;

import java.time.LocalDate;

import static school.hei.patrimoine.modele.possession.TypeAgregat.FLUX;

public final class Vente extends Possession {
    private final LocalDate tVente;
    private final Possession possession;
    private final Compte compteBeneficiaire;
    private final Argent prixVente;
    public Vente(String nom, LocalDate t, Argent valeurComptable, LocalDate tVente, Possession possession, Argent prixVente, Compte compteBeneficiaire) {
        super(nom, t, valeurComptable);
        this.tVente = tVente;
        this.possession = possession;
        possession.vendre(tVente, prixVente, compteBeneficiaire);
        this.compteBeneficiaire = compteBeneficiaire;
        this.prixVente = prixVente;
    }
    
    private Vente(String nom, LocalDate t, Argent valeurComptable, LocalDate tVente, Possession possession, Argent prixVente, Compte compteBeneficiaire, boolean estProjete) {
        super(nom, t, valeurComptable);
        this.tVente = tVente;
        this.possession = possession;
        this.compteBeneficiaire = compteBeneficiaire;
        this.prixVente = prixVente;
    }

    @Override
    public Possession projectionFuture(LocalDate tFutur) {
        if (tFutur.isBefore(tVente)) {
            return new Vente(nom,
                    tFutur,
                    valeurComptable,
                    tVente,
                    possession.projectionFuture(tFutur),
                    prixVente,
                    compteBeneficiaire,
                    true);
        }
        return null;
    }

    @Override
    public TypeAgregat typeAgregat() {
        return FLUX;
    }
}
