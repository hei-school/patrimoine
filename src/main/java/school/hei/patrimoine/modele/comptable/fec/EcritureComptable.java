package school.hei.patrimoine.modele.comptable.fec;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

@Builder
public record EcritureComptable(
    String id,
    LocalDate date,
    String libelle,
    Argent valeur,
    PieceJustificative pj,
    LocalDate dateValidation,
    List<LigneEcriture> lignes) {}
