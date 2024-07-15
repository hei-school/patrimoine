package school.hei.patrimoine.modele;

import school.hei.patrimoine.modele.possession.FluxArgent;

import java.time.LocalDate;
import java.util.Set;

public record FluxImpossibles(LocalDate date, String nomArgent, int valeurArgent, Set<FluxArgent> flux) {
}