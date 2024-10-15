package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import java.util.List;

public record ExtractedPatrimoine<T>(String possessionLink, List<T> patrimoineLinkList) {}
