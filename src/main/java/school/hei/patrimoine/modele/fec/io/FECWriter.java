package school.hei.patrimoine.modele.fec.io;

import static com.opencsv.ICSVWriter.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newOutputStream;
import static school.hei.patrimoine.modele.fec.FECLine.headers;
import static school.hei.patrimoine.modele.fec.mapper.FECLineMapper.toFECLine;

import com.opencsv.CSVWriter;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import school.hei.patrimoine.modele.fec.Journal;

public class FECWriter implements Closeable {
  private final Writer writer;
  private final CSVWriter csvWriter;

  private static final char UTF8_BOM = '\uFEFF';

  public FECWriter(Path path) throws IOException {
    this.writer = new OutputStreamWriter(newOutputStream(path), UTF_8);
    writer.write(UTF8_BOM);

    this.csvWriter =
        new CSVWriter(writer, '\t', NO_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);
  }

  public void writeFEC(Collection<Journal> journals) {
    List<String[]> allLines = new ArrayList<>();
    allLines.add(headers());

    for (var journal : journals) {
      for (var ecriture : journal.ecritures()) {
        for (var ligne : ecriture.lignes()) {
          var line = toFECLine(journal, ecriture, ligne);
          allLines.add(line.values());
        }
      }
    }

    int chunckSize = 1_000;
    for (int i = 0; i < allLines.size(); i += chunckSize) {
      csvWriter.writeAll(allLines.subList(i, Math.min(i + chunckSize, allLines.size())));
    }
  }

  @Override
  public void close() throws IOException {
    csvWriter.close();
    writer.close();
  }
}
