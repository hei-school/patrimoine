package school.hei.patrimoine.compiler;

import java.util.function.Function;
import lombok.SneakyThrows;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.PossessionSource;

public class PossessionExtractor implements Function<String, PossessionSource> {

  @SneakyThrows
  @Override
  public PossessionSource apply(String javaSource) {
    String importsData = extractImports(javaSource);
    String possessionsData = extractPossessions(javaSource);

    return new PossessionSource(importsData, possessionsData);
  }

  private String extractImports(String javaSource) {
    int classIndex = javaSource.indexOf("public class");
    if (classIndex == -1) {
      throw new RuntimeException("The source does not contain a 'public class' declaration.");
    }
    return javaSource.substring(0, classIndex).trim();
  }

  private String extractPossessions(String javaSource) {
    int firstBraceIndex = javaSource.indexOf("{");
    int lastBraceIndex = javaSource.lastIndexOf("}");

    if (firstBraceIndex != -1 && lastBraceIndex != -1 && lastBraceIndex > firstBraceIndex) {
      return javaSource.substring(firstBraceIndex + 1, lastBraceIndex).trim();
    } else {
      return "";
    }
  }
}
