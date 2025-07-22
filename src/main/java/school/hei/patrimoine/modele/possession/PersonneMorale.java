package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.Argent.euro;
import static school.hei.patrimoine.modele.possession.TypeAgregat.PATRIMOINE;

import java.time.LocalDate;
import lombok.Getter;
import lombok.experimental.Accessors;
import school.hei.patrimoine.modele.Personne;

public final class PersonneMorale extends Possession {

  @Accessors(fluent = true)
  @Getter
  private final Personne personne;

  public PersonneMorale(String nom) {
    super(nom, LocalDate.MIN, euro(0));
    personne = new Personne(nom);
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    return new GroupePossession(
        personne.nom() + " " + tFutur,
        devise(),
        tFutur,
        personne.patrimoine(devise(), tFutur).getPossessions());
  }

  @Override
  public TypeAgregat typeAgregat() {
    return PATRIMOINE;
  }
}
