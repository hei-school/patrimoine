package school.hei.patrimoine.utils;

import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.util.List;

@Slf4j
public class Comparator {
  public static boolean isFluxArgentEquals(FluxArgent a, FluxArgent b) {
    if (!a.nom().equals(b.nom())) {
      return false;
    }

    if (!a.getFluxMensuel().equals(b.getFluxMensuel())) {
      return false;
    }

    if (!a.getDebut().equals(b.getDebut())) {
      return false;
    }

    if (!a.getFin().equals(b.getFin())) {
      return false;
    }

    if(!a.getCompte().nom().equals(b.getCompte().nom())) {
      return false;
    }

    return a.getDateOperation() == b.getDateOperation();
  }

  public static boolean isFluxArgentEquals(List<FluxArgent> a, List<FluxArgent> b) {
    if(a.size() != b.size()) {
      return false;
    }

    for(int i = 0; i < a.size(); i++) {
      var fluxA = a.get(i);
      var fluxB = b.get(i);

      if(!isFluxArgentEquals(fluxA, fluxB)) {
        log.error("{} is not equals to {}", fluxA, fluxB);
        return false;
      }
    }

    return true;
  }
}
