package school.hei.patrimoine.visualisation.swing.ihm.google.modele.fec.export;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.fec.mapper.FecLineMapper.toFecLine;

import com.opencsv.CSVWriter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.LigneEcriture;

public class FecWriter implements Closeable {
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

  public FecWriter(File file) throws IOException {
    this.writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
    writer.write('\uFEFF');
    this.csvWriter =
        new CSVWriter(
            writer,
            '|',
            CSVWriter.NO_QUOTE_CHARACTER,
            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END);
  }

  public void writeHeader() {
    csvWriter.writeNext(FEC_HEADER);
  }

  public void writeLine(Journal journal, EcritureComptable ecriture, LigneEcriture ligne) {
    csvWriter.writeNext(toFecLine(journal, ecriture, ligne).toArray());
  }

  @Override
  public void close() throws IOException {
    csvWriter.close();
    writer.close();
  }
}
