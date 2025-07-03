package school.hei.patrimoine.compiler;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.CAS_FILE_EXTENSION;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.TOUT_CAS_FILE_EXTENSION;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatriLangFileNameExtractor implements FileNameExtractor {
  private static final String CAS_SET_FILENAME = "CasSet";
  private static final List<Pattern> PATRILANG_FILE_NAME_PATTERNS =
      List.of(Pattern.compile("Cas de (\\w+)"), Pattern.compile("Cas De (\\w+)"));

  @Override
  public String apply(String code) {
    for (Pattern pattern : PATRILANG_FILE_NAME_PATTERNS) {
      Matcher matcher = pattern.matcher(code);

      if (matcher.find()) {
        return matcher.group(1) + CAS_FILE_EXTENSION;
      }
    }

    return CAS_SET_FILENAME + TOUT_CAS_FILE_EXTENSION;
  }
}
