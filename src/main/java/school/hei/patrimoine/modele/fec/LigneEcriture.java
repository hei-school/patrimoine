package school.hei.patrimoine.modele.fec;

import java.time.LocalDate;
import lombok.Builder;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.TypeComptable;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

@Builder
public record LigneEcriture(
    Compte compte,
    Compte compteAuxiliaire,
    PieceJustificative pieceJustificative,
    Argent flux,
    String lettrage,
    LocalDate dateLettrage,
    TypeComptable type) {}
