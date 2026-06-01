package school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class PieceJustificativeGenerator
    implements Function<Map<String, Object>, PieceJustificative> {
  @Override
  public PieceJustificative apply(Map<String, Object> args) {
    if (!args.containsKey("ref")) {
      return null;
    }

    if (!args.containsKey("link")) {
      return null;
    }

    if (!args.containsKey("date")) {
      return null;
    }

    if (!args.containsKey("id")) {
      return null;
    }

    var id = (String) args.get("id");
    var ref = (String) args.get("ref");
    var link = (String) args.get("link");
    var date = (LocalDate) args.get("date");

    if (id.isBlank() || ref.isBlank() || link.isBlank()) {
      return null;
    }

    return new PieceJustificative(id, date, ref, link);
  }
}
