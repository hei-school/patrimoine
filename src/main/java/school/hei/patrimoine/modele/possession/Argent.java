package school.hei.patrimoine.modele.possession;

import lombok.Getter;
import school.hei.patrimoine.modele.Devise;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Getter
public sealed class Argent extends Possession permits Dette, Creance {
  private final LocalDate dateOuverture;
  private final Set<FluxArgent> fluxArgents;

  private final Set<OperationImpossible> operationsImpossibles = new HashSet<>();

  public record OperationImpossible(LocalDate date, int valeurArgentAvantOperation, FluxArgent flux) {
    @Override
    public String toString() {
      return String.join(", ",
          date.toString(),
          flux.getArgent().getNom(),
          valeurArgentAvantOperation + "",
          flux.getNom(),
          flux.getFluxMensuel() + "");
    }
  }

  public void addOperationImpossible(OperationImpossible operationImpossible) {
    operationsImpossibles.add(operationImpossible);
  }

  public Argent(String nom, LocalDate t, int valeurComptable, Devise devise) {
    this(nom, t, t, valeurComptable, devise);
  }

  public Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Devise devise) {
    this(nom, dateOuverture, t, valeurComptable, new HashSet<>(), devise);
  }

  private Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Set<FluxArgent> fluxArgents, Devise devise) {
    super(nom, t, valeurComptable, devise);
    this.fluxArgents = fluxArgents;
    this.dateOuverture = dateOuverture;
  }

  public Argent(String nom, LocalDate t, int valeurComptable) {
    this(nom, t, t, valeurComptable);
  }

  public Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable) {
    this(nom, dateOuverture, t, valeurComptable, new HashSet<>());
  }

  private Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Set<FluxArgent> fluxArgents) {
    super(nom, t, valeurComptable);
    this.fluxArgents = fluxArgents;
    this.dateOuverture = dateOuverture;
  }

  @Override
  public Argent projectionFuture(LocalDate tFutur) {
    if (tFutur.isBefore(dateOuverture)) {
      return new Argent(nom, tFutur, 0, devise);
    }

    return new Argent(
        nom,
        dateOuverture,
        tFutur,
        valeurComptableFutur(tFutur),
        fluxArgents.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()), devise);
  }

  private int valeurComptableFutur(LocalDate tFutur) {
    var res = valeurComptable;
    for (var f : fluxArgents) {
      var financementsFuturs = valeurComptable - f.projectionFuture(tFutur).getArgent().getValeurComptable();
      var resAcc = res - financementsFuturs;
      if (!(this instanceof Dette) && resAcc < 0) {
        this.addOperationImpossible(new Argent.OperationImpossible(tFutur, res, f));
      }
      res = resAcc;
    }
    return res;
  }

  void addFinancés(FluxArgent fluxArgent) {
    fluxArgents.add(fluxArgent);
  }
}
