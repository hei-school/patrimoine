package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.GoogleApiUtilities;

@Slf4j
public record GoogleLinkListCacheManager() {
  public void save(GoogleLinkList<GoogleLinkList.NamedLink> links) {
    writeLinksToFile(getPlannedFilePath(), links.planned());
    writeLinksToFile(getDoneFilePath(), links.done());

    log.info("Links saved in {}", GoogleApiUtilities.getCacheDirectoryPath());
  }

  public static String getPlannedFilePath() {
    return GoogleApiUtilities.getCacheDirectoryPath() + "/planifies.txt";
  }

  public static String getDoneFilePath() {
    return GoogleApiUtilities.getCacheDirectoryPath() + "/realises.txt";
  }

  public String loadPlannedLinks() {
    return loadLinksFromFile(getPlannedFilePath());
  }

  public String loadDoneLinks() {
    return loadLinksFromFile(getDoneFilePath());
  }

  private String loadLinksFromFile(String filePath) {
    var path = Path.of(filePath);
    try {
      if (Files.exists(path)) {
        return Files.readString(path);
      }
    } catch (IOException e) {
      log.error("Error reading links from {}", filePath, e);
    }
    return "";
  }

  private static void writeLinksToFile(String filePath, List<GoogleLinkList.NamedLink> namedLinks) {
    try {
      var lines = namedLinks.stream().map(link -> link.name() + ": " + link.link()).toList();
      Files.write(Path.of(filePath), lines);
    } catch (IOException e) {
      throw new RuntimeException("Error writing links to file: " + filePath, e);
    }
  }
}
