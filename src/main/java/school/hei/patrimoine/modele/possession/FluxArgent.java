package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.possession.TypeAgregat.FLUX;

import java.time.LocalDate;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.Pair;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

@ToString(callSuper = true)
@Slf4j
@Getter
public final class FluxArgent extends Possession {
  @ToString.Exclude private final Compte compte;
  private final LocalDate debut;
  private final LocalDate fin;
  private final Argent fluxMensuel;
  private final int dateOperation;
  private final PieceJustificative pieceJustificative;

  public FluxArgent(
      String nom,
      Compte compte,
      LocalDate debut,
      LocalDate fin,
      int dateOperation,
      Argent fluxMensuel,
      PieceJustificative pieceJustificative) {
    super(nom, debut, new Argent(0, fluxMensuel.devise()));
    this.compte = compte;
    this.compte.addFinancés(this);

    this.debut = debut;
    this.fin = fin;
    this.fluxMensuel = fluxMensuel;
    this.dateOperation = dateOperation;
    this.pieceJustificative = pieceJustificative;
  }

  public FluxArgent(
      String nom,
      Compte compte,
      LocalDate date,
      Argent montant,
      PieceJustificative pieceJustificative) {
    this(nom, compte, date, date, date.getDayOfMonth(), montant, pieceJustificative);
  }

  @Override
  public FluxArgent projectionFuture(LocalDate tFutur) {
    var tFuturMajoréParFin = (tFutur.isBefore(fin)) ? tFutur : fin;
    var debutOperationMinoréParDebut = compte.t.isBefore(debut) ? debut : compte.t;
    if (debutOperationMinoréParDebut.isAfter(tFuturMajoréParFin)) {
      return this;
    }

    var valeurFutur =
        debutOperationMinoréParDebut
            .datesUntil(tFuturMajoréParFin.plusDays(1))
            .filter(d -> d.getDayOfMonth() == Math.min(dateOperation, d.lengthOfMonth()))
            .sorted()
            .map(d -> Pair.of(fluxMensuel, d))
            // Addition must be done at a given time since Devise fluctuates
            // TODO: test with Transfert between Compte with different Devise
            .reduce(Pair.of(compte.valeurComptable, t), FluxArgent::add)
            .first();
    var argentFutur =
        new Compte(compte.nom + " réduit au financement de " + this, tFutur, valeurFutur);
    return new FluxArgent(
        nom, argentFutur, debut, tFuturMajoréParFin, dateOperation, fluxMensuel, null);
  }

  private static Pair<Argent, LocalDate> add(
      Pair<Argent, LocalDate> p1, Pair<Argent, LocalDate> p2) {
    return Pair.of(p1.first().add(p2.first(), p2.second()), p2.second());
  }

  @Override
  public TypeAgregat typeAgregat() {
    return FLUX;
  }
}
