package school.hei.patrimoine.visualisation.swing.ihm.google.downloader;

import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

public interface NamedIdDownloader {
  GoogleLinkList<NamedID> apply(GoogleLinkList<NamedID> ids) throws GoogleIntegrationException;
}
