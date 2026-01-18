package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement;

import java.text.Normalizer;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

@Slf4j
public class PossessionRecoupeeListPanel extends JPanel {
  private final State state;

  public PossessionRecoupeeListPanel(State state) {
    super();
    this.state = state;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(true);
  }

  public void update(
      Set<PossessionRecoupee> possessionRecoupees, Set<PieceJustificative> piecesJustificatives) {
    removeAll();

    var pjSet = piecesJustificatives == null ? Set.<PieceJustificative>of() : piecesJustificatives;

    log.info(
        "PossessionRecoupeeListPanel.update -> possessions="
            + possessionRecoupees.size()
            + " pjs="
            + pjSet.size());

    possessionRecoupees.forEach(
        possessionRecoupee -> {
          var name = possessionRecoupee.possession().nom();
          var matched = findMatchingPiece(pjSet, name);

          if (matched == null && !pjSet.isEmpty()) {
            var candidates =
                pjSet.stream().map(PieceJustificative::id).collect(Collectors.joining(", "));
            log.info("Aucune PJ trouvée pour \"" + name + "\". Candidates: [" + candidates + "]");
          } else if (matched != null) {
            log.info("PJ trouvée pour \"" + name + "\" -> " + matched.id());
          }

          add(new PossessionRecoupeeItem(state, possessionRecoupee, matched));
          add(Box.createVerticalStrut(10));
        });

    revalidate();
    repaint();
  }

  public boolean hasMatchingPiece(PieceJustificative pj, String possessionName) {
    var normPoss = normalizeAndStrip(stripTrailingDate(possessionName));
    var idNorm = normalizeAndStrip(stripTrailingDate(pj.id()));
    var linkNorm = pj.link() == null ? "" : normalizeAndStrip(stripTrailingDate(pj.link()));

    return idNorm.equals(normPoss)
        || stripExtensions(idNorm).equals(stripExtensions(normPoss))
        || idNorm.contains(normPoss)
        || normPoss.contains(idNorm)
        || linkNorm.contains(normPoss)
        || normPoss.contains(linkNorm);
  }

  public JScrollPane toScrollPane() {
    var scroll =
        new JScrollPane(
            this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.getVerticalScrollBar().setUnitIncrement(20);

    return scroll;
  }

  private PieceJustificative findMatchingPiece(
      Set<PieceJustificative> pjSet, String possessionName) {
    if (possessionName == null || pjSet == null || pjSet.isEmpty()) {
      return null;
    }

    var normalizePossession = normalizeAndStrip(stripTrailingDate(possessionName));

    var exactId =
        pjSet.stream()
            .filter(pj -> normalizeAndStrip(stripTrailingDate(pj.id())).equals(normalizePossession))
            .findFirst()
            .orElse(null);
    if (exactId != null) return exactId;

    var exactLink =
        pjSet.stream()
            .filter(
                pj ->
                    pj.link() != null
                        && normalizeAndStrip(stripTrailingDate(pj.link()))
                            .equals(normalizePossession))
            .findFirst()
            .orElse(null);
    if (exactLink != null) return exactLink;

    var withoutExt = stripExtensions(normalizePossession);
    var matchWithoutExt =
        pjSet.stream()
            .filter(
                pj ->
                    stripExtensions(normalizeAndStrip(stripTrailingDate(pj.id())))
                            .equals(withoutExt)
                        || (pj.link() != null
                            && stripExtensions(normalizeAndStrip(stripTrailingDate(pj.link())))
                                .equals(withoutExt)))
            .findFirst()
            .orElse(null);
    if (matchWithoutExt != null) return matchWithoutExt;

    var fallback =
        pjSet.stream()
            .filter(
                pj -> {
                  var idNorm = normalizeAndStrip(stripTrailingDate(pj.id()));
                  var linkNorm =
                      pj.link() == null ? "" : normalizeAndStrip(stripTrailingDate(pj.link()));
                  return idNorm.contains(normalizePossession)
                      || normalizePossession.contains(idNorm)
                      || linkNorm.contains(normalizePossession)
                      || normalizePossession.contains(linkNorm);
                })
            .findFirst()
            .orElse(null);
    if (fallback != null) return fallback;

    return null;
  }

  private String normalizeAndStrip(String text) {
    if (text == null) return "";
    var norme = text.trim().toLowerCase();
    norme = Normalizer.normalize(norme, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    norme = norme.replaceAll("[_\\-\\\\/]+", " ");
    norme = norme.replaceAll("\\text+", " ").trim();
    return norme;
  }

  private String stripExtensions(String text) {
    if (text == null) return "";
    var out = text;
    out = out.replaceAll("\\.pj\\.md$", "");
    out = out.replaceAll("\\.pj$", "");
    out = out.replaceAll("\\.md$", "");
    out = out.replaceAll("\\.pdf$", "");
    return out;
  }

  private String stripTrailingDate(String text) {
    if (text == null) return "";
    var out = text.replaceAll("\\text*\\(\\d{1,2}/\\d{1,2}/\\d{2,4}\\)\\text*$", "");
    out = out.replaceAll("\\text*\\d{1,2}/\\d{1,2}/\\d{2,4}\\text*$", "");
    out = out.replaceAll("\\text*\\d{4}-\\d{2}-\\d{2}\\text*$", "");
    return out.trim();
  }
}
