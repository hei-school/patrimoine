package school.hei.patrimoine.compiler;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatriLangFileNameExtractor implements FileNameExtractor {
  private static final String CAS_SET_FILENAME = "CasSet";
  private static final List<Pattern> TOUT_CAS_FILE_NAME_PATTERN =
      List.of(Pattern.compile("Objectif final"), Pattern.compile("Objectif Final"));

  private static final List<Pattern> CAS_FILE_NAME_PATTERNS =
      List.of(Pattern.compile("Cas de (\\w+)"), Pattern.compile("Cas De (\\w+)"));

  private static final List<Pattern> PJ_FILE_NAME_PATTERNS =
      List.of(
          Pattern.compile("Pièces justificatives"),
          Pattern.compile("Pièces Justificatives"),
          Pattern.compile("Pieces justificatives"),
          Pattern.compile("Pieces Justificatives"));

  @Override
  public String apply(String code) {
    for (Pattern pattern : PJ_FILE_NAME_PATTERNS) {
      Matcher matcher = pattern.matcher(code);
      if (matcher.find()) {
        for (Pattern casPattern : CAS_FILE_NAME_PATTERNS) {
          Matcher casNameMatcher = casPattern.matcher(code);
          if (casNameMatcher.find()) {
            return casNameMatcher.group(1) + PJ_FILE_EXTENSION;
          }
        }
        throw new IllegalArgumentException("PJ file without 'Cas de ...' declaration.");
      }
    }

    for (Pattern pattern : CAS_FILE_NAME_PATTERNS) {
      Matcher matcher = pattern.matcher(code);

      if (matcher.find()) {
        return matcher.group(1) + CAS_FILE_EXTENSION;
      }
    }

    for (Pattern pattern : TOUT_CAS_FILE_NAME_PATTERN) {
      Matcher matcher = pattern.matcher(code);

      if (matcher.find()) {
        return CAS_SET_FILENAME + TOUT_CAS_FILE_EXTENSION;
      }
    }

    throw new IllegalArgumentException("Wrong PatriLang content detected.");
  }
}
