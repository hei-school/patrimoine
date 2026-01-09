package school.hei.patrimoine.modele.decomposeur;

import java.time.LocalDate;
import java.util.List;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Possession;

public class GroupePossessionDecomposeur
    extends PossessionDecomposeurBase<GroupePossession, Possession> {
  public GroupePossessionDecomposeur(LocalDate finSimulation) {
    super(finSimulation);
  }

  @Override
  public List<Possession> apply(GroupePossession groupePossession) {
    return groupePossession.getPossessions().stream()
        .map(
            possession -> {
              var decomposeur = PossessionDecomposeurFactory.make(possession, getFinSimulation());
              return decomposeur.apply(possession);
            })
        .flatMap(List::stream)
        .toList();
  }
}
