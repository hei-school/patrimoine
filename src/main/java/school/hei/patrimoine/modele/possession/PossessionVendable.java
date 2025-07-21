package school.hei.patrimoine.modele.possession;


import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.vendre.Vendable;

import java.time.LocalDate;

public non-sealed abstract class PossessionVendable extends Possession implements Vendable {
    protected LocalDate dateVente;
    protected Argent prixVente;

    protected PossessionVendable(String nom, LocalDate t, Argent valeurComptable) {
        super(nom, t, valeurComptable);
    }

    @Override
    public void vendre(LocalDate date, Argent prix) {
        this.dateVente = date;
        this.prixVente = prix;
    }

    @Override
    public boolean estVendue() {
        return dateVente != null;
    }

    @Override
    public boolean estActiveALaDate(LocalDate date) {
        return dateVente == null || date.isBefore(dateVente);
    }

    @Override
    public LocalDate getDateVente() {
        return dateVente;
    }

    @Override
    public Argent getPrixVente() {
        return prixVente;
    }

}
