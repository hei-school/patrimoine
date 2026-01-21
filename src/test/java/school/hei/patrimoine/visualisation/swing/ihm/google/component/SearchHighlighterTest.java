package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.SearchHighlighter.highlightInTextComponent;

import javax.swing.*;
import org.junit.jupiter.api.Test;

class SearchHighlighterTest {

  @Test
  void highlight_multiple_matches_in_text_component() {
    var component = new JTextArea();
    String text =
        """
        * beneficeKoto2025 Dates:ajd, entrer 20000000Ar vers Trésoreries:businessMan
        * beneficeBema2025 Dates:ajd, entrer 15000000Ar vers Trésoreries:businessMan
        * beneficeRasoa2025 Dates:ajd, entrer 10000000Ar vers Trésoreries:businessMan
        """;
    component.setText(text);
    String searchText = "vers";

    highlightInTextComponent(component, searchText);
    var actual = component.getHighlighter().getHighlights().length;

    assertEquals(3, actual);
  }

  @Test
  void highlight_case_insensitive_in_text_component() {
    var component = new JTextArea();
    component.setText("Vers VERS vers VeRs");

    SearchHighlighter.highlightInTextComponent(component, "vers");
    var actual = component.getHighlighter().getHighlights().length;

    assertEquals(4, actual);
  }

  @Test
  void highlight_multiple_matches_in_html() {
    String html =
        """
        <html>
        <body>
            <p>Premier vers de texte</p>
            <p>Deuxième vers de texte</p>
            <p>Troisième vers de texte</p>
        </body>
        </html>
        """;
    String searchText = "vers";

    var expected =
        """
        <html>
        <body>
            <p>Premier <span style='background-color: yellow;'>vers</span> de texte</p>
            <p>Deuxième <span style='background-color: yellow;'>vers</span> de texte</p>
            <p>Troisième <span style='background-color: yellow;'>vers</span> de texte</p>
        </body>
        </html>
        """;

    var actual = SearchHighlighter.highlightInHtml(html, searchText);

    assertEquals(expected, actual);
  }

  @Test
  void should_not_highlight_inside_tags() {
    String html =
        """
        <html>
        <body>
            <div class="container">texte</div>
            <p>container dans le texte</p>
        </body>
        </html>
        """;
    String searchText = "container";

    var expected =
        """
        <html>
        <body>
            <div class="container">texte</div>
            <p><span style='background-color: yellow;'>container</span> dans le texte</p>
        </body>
        </html>
        """;

    var actual = SearchHighlighter.highlightInHtml(html, searchText);

    assertEquals(expected, actual);
  }
}
