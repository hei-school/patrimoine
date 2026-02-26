package school.hei.patrimoine.modele.fec;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record EcritureComptable(
    String id,
    LocalDate date,
    String libelle,
    List<LigneEcriture> lignes,
    LocalDate dateValidation) {}
