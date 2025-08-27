package school.hei.patrimoine.modele.recouppement;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;

import java.util.Optional;
import java.util.Set;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

public record RecoupeurDePossessions(Set<Possession> prévus, Set<Possession> réalités) {
    public static final String ID_DATE_SEPARATEUR = "--";

    public static RecoupeurDePossessions of(Patrimoine prévu, Patrimoine réalité){
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
        //TODO: implement
        return Set.of();
    }

    private <T extends Possession> Optional<T> getPossessionPrévu(T réalité) {
        //TODO: implement
        return null;
    }

    private <T extends Possession> Optional<T> getPossessionExecuté(T prévu) {
        //TODO: implementk
        return null;
    }
}
