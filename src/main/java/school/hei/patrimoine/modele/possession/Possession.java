package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public abstract sealed class Possession implements Serializable /*note(no-serializable)*/
    permits Argent,
        FluxArgent,
        TransfertArgent,
        Materiel,
        AchatMaterielAuComptant,
        GroupePossession {
  protected final String nom;
  protected final LocalDate t;
  protected final int valeurComptable;
  protected final Devise devise;
  protected final Map<Personne, Double> possesseurs;

  public Possession(String nom, LocalDate t, int valeurComptable, Devise devise) {
    this(nom, t, valeurComptable, devise, Map.of());
  }

  public Possession(String nom, LocalDate t, int valeurComptable) {
    this(nom, t, valeurComptable, NON_NOMMEE, Map.of());
  }

  public final int valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).getValeurComptable(this.devise, tFutur);
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  public abstract TypeAgregat typeAgregat();

  public final int getValeurComptable(Devise autreDevise, LocalDate tFutur) {
    double valeurEnAriaryAutreDeviseATempsT = autreDevise.valeurEnAriary(tFutur);
    return (int)
        ((this.valeurComptable * this.devise.valeurEnAriary(tFutur))
            / valeurEnAriaryAutreDeviseATempsT);
  }
}
