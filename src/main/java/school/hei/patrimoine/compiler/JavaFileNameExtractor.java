package school.hei.patrimoine.compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaFileNameExtractor implements FileNameExtractor {
  private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("public class (\\w+)");
  public static String JAVA_FILE_EXTENSION = ".java";

  @Override
  public String apply(String code) {
    Matcher matcher = CLASS_NAME_PATTERN.matcher(code);
    if (matcher.find()) {
      return matcher.group(1) + JAVA_FILE_EXTENSION;
    } else {
      throw new IllegalArgumentException("No class name found in the provided code.");
    }
  }
}
