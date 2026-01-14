package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.possession.TypeAgregat.CORRECTION;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;

public final class Correction extends Possession {

  private final Possession possession;
  private final CompteCorrection compteCorrection;

  public Correction(FluxArgent fluxArgent) {
    super(
        String.format("Correction[p=%s,%s]", fluxArgent.getCompte().nom, fluxArgent.nom),
        fluxArgent.t,
        fluxArgent.valeurComptable);
    this.possession = fluxArgent.getCompte();
    this.compteCorrection = possession.getCompteCorrection();

    new FluxArgent(
        String.format(
            "Correction.Flux[compteCorrection=%s,%s]", compteCorrection.nom, fluxArgent.nom),
        compteCorrection.getCompte(),
        fluxArgent.getDebut(),
        fluxArgent.getFin(),
        fluxArgent.getDateOperation(),
        fluxArgent.getFluxMensuel());
  }

  public Correction(Possession possession, String raison, LocalDate t, Argent valeur) {
    super(
        String.format("Correction[p=%s,%s]", possession.nom, raison),
        t,
        new Argent(0, possession.getCompteCorrection().devise()));

    this.possession = possession;
    this.compteCorrection = possession.getCompteCorrection();

    new FluxArgent(
        String.format("Correction.Flux[compteCorrection=%s,%s]", compteCorrection.nom, raison),
        compteCorrection.getCompte(),
        t,
        valeur);
  }

  private Correction(Possession possession, CompteCorrection compteCorrection) {
    super(possession.nom, possession.t, possession.valeurComptable);
    this.possession = possession;
    this.compteCorrection = compteCorrection;
  }

  @Override
  public Correction projectionFuture(LocalDate tFutur) {
    return new Correction(possession.projectionFuture(tFutur), compteCorrection);
  }

  @Override
  public TypeAgregat typeAgregat() {
    return CORRECTION;
  }

  @Override
  public TypeFEC getTypeFEC() {
    return TypeFEC.AUTRE;
  }
}
