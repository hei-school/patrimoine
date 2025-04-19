package school.hei.patrimoine.patrilang.modele;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.Possession;

import java.util.Set;
import java.util.function.Function;

@RequiredArgsConstructor
public class PossessionGetter<T extends Possession> implements Function<String, T> {
    private final Set<T> possessions;

    @Override
    public T apply(String nom) {
        return this.possessions
            .stream()
            .filter(possession -> possession.nom().equals(nom))
            .findFirst()
            .orElseThrow();
    }
}
