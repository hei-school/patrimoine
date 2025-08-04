package school.hei.patrimoine.visualisation.swing.ihm.google.compiler;

import java.util.List;
import java.util.function.Function;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

public interface GoogleLinkListCompiler
    extends Function<GoogleLinkList<NamedID>, List<Patrimoine>> {}
