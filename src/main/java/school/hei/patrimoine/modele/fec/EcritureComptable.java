package school.hei.patrimoine.modele.fec;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

@Builder
public record EcritureComptable(
    String id,
    LocalDate date,
    String libelle,
    PieceJustificative pj,
    LocalDate dateValidation,
    List<LigneEcriture> lignes) {}
