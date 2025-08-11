package school.hei.patrimoine.google;

import static java.nio.file.StandardOpenOption.*;
import static school.hei.patrimoine.compiler.CompilerUtilities.DOWNLOADS_DIRECTORY_PATH;
import static school.hei.patrimoine.google.GoogleApi.*;

import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.Document;
import com.google.api.services.docs.v1.model.ParagraphElement;
import com.google.api.services.docs.v1.model.StructuralElement;
import com.google.api.services.docs.v1.model.TextRun;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import school.hei.patrimoine.compiler.FileNameExtractor;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;

public record DocsApi(Docs docsService) {
  public DocsApi(AuthDetails authDetails) {
    this(
        new Docs.Builder(authDetails.httpTransport(), JSON_FACTORY, authDetails.credential())
            .setApplicationName(APPLICATION_NAME)
            .build());
  }

  public String read(String docId) throws GoogleIntegrationException {
    try {
      Document doc = docsService.documents().get(docId).execute();
      List<StructuralElement> bodyElements = doc.getBody().getContent();
      StringBuilder combinedContent = new StringBuilder();

      // Iterate through the body elements
      for (StructuralElement element : bodyElements) {
        if (element.getParagraph() != null) {
          // For each paragraph, get the text content
          List<ParagraphElement> elements = element.getParagraph().getElements();
          for (ParagraphElement e : elements) {
            TextRun textRun = e.getTextRun();
            if (textRun != null && textRun.getContent() != null) {
              combinedContent.append(textRun.getContent());
            }
          }
        }
      }

      return combinedContent.toString();
    } catch (IOException e) {
      throw new GoogleIntegrationException(
          "Échec de la lecture du contenu du document Google Docs", e);
    }
  }

  public void download(String docId, FileNameExtractor fileNameExtractor)
      throws GoogleIntegrationException {
    var code = read(docId);
    var filename = fileNameExtractor.apply(code);
    var path = new File(DOWNLOADS_DIRECTORY_PATH).toPath().resolve(filename);

    try {
      Files.write(path, code.getBytes(), CREATE, TRUNCATE_EXISTING, WRITE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
