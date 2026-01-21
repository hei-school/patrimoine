package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import static org.reflections.Reflections.log;

import java.awt.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

public class SearchHighlighter {
  public static void highlightInTextComponent(JTextComponent component, String searchText) {
    if (searchText == null || searchText.isEmpty()) {
      return;
    }

    try {
      Highlighter highlighter = component.getHighlighter();
      String text = component.getText().toLowerCase();
      String textToSearch = searchText.toLowerCase();

      int index = 0;
      while ((index = text.indexOf(textToSearch, index)) >= 0) {
        highlighter.addHighlight(
            index,
            index + searchText.length(),
            new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
        index += searchText.length();
      }
    } catch (BadLocationException e) {
      log.error("Erreur lors du highlighting", e);
    }
  }

  public static String highlightInHtml(String html, String searchText) {
    if (searchText == null || searchText.isEmpty()) {
      return html;
    }

    int bodyStart = html.indexOf("<body");
    if (bodyStart == -1) {
      return html;
    }

    int bodyContentStart = html.indexOf('>', bodyStart);
    if (bodyContentStart == -1) {
      return html;
    }
    bodyContentStart++;

    int bodyEnd = html.lastIndexOf("</body>");
    if (bodyEnd == -1) {
      bodyEnd = html.length();
    }

    String beforeBody = html.substring(0, bodyContentStart);
    String bodyContent = html.substring(bodyContentStart, bodyEnd);
    String afterBody = html.substring(bodyEnd);

    String highlightedBody = highlightTextInHtml(bodyContent, searchText);

    return beforeBody + highlightedBody + afterBody;
  }

  private static String highlightTextInHtml(String html, String searchText) {
    var result = new StringBuilder();
    String lowerHtml = html.toLowerCase();
    String lowerSearch = searchText.toLowerCase();

    int position = 0;
    boolean insideTag = false;

    while (position < html.length()) {
      char c = html.charAt(position);

      if (c == '<') {
        insideTag = true;
        result.append(c);
        position++;
        continue;
      } else if (c == '>') {
        insideTag = false;
        result.append(c);
        position++;
        continue;
      }

      if (insideTag) {
        result.append(c);
        position++;
        continue;
      }

      if (position + lowerSearch.length() <= html.length()
          && lowerHtml.startsWith(lowerSearch, position)) {
        result.append("<span style='background-color: yellow;'>");
        result.append(html, position, position + searchText.length());
        result.append("</span>");
        position += searchText.length();

      } else {
        result.append(c);
        position++;
      }
    }

    return result.toString();
  }
}
