package school.hei.patrimoine.possession;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

@Getter
public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;
  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }
  @Override
  public Possession projectionFuture(Instant tFutur){
    int monthT= (t.atZone(ZoneId.systemDefault())).getMonthValue();
    int monthTfutur=(tFutur.atZone(ZoneId.systemDefault())).getMonthValue();
    int diff=monthTfutur-monthT;
    if(diff<=12){
      return new Materiel(nom,tFutur,getValeurComptable(),tauxDAppreciationAnnuelle);
    }else {
      return new Materiel(nom, tFutur, (int) (getValeurComptable() - (getValeurComptable() * 0.10)),tauxDAppreciationAnnuelle);
    }
  }
}