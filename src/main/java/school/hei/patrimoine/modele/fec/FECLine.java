package school.hei.patrimoine.modele.fec;

import java.util.Arrays;
import java.util.List;

public class FECLine {
  private final List<String> values;

  public FECLine(List<String> values) {
    this.values = values;
  }

  public String[] toArray() {
    return values.toArray(String[]::new);
  }

  public static String[] header() {
    return Arrays.stream(FECColumn.values()).map(FECColumn::label).toArray(String[]::new);
  }
}
