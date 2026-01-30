package school.hei.patrimoine.modele.fec;

import static school.hei.patrimoine.modele.fec.JournalCode.*;

import com.opencsv.CSVWriter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import javax.swing.*;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;

public class CsvExporter {
  public static final String CSV_FILE_EXTENSION = ".csv";

  private static final String DOWNLOADS_PATH = "Downloads";
  private static final String TELECHARGEMENTS_PATH = "Téléchargements";

  public static void exportCsv(List<PossessionRecoupee> possessions, Set<PieceJustificative> pjs)
      throws IOException {
    Optional<File> outputFile = getOutputFile();
    if (outputFile.isEmpty()) return;

    try (Writer writer =
            new OutputStreamWriter(new FileOutputStream(outputFile.get()), StandardCharsets.UTF_8);
        var csvWriter =
            new CSVWriter(
                writer,
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {

      writer.write('\uFEFF');

      csvWriter.writeNext(getFecHeader());

      int sequence = 0;
      for (PossessionRecoupee p : possessions) {
        if (p.ecartValeurAvecRealises().montant() != 0) continue;

        csvWriter.writeNext(getFecLine(p, pjs, sequence));
        sequence++;
      }
    }
  }

  private static Optional<File> getOutputFile() {
    var fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Exporter le fichier CSV");
    fileChooser.setCurrentDirectory(getDefaultDir());
    fileChooser.setSelectedFile(new File("FEC.csv"));

    if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
      return Optional.empty();
    }

    File outputFile = fileChooser.getSelectedFile();

    if (!outputFile.getName().toLowerCase().endsWith(CSV_FILE_EXTENSION)) {
      outputFile = new File(outputFile.getParentFile(), outputFile.getName() + CSV_FILE_EXTENSION);
    }

    if (!isFileNameAvailable(outputFile)) return Optional.empty();

    return Optional.of(outputFile);
  }

  private static File getDefaultDir() {
    var home = new File(System.getProperty("user.home"));
    var downloads = new File(home, DOWNLOADS_PATH);

    if (!downloads.exists()) {
      downloads = new File(home, TELECHARGEMENTS_PATH);
    }

    return downloads.exists() ? downloads : home;
  }

  private static String[] getFecHeader() {
    return new String[] {
      "JournalCode",
      "JournalLib",
      "EcritureNum",
      "EcritureDate",
      "CompteNum",
      "CompteLib",
      "CompAuxNum",
      "CompAuxLib",
      "PieceRef",
      "PieceDate",
      "EcritureLib",
      "Debit",
      "Credit",
      "EcritureLet",
      "DateLet",
      "ValidDate",
      "Montantdevise",
      "Idevise"
    };
  }

  private static String[] getFecLine(
      PossessionRecoupee possessionRecoupee, Set<PieceJustificative> pjs, int sequence) {
    Possession possession = possessionRecoupee.possession();

    var currentjournalCode = JN;

    var journalCode = currentjournalCode.toString();
    var journalLib = currentjournalCode.getJournalLib();
    var ecritureNum = journalCode + String.format("%03d", sequence);
    var ecritureDate = formatFECDate(possession.t());
    var compteNum = "";
    var compteLib = possession.nom();
    var compAuxNum = "";
    var compAuxLib = "";
    var pj = getPj(possession, pjs);
    var pieceRef = pj != null ? pj.id() : "";
    var pieceDate = pj != null ? formatFECDate(pj.date()) : "";
    var ecritureLib = "";

    var valeurRealise = possessionRecoupee.valeurRealisee().convertir(Devise.EUR, LocalDate.now());
    var debit = "";
    var credit = "";
    var amount = valeurRealise.montant();
    if (amount < 0) {
      debit = formatFECAmount(amount);
    } else {
      credit = formatFECAmount(amount);
    }

    var ecritureLet = "";
    var dateLet = "";
    var validDate = "";
    var montantDevise =
        formatFECAmount(valeurRealise.convertir(Devise.MGA, LocalDate.now()).montant());
    var idevise = possessionRecoupee.valeurRealisee().devise().codeIso();

    return new String[] {
      journalCode,
      journalLib,
      ecritureNum,
      ecritureDate,
      compteNum,
      compteLib,
      compAuxNum,
      compAuxLib,
      pieceRef,
      pieceDate,
      ecritureLib,
      debit,
      credit,
      ecritureLet,
      dateLet,
      validDate,
      montantDevise,
      idevise
    };
  }

  private static PieceJustificative getPj(Possession possession, Set<PieceJustificative> pjs) {
    return pjs.stream().filter(p -> p.id().equals(possession.nom())).findFirst().orElse(null);
  }

  private static boolean isFileNameAvailable(File file) {
    if (!file.exists()) {
      return true;
    }

    return JOptionPane.showConfirmDialog(
            null,
            "Le fichier existe déjà.\nVoulez-vous le remplacer ?",
            "Fichier existant",
            JOptionPane.YES_NO_OPTION)
        == JOptionPane.YES_OPTION;
  }

  private static String formatFECDate(LocalDate t) {
    return String.join("", t.toString().split("-"));
  }

  private static String formatFECAmount(double m) {
    return String.format(Locale.US, "%.2f", Math.abs(m));
  }
}
