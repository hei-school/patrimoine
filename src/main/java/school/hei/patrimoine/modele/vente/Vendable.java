package school.hei.patrimoine.modele.vente;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;

import java.time.LocalDate;
import java.util.Optional;

public interface Vendable {
    Argent getValeurMarche(LocalDate t);
    void vendre(LocalDate t, Argent prixVente, Compte compteBenificiaire);
    boolean estVendu(LocalDate t);
    Optional<LocalDate> getDateVente();
    Optional<Argent> getPrixVente();
}