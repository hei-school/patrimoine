package school.hei.patrimoine.visualisation.swing.ihm.google.component.html;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;
import static javax.swing.event.HyperlinkEvent.EventType.ACTIVATED;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.SearchHighlighter.highlightInHtml;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.SearchHighlighter.highlightInTextComponent;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode.EDIT;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode.VIEW;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.*;

import java.awt.*;
import java.util.Optional;
import java.util.Set;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.MarkdownToHtmlConverter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

@Slf4j
public class HtmlViewer extends JEditorPane {
  private final State state;
  private final MarkdownToHtmlConverter markdownToHtmlConverter;

  private ViewMode lastViewMode;
  private PatriLangFileContext lastSelectedFile;

  public HtmlViewer(State state) {
    this.state = state;
    this.markdownToHtmlConverter = new MarkdownToHtmlConverter();

    showEmptyContent();
    setBackground(new Color(255, 248, 220));

    addHyperlinkListener(
        e -> {
          if (e.getEventType() != ACTIVATED) {
            return;
          }

          new LinkOpener().accept(e.getURL().toString());
        });

    state.subscribe(Set.of("viewMode", "fontSize", "selectedFile", "searchText"), this::update);
  }

  private ViewMode getViewMode() {
    return state.get("viewMode");
  }

  private Optional<PatriLangFileContext> getSelectedFile() {
    return Optional.ofNullable(state.get("selectedFile"));
  }

  private int getFontSize() {
    return state.get("fontSize");
  }

  private String getSearchText() {
    return state.get("searchText") == null ? "" : state.get("searchText");
  }

  private Optional<ViewMode> getLastViewMode() {
    return Optional.ofNullable(lastViewMode);
  }

  private Optional<PatriLangFileContext> getLastSelectedFile() {
    return Optional.ofNullable(lastSelectedFile);
  }

  private void updateLastViewModeAndFile() {
    lastViewMode = getViewMode();
    lastSelectedFile = getSelectedFile().orElse(null);
  }

  private void showEmptyContent() {
    setContentType("text/html");
    setEditable(false);
    setText(
        "<html><body style='background-color:#fff8dc;'>Sélectionner un fichier pour"
            + " l'afficher.</body></html>");
  }

  private void showEditMode() {
    setEditable(true);
    setContentType("text/plain");
    var content = getContent(getSelectedFile().orElseThrow()).input().content();

    setText(content);
    if (!getSearchText().isBlank()) {
      highlightInTextComponent(this, getSearchText());
    }

    setCaretPosition(0);
  }

  private void showReadMode() {
    var content = getContent(getSelectedFile().orElseThrow()).input().content();
    var html = markdownToHtmlConverter.apply(new LinkReplacer().apply(content));

    if (getSearchText().isEmpty()) {
      html = highlightInHtml(html, getSearchText());
    }

    html = html.replace("<body>", "<body style='font-size: " + getFontSize() + "px;'>");
    html = html.replaceAll("<code>", "<code style='font-size: " + getFontSize() + "px;'>");

    setContentType("text/html");
    setEditable(false);
    setText(html);
    setCaretPosition(0);
  }

  private void showErrorContent() {
    setText("<html><body style='color:red;'>Erreur lors de la lecture du fichier.</body></html>");
  }

  public void update() {
    getHighlighter().removeAllHighlights();

    if (EDIT.equals(getLastViewMode().orElse(VIEW))) {
      saveLastFileToTempContent();
    }
    updateLastViewModeAndFile();

    if (getSelectedFile().isEmpty()) {
      showEmptyContent();
      return;
    }

    try {
      setFont(new Font(MONOSPACED, PLAIN, getFontSize()));
      if (EDIT.equals(getViewMode())) {
        showEditMode();
        return;
      }
      showReadMode();
    } catch (Exception ex) {
      ex.printStackTrace();
      log.error(ex.getMessage());
      showErrorContent();
    }
  }

  public void saveLastFileToTempContent() {
    if (getLastSelectedFile().isEmpty()) {
      return;
    }

    var lastFile = getLastSelectedFile().orElseThrow();
    var lastContent = getContent(lastFile);

    var currentContent = getText();
    if (lastContent.original() && currentContent.equals(lastContent.input().content())) {
      removeInTempContent(lastFile);
      return;
    }

    saveTempContent(lastFile, currentContent);
  }
}
