package school.hei.patrimoine.modele.fec;

import java.util.Arrays;
import java.util.List;

public record FECLine(List<String> values) {
  public String[] toArray() {
    return values.toArray(String[]::new);
  }

  public static String[] header() {
    return Arrays.stream(FECColumn.values()).map(FECColumn::label).toArray(String[]::new);
  }
}
