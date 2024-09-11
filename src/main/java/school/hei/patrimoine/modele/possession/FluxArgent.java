package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.possession.TypeAgregat.FLUX;

import java.time.LocalDate;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.Argent;

@ToString(callSuper = true)
@Slf4j
@Getter
public final class FluxArgent extends Possession {
  @ToString.Exclude private final Compte compte;
  private final LocalDate debut;
  private final LocalDate fin;
  private final Argent fluxMensuel;
  private final int dateOperation;

  public FluxArgent(
      String nom,
      Compte compte,
      LocalDate debut,
      LocalDate fin,
      Argent fluxMensuel,
      int dateOperation) {
    super(nom, LocalDate.MIN, new Argent(0, fluxMensuel.devise()));
    this.compte = compte;
    this.compte.addFinancés(this);

    this.debut = debut;
    this.fin = fin;
    this.fluxMensuel = fluxMensuel;
    this.dateOperation = dateOperation;
  }

  public FluxArgent(String nom, Compte compte, LocalDate date, Argent montant) {
    this(nom, compte, date, date, montant, date.getDayOfMonth());
  }

  @Override
  public FluxArgent projectionFuture(LocalDate tFutur) {
    var tFuturMajoréParFin = (tFutur.isBefore(fin)) ? tFutur : fin;
    var debutOperationMinoréParDebut = compte.t.isBefore(debut) ? debut : compte.t;
    if (debutOperationMinoréParDebut.isAfter(tFuturMajoréParFin)) {
      return this;
    }

    var nbOperations =
        (int)
            debutOperationMinoréParDebut
                .datesUntil(tFuturMajoréParFin.plusDays(1))
                .filter(d -> d.getDayOfMonth() == dateOperation)
                .count();
    var valeurFutur = compte.valeurComptable().add(fluxMensuel.mult(nbOperations), tFutur);
    var argentFutur =
        new Compte(compte.nom + " réduit au financement de " + this, tFutur, valeurFutur);
    return new FluxArgent(nom, argentFutur, debut, tFuturMajoréParFin, fluxMensuel, dateOperation);
  }

  @Override
  public TypeAgregat typeAgregat() {
    return FLUX;
  }
}
