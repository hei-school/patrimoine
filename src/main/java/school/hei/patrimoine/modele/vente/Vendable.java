package school.hei.patrimoine.modele.vente;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;

import java.time.LocalDate;

public interface Vendable {
    void vendre(LocalDate date, Argent prix, Compte compteBeneficiaire);

    boolean estVendue();

    boolean estVendue(LocalDate date);

    LocalDate getDateVente();

    Argent getPrixVente();
}
