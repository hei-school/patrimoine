package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.Argent.euro;
import static school.hei.patrimoine.modele.possession.TypeAgregat.FLUX;
import static school.hei.patrimoine.modele.possession.TypeFEC.CHARGE;

import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.experimental.Accessors;
import school.hei.patrimoine.modele.Argent;

public final class RemboursementDette extends Possession {
  private final GroupePossession commeGroupe;

  @Getter
  @Accessors(fluent = true)
  private final Compte rembourseur;

  @Getter
  @Accessors(fluent = true)
  private final Compte remboursé;

  public RemboursementDette(
      String nom,
      Compte rembourseur,
      Compte remboursé,
      Dette dette,
      Creance creance,
      LocalDate t,
      Argent montant) {
    this(
        new GroupePossession(
            nom,
            remboursé.devise(),
            t,
            Set.of(
                new TransfertArgent(nom + " (transfert)", rembourseur, remboursé, t, montant),
                new FluxArgent(nom + " (annulation dette)", dette, t, montant),
                new FluxArgent(nom + " (annulation créance)", creance, t, montant.mult(-1)))),
        rembourseur,
        remboursé);
  }

  private RemboursementDette(GroupePossession commeGroupe, Compte rembourseur, Compte remboursé) {
    super(commeGroupe.nom, LocalDate.MIN, euro(0));
    this.commeGroupe = commeGroupe;
    this.rembourseur = rembourseur;
    this.remboursé = remboursé;
  }

  @Override
  public RemboursementDette projectionFuture(LocalDate tFutur) {
    return new RemboursementDette(commeGroupe.projectionFuture(tFutur), rembourseur, remboursé);
  }

  @Override
  public TypeAgregat typeAgregat() {
    return FLUX;
  }

  @Override
  public TypeFEC getTypeFEC() {
    return CHARGE;
  }
}
