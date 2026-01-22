package school.hei.patrimoine.visualisation.swing.ihm.google.component.html;

import java.util.function.Function;
import java.util.regex.Pattern;

public class LinkReplacer implements Function<String, String> {
  private static final Pattern URL_PATTERN = Pattern.compile("\"(https?://[^\"]+)\"");

  @Override
  public String apply(String content) {
    var matcher = URL_PATTERN.matcher(content);
    var result = new StringBuilder();

    while (matcher.find()) {
      var url = matcher.group(1);
      matcher.appendReplacement(result, toAWithHref(url));
    }

    matcher.appendTail(result);
    return result.toString();
  }

  private static String toAWithHref(String url) {
    return String.format("<a href=\"%s\" target=\"_blank\">%s</a>", url, url);
  }
}
