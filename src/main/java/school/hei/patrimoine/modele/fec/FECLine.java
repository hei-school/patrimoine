package school.hei.patrimoine.modele.fec;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class FECLine {
  private final Map<FECColumn, String> values;

  public FECLine(Map<FECColumn, String> values) {
    this.values = new EnumMap<>(values);
  }

  public String[] values() {
    return Arrays.stream(FECColumn.values())
        .map(column -> values.getOrDefault(column, ""))
        .toArray(String[]::new);
  }

  public static String[] headers() {
    return Arrays.stream(FECColumn.values()).map(FECColumn::label).toArray(String[]::new);
  }
}
