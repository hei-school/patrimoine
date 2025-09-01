package school.hei.patrimoine.modele.recouppement;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.possession.CorrectionGenerateurFactory;

public record RecoupeurDePossessions(Set<Possession> prévus, Set<Possession> réalités) {
  public static RecoupeurDePossessions of(Patrimoine prévu, Patrimoine réalité) {
    return new RecoupeurDePossessions(prévu.getPossessions(), réalité.getPossessions());
  }

  public Set<Possession> getPossessionsExecutés() {
    return prévus.stream().filter(p -> getPossessionExecuté(p).isPresent()).collect(toSet());
  }

  public Set<Possession> getPossessionsNonExecutés() {
    return prévus.stream().filter(not(p -> getPossessionExecuté(p).isPresent())).collect(toSet());
  }

  public Set<Possession> getPossessionsNonPrévus() {
    return réalités.stream().filter(p -> getPossessionPrévu(p).isEmpty()).collect(toSet());
  }

  public Set<Correction> getCorrections() {
      Set<Correction> corrections = new HashSet<>();

      getPossessionsNonExecutés().forEach(p -> {
          var correctionGenerateur = CorrectionGenerateurFactory.make(p);
          corrections.addAll(correctionGenerateur.nonExecuté(p));
      });

      getPossessionsNonPrévus().forEach(p -> {
          var correctionGenerateur = CorrectionGenerateurFactory.make(p);
          corrections.addAll(correctionGenerateur.nonPrévu(p));
      });

      for(var prévu: getPossessionsExecutés()){
        var réalité = getPossessionExecuté(prévu).get();
        var correctionGenerateur = CorrectionGenerateurFactory.make(prévu);
        corrections.addAll(correctionGenerateur.comparer(prévu, réalité));
      }

      return corrections;
  }


  @SuppressWarnings("unchecked")
  private <T extends Possession> Optional<T> getPossessionPrévu(T réalité) {
    if (réalité instanceof FluxArgent) {
        //TOOD: handle
    }

    var prévu = prévus.stream().filter(p -> p.nom().equals(réalité.nom())).findFirst();
    if (!(prévu.isPresent() && prévu.get().getClass().equals(réalité.getClass()))) {
      throw new IllegalArgumentException("TODO: handle message");
    }

    return (Optional<T>) prévu;
  }

  @SuppressWarnings("unchecked")
  private <T extends Possession> Optional<T> getPossessionExecuté(T prévu) {
    if (prévu instanceof FluxArgent) {
        //TOOD: handle
    }

    var réalité = réalités.stream().filter(p -> p.nom().equals(prévu.nom())).findFirst();
    if (!(réalité.isPresent() && réalité.get().getClass().equals(prévu.getClass()))) {
      throw new IllegalArgumentException("TODO: handle message");
    }

    return (Optional<T>) réalité;
  }
}
