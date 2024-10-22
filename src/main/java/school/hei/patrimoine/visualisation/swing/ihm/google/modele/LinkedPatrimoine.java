package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import java.util.List;

public record LinkedPatrimoine<T>(String possessionLink, List<T> patrimoineLinkList) {}
