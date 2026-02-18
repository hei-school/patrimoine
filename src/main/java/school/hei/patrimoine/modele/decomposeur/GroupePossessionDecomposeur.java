package school.hei.patrimoine.modele.decomposeur;

import static school.hei.patrimoine.modele.decomposeur.PossessionDecomposeurFacade.decompose;

import java.time.LocalDate;
import java.util.List;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Possession;

public class GroupePossessionDecomposeur
    extends PossessionDecomposeurBase<GroupePossession, Possession> {
  public GroupePossessionDecomposeur(LocalDate debut, LocalDate fin) {
    super(debut, fin);
  }

  @Override
  public List<Possession> apply(GroupePossession groupePossession) {
    return groupePossession.getPossessions().stream()
        .map(possession -> decompose(possession, getDebut(), getFin()))
        .flatMap(List::stream)
        .toList();
  }
}
