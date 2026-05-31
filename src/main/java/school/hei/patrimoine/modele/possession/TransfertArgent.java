package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.possession.TypeAgregat.FLUX;

import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import school.hei.patrimoine.modele.Argent;

@Getter
public final class TransfertArgent extends Possession {
  private final Compte versCompte;
  private final Argent fluxMensuel;
  private final Compte depuisCompte;
  private final GroupePossession transfertCommeGroupe;
  private final LocalDate fin;
  private final LocalDate debut;
  private final int dateOperation;

  public TransfertArgent(
      String nom,
      Compte depuisCompte,
      Compte versCompte,
      LocalDate debut,
      LocalDate fin,
      int dateOperation,
      Argent fluxMensuel) {
    super(nom, debut, new Argent(0, fluxMensuel.devise()));
    this.depuisCompte = depuisCompte;
    this.versCompte = versCompte;
    this.fluxMensuel = fluxMensuel;
    this.fin = fin;
    this.debut = debut;
    this.dateOperation = dateOperation;
    this.transfertCommeGroupe =
        new GroupePossession(
            nom,
            valeurComptable.devise(),
            debut,
            Set.of(
                new FluxArgent(
                    "Flux TransfertArgent sortant: " + nom,
                    depuisCompte,
                    debut,
                    fin,
                    dateOperation,
                    fluxMensuel.mult(-1)),
                new FluxArgent(
                    "Flux TransfertArgent entrant: " + nom,
                    versCompte,
                    debut,
                    fin,
                    dateOperation,
                    fluxMensuel)));
  }

  public TransfertArgent(
      String nom, Compte depuisCompte, Compte versCompte, LocalDate t, Argent fluxMensuel) {
    this(nom, depuisCompte, versCompte, t, t, t.getDayOfMonth(), fluxMensuel);
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
