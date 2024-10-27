package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;

@ToString
@EqualsAndHashCode
@Getter
public abstract sealed class Possession implements Serializable /*note(no-serializable)*/
    permits AchatMaterielAuComptant,
        Argent,
        CompteCorrection,
        Correction,
        FluxArgent,
        GroupePossession,
        Materiel,
        TransfertArgent {
  protected final String nom;
  protected final LocalDate t;
  protected final int valeurComptable;
  protected final Devise devise;
  @EqualsAndHashCode.Exclude @ToString.Exclude private CompteCorrection compteCorrection;
  protected final Map<Personne, Double> possesseurs;

  public Possession(
      String nom,
      LocalDate t,
      int valeurComptable,
      Devise devise,
      Map<Personne, Double> possesseurs) {
    super();
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
    this.devise = devise;
    this.possesseurs = possesseurs;
  }

  public Possession(String nom, LocalDate t, int valeurComptable, Devise devise) {
    this(nom, t, valeurComptable, devise, Map.of());
  }

  public Possession(String nom, LocalDate t, int valeurComptable) {
    this(nom, t, valeurComptable, NON_NOMMEE, Map.of());
  }

  public CompteCorrection getCompteCorrection() {
    if (compteCorrection == null) {
      compteCorrection = new CompteCorrection(nom, devise);
    }
    return compteCorrection;
  }

  public final int valeurComptable() {
    return valeurComptable;
  }

  public final int valeurComptable(Devise autreDevise) {
    double valeurEnAriaryAutreDeviseATempsT = autreDevise.valeurEnAriary(t);
    return (int)
        ((this.valeurComptable * this.devise.valeurEnAriary(t)) / valeurEnAriaryAutreDeviseATempsT);
  }

  public final int valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).valeurComptable(this.devise);
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  public abstract TypeAgregat typeAgregat();
}
