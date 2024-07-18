package school.hei.patrimoine.modele;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.possession.FluxArgent;

public record FluxImpossibles(
    LocalDate date, String nomArgent, int valeurArgent, Set<FluxArgent> flux) {}
