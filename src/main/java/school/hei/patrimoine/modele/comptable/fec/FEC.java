package school.hei.patrimoine.modele.comptable.fec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.comptable.fec.io.FECWriter;

@RequiredArgsConstructor
public class FEC {
  private final List<Journal> journals;

  public File export(Path outputPath) {
    try (var fecWriter = new FECWriter(outputPath)) {
      fecWriter.writeFEC(journals);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return outputPath.toFile();
  }
}
