package school.hei.patrimoine.modele.fec;

import static school.hei.patrimoine.modele.fec.mapper.FECLineMapper.toFECLine;

import com.opencsv.CSVWriter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public class FECWriter implements Closeable {
  private final Writer writer;
  private final CSVWriter csvWriter;

  private static final String[] FEC_HEADER = {
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

  public FECWriter(Path path) throws IOException {
    this.writer = new OutputStreamWriter(Files.newOutputStream(path), StandardCharsets.UTF_8);
    writer.write('\uFEFF');

    this.csvWriter =
        new CSVWriter(
            writer,
            '|',
            CSVWriter.NO_QUOTE_CHARACTER,
            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END);
  }

  public void writeFEC(Collection<Journal> journals) {
    writeHeader();
    writeLines(journals);
  }

  private void writeLines(Collection<Journal> journals) {
    for (var journal : journals) {
      for (var ecriture : journal.ecritures()) {
        for (var ligne : ecriture.lignes()) {
          writeLine(journal, ecriture, ligne);
        }
      }
    }
  }

  private void writeHeader() {
    csvWriter.writeNext(FEC_HEADER);
  }

  private void writeLine(Journal journal, EcritureComptable ecriture, LigneEcriture ligne) {
    csvWriter.writeNext(toFECLine(journal, ecriture, ligne).toArray());
  }

  @Override
  public void close() throws IOException {
    csvWriter.close();
    writer.close();
  }
}
