package school.hei.patrimoine.modele.vente;

import java.time.LocalDate;
import java.util.Optional;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;

public interface Vendable {
  boolean estVendu(LocalDate t);

  Optional<Argent> getPrixVente();

  Optional<LocalDate> getDateVente();

  Argent getValeurMarche(LocalDate t);

  void vendre(LocalDate t, Argent prixVente, Compte compteBenificiaire);
}
