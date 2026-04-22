package school.hei.patrimoine.modele.comptable.fec.mapper;

import static java.time.LocalDate.now;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.Devise.MGA;
import static school.hei.patrimoine.modele.comptable.fec.FECColumn.*;

import java.time.LocalDate;
import java.util.*;
import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.modele.comptable.CompteComptable;
import school.hei.patrimoine.modele.comptable.MouvementComptable;
import school.hei.patrimoine.modele.comptable.fec.*;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class FECLineMapper {
  private final CompteNumResolver compteNumResolver;

  public FECLineMapper(CompteNumResolver compteNumResolver) {
    this.compteNumResolver = compteNumResolver;
  }

  public FECLine toFECLine(Journal journal, EcritureComptable ecriture, LigneEcriture ligne) {
    var compte = ligne.compteComptable();
    var compAux = ligne.compteAuxiliaire();
    var pj = ecriture.pj();

    var montantEUR = ecriture.valeur().convertir(EUR, now());
    var montantMGA = montantEUR.convertir(MGA, now()).montant();
    var debit =
        compte.mouvementComptable() == MouvementComptable.DEBIT
            ? formatAmount(montantEUR.montant())
            : formatAmount(0);
    var credit =
        compte.mouvementComptable() == MouvementComptable.CREDIT
            ? formatAmount(montantEUR.montant())
            : formatAmount(0);

    var values =
        getFecColumnStringMap(
            journal, ecriture, ligne, compte, compAux, pj, debit, credit, montantMGA);

    return new FECLine(values);
  }

  private @NonNull Map<FECColumn, String> getFecColumnStringMap(
      Journal journal,
      EcritureComptable ecriture,
      LigneEcriture ligne,
      CompteComptable compte,
      CompteComptable compAux,
      PieceJustificative pj,
      String debit,
      String credit,
      double montantMGA) {
    Map<FECColumn, String> values = new EnumMap<>(FECColumn.class);
    values.put(JOURNAL_CODE, journal.code().toString());
    values.put(JOURNAL_LIB, journal.libelle());
    values.put(ECRITURE_NUM, ecriture.id());
    values.put(ECRITURE_DATE, formatDate(ecriture.date()));
    values.put(COMPTE_NUM, compteNumResolver.resolve(compte));
    values.put(COMPTE_LIB, compte.compte().nom());
    values.put(COMP_AUX_NUM, compAux != null ? compAux.typeComptable().toString() : "");
    values.put(COMP_AUX_LIB, compAux != null ? compAux.compte().nom() : "");
    values.put(PIECE_REF, pj != null ? pj.id() : "");
    values.put(PIECE_DATE, formatDate(pj != null ? pj.date() : null));
    values.put(ECRITURE_LIB, ecriture.libelle());
    values.put(DEBIT, debit);
    values.put(CREDIT, credit);
    values.put(ECRITURE_LET, ligne.lettrage() != null ? ligne.lettrage() : "");
    values.put(DATE_LET, formatDate(ligne.dateLettrage()));
    values.put(VALID_DATE, formatDate(ecriture.dateValidation()));
    values.put(MONTANT_DEVISE, formatAmount(montantMGA));
    values.put(IDEVISE, MGA.codeIso());

    return values;
  }

  private static String formatDate(LocalDate date) {
    return (date != null) ? date.toString().replace("-", "") : "";
  }

  private static String formatAmount(double montant) {
    return String.format(Locale.US, "%.2f", Math.abs(montant));
  }
}
