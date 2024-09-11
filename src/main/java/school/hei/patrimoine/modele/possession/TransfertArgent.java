package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.possession.TypeAgregat.FLUX;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.Argent;

public final class TransfertArgent extends Possession {
  private final GroupePossession transfertCommeGroupe;

  public TransfertArgent(
      String nom,
      Compte depuisCompte,
      Compte versCompte,
      LocalDate debut,
      LocalDate fin,
      Argent fluxMensuel,
      int dateOperation) {
    super(nom, debut, new Argent(0, fluxMensuel.devise()));
    this.transfertCommeGroupe =
        new GroupePossession(
            nom,
            debut,
            Set.of(
                new FluxArgent(
                    "Flux TransfertArgent sortant: " + nom,
                    depuisCompte,
                    debut,
                    fin,
                    fluxMensuel.mult(-1),
                    dateOperation),
                new FluxArgent(
                    "Flux TransfertArgent entrant: " + nom,
                    versCompte,
                    debut,
                    fin,
                    fluxMensuel,
                    dateOperation)),
            valeurComptable.devise());
  }

  public TransfertArgent(
      String nom, Compte depuisCompte, Compte versCompte, LocalDate t, Argent fluxMensuel) {
    this(nom, depuisCompte, versCompte, t, t, fluxMensuel, t.getDayOfMonth());
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    return transfertCommeGroupe.projectionFuture(tFutur);
  }

  @Override
  public TypeAgregat typeAgregat() {
    return FLUX;
  }
}
