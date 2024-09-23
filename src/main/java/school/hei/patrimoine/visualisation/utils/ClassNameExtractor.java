package school.hei.patrimoine.visualisation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassNameExtractor {
    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("public class (\\w+)");

    public static String extractClassName(String code) {
        Matcher matcher = CLASS_NAME_PATTERN.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalArgumentException("No class name found in the provided code.");
        }
    }
}
