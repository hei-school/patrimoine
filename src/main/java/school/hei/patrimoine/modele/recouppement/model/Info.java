package school.hei.patrimoine.modele.recouppement.model;

import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import lombok.Builder;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;

@Builder
public record Info<T extends Possession>(
    String nom, LocalDate t, Argent valeur, T possession, Possession possessionACorriger) {
  public static <T extends Possession> Info<T> empty() {
    return new Info<>("", LocalDate.MIN, ariary(0), null, null);
  }

  public boolean isEmpty() {
    return nom.isEmpty() || possession == null;
  }
}
