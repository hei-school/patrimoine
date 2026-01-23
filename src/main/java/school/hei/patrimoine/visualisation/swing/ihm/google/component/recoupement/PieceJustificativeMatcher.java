package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.text.Normalizer;
import java.util.Set;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class PieceJustificativeMatcher {
  public PieceJustificative findMatchingPiece(
      Set<PieceJustificative> pieces, String possessionName) {

    if (possessionName == null || pieces == null || pieces.isEmpty()) {
      return null;
    }

    var normalizedPossession = normalize(possessionName);

    return pieces.stream().filter(pj -> matches(pj, normalizedPossession)).findFirst().orElse(null);
  }

  private boolean matches(PieceJustificative pj, String possession) {
    var id = normalize(pj.id());
    var link = normalize(pj.link());

    return equalsOrContains(id, possession)
        || equalsOrContains(stripExtension(id), stripExtension(possession))
        || equalsOrContains(link, possession);
  }

  private boolean equalsOrContains(String a, String b) {
    return a.equals(b) || a.contains(b) || b.contains(a);
  }

  private String normalize(String input) {
    if (input == null) return "";

    var text = input.trim().toLowerCase();
    text = removeAccents(text);
    text = replaceSeparators(text);
    text = removeTrailingDate(text);

    return text.trim();
  }

  private String removeAccents(String text) {
    return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
  }

  private String replaceSeparators(String text) {
    return text.replace('_', ' ').replace('-', ' ').replace('/', ' ').replace('\\', ' ');
  }

  private String stripExtension(String text) {
    if (text.endsWith(".pj.md")) return text.substring(0, text.length() - 6);
    if (text.endsWith(".pj")) return text.substring(0, text.length() - 3);
    if (text.endsWith(".md")) return text.substring(0, text.length() - 3);
    if (text.endsWith(".pdf")) return text.substring(0, text.length() - 4);
    return text;
  }

  private String removeTrailingDate(String text) {
    var trimmed = text.trim();

    if (endsWithDateBetweenParentheses(trimmed)) {
      return trimmed.substring(0, trimmed.lastIndexOf('(')).trim();
    }

    if (endsWithSimpleDate(trimmed)) {
      return trimmed.substring(0, trimmed.lastIndexOf(' ')).trim();
    }

    return trimmed;
  }

  private boolean endsWithDateBetweenParentheses(String text) {
    return text.matches(".*\\(\\d{1,2}/\\d{1,2}/\\d{2,4}\\)$");
  }

  private boolean endsWithSimpleDate(String text) {
    return text.matches(".*\\d{4}-\\d{2}-\\d{2}$") || text.matches(".*\\d{1,2}/\\d{1,2}/\\d{2,4}$");
  }
}
