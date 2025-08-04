package school.hei.patrimoine.visualisation.swing.ihm.google.compiler;

import java.util.function.Function;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

public interface GoogleLinkListDownloader
    extends Function<GoogleLinkList<NamedID>, GoogleLinkList<NamedID>> {}
