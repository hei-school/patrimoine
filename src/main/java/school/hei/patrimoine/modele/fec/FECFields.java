package school.hei.patrimoine.modele.fec;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.decomposeur.PossessionCompteResolver;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;

public class FECFields {
  protected static String getPieceRef(Possession possession, Set<PieceJustificative> pjs) {
    var pj = getPj(possession, pjs);
    return pj != null ? pj.id() : possession.nom();
  }

  protected static String getPieceDate(Possession possession, Set<PieceJustificative> pjs) {
    var pj = getPj(possession, pjs);
    return pj != null ? formatFECDate(pj.date()) : formatFECDate(possession.t());
  }

  protected static String getEcritureLib(Possession possession) {
    return possession
        .nom()
        .replaceFirst("^" + possession.getTypeFEC().name() + "_", "")
        .replaceFirst("^" + possession.getTypeFEC().abrev() + "_", "")
        .replaceFirst("_\\d{4}_\\d{2}_\\d{2}$", "")
        .replace("_", " ")
        .replaceAll("(?<=[a-z])(?=[A-Z])", " ")
        .replaceAll("(?<=[a-zA-Z])(?=\\d)", " ")
        .replaceAll("(?<=\\d)(?=[a-zA-Z])", " ")
        .trim();
  }

  protected static void getDebitOrCredit(
      PossessionRecoupee possessionRecoupee, boolean isCreditor) {
    var valeurRealise = possessionRecoupee.valeurRealisee().convertir(Devise.EUR, LocalDate.now());
    var amount = valeurRealise.montant();

    if (amount == 0) {
      throw new IllegalStateException("Montant nul interdit en FEC");
    }

    if (isCreditor) {
      formatFECAmount(Math.abs(amount));
    } else {
      formatFECAmount(Math.abs(amount));
    }
  }

  protected static PieceJustificative getPj(Possession possession, Set<PieceJustificative> pjs) {
    return pjs.stream().filter(p -> p.id().equals(possession.nom())).findFirst().orElse(null);
  }

  protected static String formatFECDate(LocalDate t) {
    return String.join("", t.toString().split("-"));
  }

  protected static String formatFECAmount(double m) {
    return String.format(Locale.US, "%.2f", Math.abs(m));
  }

  protected static String getCompteLib(
      PossessionCompteResolver.Comptes comptes, boolean isCreditor) {
    if (isCreditor) {
      return comptes.compteCréditeur() != null ? comptes.compteCréditeur().nom() : "";
    }
    return comptes.compteDébiteur() != null ? comptes.compteDébiteur().nom() : "";
  }
}
