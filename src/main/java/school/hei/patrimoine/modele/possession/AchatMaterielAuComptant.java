package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import java.util.Set;

public final class AchatMaterielAuComptant extends Possession {

  private final GroupePossession achatCommeGroupe;

  public AchatMaterielAuComptant(
      String nom, LocalDate dateAchat, int valeurComptableALAchat, double tauxAppreciationAnnuelle, Argent financeur) {
    super(nom, dateAchat, valeurComptableALAchat);
    this.achatCommeGroupe = new GroupePossession(
        nom,
        dateAchat,
        Set.of(
            new Materiel(nom, dateAchat, valeurComptableALAchat, dateAchat, tauxAppreciationAnnuelle),
            new FluxArgent(
                "Financement AchatMaterielAuComptant: " + nom,
                financeur,
                dateAchat,
                dateAchat,
                -1 * valeurComptableALAchat,
                dateAchat.getDayOfMonth())));
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    return achatCommeGroupe.projectionFuture(tFutur);
  }
}
