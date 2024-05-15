package school.hei.patrimoine.possession;

import lombok.AllArgsConstructor;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  private final int depensesMensuelle;
  private final Argent financePar;
  private final Instant dateDePonction;

  public TrainDeVie(
      String nom,
      int depensesMensuelle,
      Instant debut,
      Instant fin,
      Argent financePar,
      Instant dateDePonction) {
    super(nom, debut, financePar.valeurComptable);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }
  public int getCompteCourant(){
    return financePar.getValeurComptable();
  }
  public int getDepensesMensuelle (){
    return depensesMensuelle;
  }
  public Instant getDebut() {
    return debut;
  }
  public Instant getFin() {
    return fin;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
     int valeur_finale_compte_courant= getCompteCourant() - getDepensesMensuelle();
     var futur_compte_courant = new Argent("futur compte courant",tFutur,valeur_finale_compte_courant);
     var vie_etudiant = new TrainDeVie(
             getNom(),
             getDepensesMensuelle(),
             getDebut(),
             getFin(),
             futur_compte_courant,
             tFutur
     );
     return vie_etudiant;
  }
  public Possession getValeurComptableFutur(Instant tFutur){
    return projectionFuture(tFutur);
  }
}
