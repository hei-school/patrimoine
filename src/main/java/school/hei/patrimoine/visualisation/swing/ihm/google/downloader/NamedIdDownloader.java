package school.hei.patrimoine.visualisation.swing.ihm.google.downloader;

import java.util.List;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

public interface NamedIdDownloader {
  List<NamedID> apply(List<NamedID> ids) throws GoogleIntegrationException;
}
