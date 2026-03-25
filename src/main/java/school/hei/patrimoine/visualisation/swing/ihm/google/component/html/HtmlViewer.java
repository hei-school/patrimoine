package school.hei.patrimoine.visualisation.swing.ihm.google.component.html;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;
import static java.nio.file.Files.readString;
import static javax.swing.event.HyperlinkEvent.EventType.ACTIVATED;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.getSelectedFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.SearchHighlighter.highlightInHtml;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.SearchHighlighter.highlightInTextComponent;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode.EDIT;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode.VIEW;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher.ANY_FILE_MODIFIED;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.util.Set;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.MarkdownToHtmlConverter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

@Slf4j
public class HtmlViewer extends JEditorPane {
  private final State state;
  private final FileSideBar fileSidebar;
  private final MarkdownToHtmlConverter markdownToHtmlConverter;

  public HtmlViewer(State state, FileSideBar fileSidebar) {
    this.state = state;
    this.fileSidebar = fileSidebar;
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

    addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            if (!e.isTemporary() && EDIT.equals(getViewMode())) {
              saveCurrentFileToTemp();
              update();
            }
          }
        });

    state.subscribe(Set.of("viewMode", "fontSize", "selectedFile", "searchText"), this::update);

    AppContext.getDefault()
        .globalState()
        .subscribe(
            ANY_FILE_MODIFIED,
            () -> {
              if (VIEW.equals(getViewMode())) {
                return;
              }

              if (getSelectedFile(state).isEmpty()) {
                return;
              }

              try {
                setText(readString(getSelectedFile(state).get().toPath()));
              } catch (IOException e) {
                showErrorContent();
                return;
              }

              if (!getSearchText().isBlank()) {
                highlightInTextComponent(this, getSearchText());
              }

              setCaretPosition(0);
              showEditMode();
            });
  }

  private ViewMode getViewMode() {
    return state.get("viewMode");
  }

  private int getFontSize() {
    return state.get("fontSize");
  }

  private String getSearchText() {
    return state.get("searchText") == null ? "" : state.get("searchText");
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
    var content = getContent(getSelectedFile(state).orElseThrow()).input().content();

    setText(content);
    if (!getSearchText().isBlank()) {
      highlightInTextComponent(this, getSearchText());
    }

    setCaretPosition(0);
  }

  private void showReadMode() {
    var content = getContent(getSelectedFile(state).orElseThrow()).input().content();
    var html = markdownToHtmlConverter.apply(new LinkReplacer().apply(content));

    if (!getSearchText().isBlank()) {
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

    if (getSelectedFile(state).isEmpty()) {
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
      log.error(ex.getMessage());
      showErrorContent();
    }
  }

  public void saveCurrentFileToTemp() {
    var selectedFile = getSelectedFile(state);
    if (selectedFile.isEmpty()) {
      return;
    }

    var currentFile = selectedFile.orElseThrow();
    var lastContent = getContent(currentFile);

    var currentContent = getText();
    if (lastContent.original() && currentContent.equals(lastContent.input().content())) {
      removeInTempContent(currentFile);
      return;
    }

    saveTempContent(currentFile, currentContent);
    fileSidebar.refresh();
  }
}
