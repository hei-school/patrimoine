package school.hei.patrimoine.modele.recouppement;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

import java.util.*;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.CompteCorrection;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.decomposeur.PossessionDecomposeurFactory;
import school.hei.patrimoine.modele.recouppement.generateur.CorrectionGenerateurFactory;

public record RecoupeurDePossessions(Set<Possession> prévus, Set<Possession> réalités) {
    public static RecoupeurDePossessions of(Patrimoine prévu, Patrimoine réalité) {
        return new RecoupeurDePossessions(prévu.getPossessions(), réalité.getPossessions());
    }

    public RecoupeurDePossessions(Set<Possession> prévus, Set<Possession> réalités) {
        this.prévus = withoutCompteCorrections(prévus).stream().map(p -> {
            var decomposeur = PossessionDecomposeurFactory.make(p);
            return decomposeur.apply(p);
        }).flatMap(Collection::stream).collect(toSet());

        this.réalités = withoutCompteCorrections(réalités).stream().map(p -> {
            var decomposeur = PossessionDecomposeurFactory.make(p);
            return decomposeur.apply(p);
        }).flatMap(Collection::stream).collect(toSet());
    }

    public Set<Possession> getPossessionsÉxecutés() {
        return prévus.stream().filter(p -> getEquivalent(réalités, p).isPresent()).collect(toSet());
    }

    public Set<Possession> getPossessionsNonExecutés() {
        return prévus.stream().filter(p -> getEquivalent(réalités, p).isEmpty()).collect(toSet());
    }

    public Set<Possession> getPossessionsNonPrévus() {
        return réalités.stream().filter(p -> getEquivalent(prévus, p).isEmpty()).collect(toSet());
    }

    public Set<Correction> getCorrections() {
        Set<Correction> corrections = new HashSet<>();

        getPossessionsNonExecutés().forEach(p -> {
            var correctionGenerateur = CorrectionGenerateurFactory.make(p);
            corrections.addAll(correctionGenerateur.nonÉxecuté(p));
        });

        getPossessionsNonPrévus().forEach(p -> {
            var correctionGenerateur = CorrectionGenerateurFactory.make(p);
            corrections.addAll(correctionGenerateur.nonPrévu(p));
        });

        for (var prévu : getPossessionsÉxecutés()) {
            var réalité = getEquivalent(réalités, prévu).get();
            var correctionGenerateur = CorrectionGenerateurFactory.make(prévu);
            corrections.addAll(correctionGenerateur.comparer(prévu, réalité));
        }

        return corrections;
    }

    public Set<Possession> getPossessionsÉxecutésAvecDifférences() {
        return getPossessionsÉxecutés().stream().filter(not(prévu -> {
            var réalité = getEquivalent(réalités, prévu).get();
            var correctionGenerateur = CorrectionGenerateurFactory.make(prévu);
            return correctionGenerateur.comparer(prévu, réalité).isEmpty();
        })).collect(toSet());
    }

    public Set<Possession> getPossessionsÉxecutésSansDifférences() {
        return getPossessionsÉxecutés().stream().filter(prévu -> {
            var réalité = getEquivalent(réalités, prévu).get();
            var correctionGenerateur = CorrectionGenerateurFactory.make(prévu);
            return correctionGenerateur.comparer(prévu, réalité).isEmpty();
        }).collect(toSet());
    }

    private static Optional<Possession> getEquivalent(Collection<Possession> possessions, Possession possession) {
        var equivalent = possessions.stream().filter(p -> p.nom().equals(possession.nom())).findFirst();

        if (equivalent.isPresent() && !equivalent.get().getClass().equals(possession.getClass())) {
            throw new IllegalArgumentException("TODO: handle message");
        }

        return equivalent;
    }

    private static Set<Possession> withoutCompteCorrections(Set<Possession> possessions)  {
        return possessions.stream().filter(not(p -> p instanceof CompteCorrection)).collect(toSet());
    }

}
