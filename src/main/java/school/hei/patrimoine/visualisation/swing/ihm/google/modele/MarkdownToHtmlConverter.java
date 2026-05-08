package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.util.function.Function;

public class MarkdownToHtmlConverter implements Function<String, String> {
  private final Parser parser;
  private final HtmlRenderer renderer;

  public MarkdownToHtmlConverter() {
    MutableDataSet options = new MutableDataSet();
    options.set(Parser.ASTERISK_DELIMITER_PROCESSOR, false);
    this.parser = Parser.builder(options).build();
    this.renderer = HtmlRenderer.builder(options).build();
  }

  @Override
  public String apply(String markdown) {
    var document = parser.parse(markdown);
    var html = renderer.render(document);

    return HTML_CONTENT.formatted(html);
  }

  private static final String HTML_CONTENT =
      """
      <html>
          <head>
            <style>
              body {
                background-color: #fff8dc;
                font-family: sans-serif;
                padding: 10px;
              }
              code {
                background-color: #ffdddd;
                padding: 2px 4px;
                border-radius: 4px;
                font-size: 13px;
              }
              li {
                  margin-bottom: 7px;
              }
            </style>
          </head>
          <body>
              %s
          </body>
      </html>
      """;
}
