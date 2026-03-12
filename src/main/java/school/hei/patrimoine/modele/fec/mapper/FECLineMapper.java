package school.hei.patrimoine.modele.fec.mapper;

import static java.time.LocalDate.now;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.FECLine;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.LigneEcriture;

public class FECLineMapper {
  public static FECLine toFECLine(
      Journal journal, EcritureComptable ecriture, LigneEcriture ligne) {
    var compte = ligne.compte();
    var compAux = ligne.compteAuxiliaire();
    var pj = ecriture.pj();

    var montantEUR = compte.compte().valeurComptable().convertir(EUR, now());
    var montantMGA = montantEUR.convertir(MGA, now()).montant();
    var debit = compte.isDebit() ? formatAmount(montantEUR.montant()) : "";
    var credit = !compte.isDebit() ? formatAmount(montantEUR.montant()) : "";

    return new FECLine(
        List.of(
            journal.code().toString(),
            journal.libelle(),
            ecriture.id(),
            formatDate(ecriture.date()),
            compte.typeComptable().toString(),
            compte.compte().nom(),
            compAux != null ? compAux.typeComptable().toString() : "",
            compAux != null ? compAux.compte().nom() : "",
            pj != null ? pj.id() : "",
            formatDate(pj != null ? pj.date() : null),
            ecriture.libelle(),
            debit,
            credit,
            ligne.lettrage() != null ? ligne.lettrage() : "",
            formatDate(ligne.dateLettrage()),
            formatDate(ecriture.dateValidation()),
            formatAmount(montantMGA),
            MGA.codeIso()));
  }

  private static String formatDate(LocalDate date) {
    return (date != null) ? date.toString().replace("-", "") : "";
  }

  private static String formatAmount(double montant) {
    return String.format(Locale.US, "%.2f", Math.abs(montant));
  }
}
