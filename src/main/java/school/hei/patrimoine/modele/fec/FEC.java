package school.hei.patrimoine.modele.fec;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FEC {
  private final List<Journal> journals;

  public File export(Path outputPath) {
    // call writer
    return null;
  }
}
