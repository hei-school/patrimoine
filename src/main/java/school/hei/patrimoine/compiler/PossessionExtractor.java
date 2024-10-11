package school.hei.patrimoine.compiler;

import java.util.function.Function;
import lombok.SneakyThrows;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.ExtractedPossession;

public class PossessionExtractor implements Function<String, ExtractedPossession> {

  @SneakyThrows
  @Override
  public ExtractedPossession apply(String javaSource) {
    String importsData = extractImports(javaSource);
    String possessionsData = extractPossessions(javaSource);

    return new ExtractedPossession(importsData, possessionsData);
  }

  private String extractImports(String javaSource) {
    int classIndex = javaSource.indexOf("public class");
    if (classIndex != -1) {
      return javaSource.substring(0, classIndex).trim();
    } else {
      return "";
    }
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
