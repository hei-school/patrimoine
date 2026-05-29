package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.Argent.euro;
import static school.hei.patrimoine.modele.possession.TypeAgregat.FLUX;

import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import school.hei.patrimoine.modele.Argent;

@Getter
public final class RemboursementDette extends Possession {
  private final Argent montant;
  private final Compte remboursé;
  private final Compte rembourseur;
  private final GroupePossession commeGroupe;

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
        remboursé,
        montant);
  }

  private RemboursementDette(
      GroupePossession commeGroupe, Compte rembourseur, Compte remboursé, Argent montant) {
    super(commeGroupe.nom, commeGroupe.t, euro(0), commeGroupe.valeursMarche);
    this.commeGroupe = commeGroupe;
    this.rembourseur = rembourseur;
    this.remboursé = remboursé;
    this.montant = montant;
  }

  @Override
  public RemboursementDette projectionFuture(LocalDate tFutur) {
    return new RemboursementDette(
        commeGroupe.projectionFuture(tFutur), rembourseur, remboursé, montant);
  }

  @Override
  public TypeAgregat typeAgregat() {
    return FLUX;
  }
}
