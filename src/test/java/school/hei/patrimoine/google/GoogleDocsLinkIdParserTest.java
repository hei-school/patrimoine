package school.hei.patrimoine.google;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class GoogleDocsLinkIdParserTest {

  @Test
  void extract_google_docs_id_from_link_ok() {
    GoogleDocsLinkIdParser googleDocsLinkIdParser = new GoogleDocsLinkIdParser();
    String validLink = "https://docs.google.com/document/d/1abc2DEF3ghi_456JKL/view";

    String result = googleDocsLinkIdParser.apply(validLink);

    assertEquals("1abc2DEF3ghi_456JKL", result);
  }

  @Test
  void extract_google_docs_id_from_link_ko() {
    GoogleDocsLinkIdParser parser = new GoogleDocsLinkIdParser();
    String invalidLink = "https://example.com/invalid-link";

    Exception exception = assertThrows(RuntimeException.class, () -> parser.apply(invalidLink));

    assertEquals("Invalid Google Docs Link: " + invalidLink, exception.getMessage());
  }
}
