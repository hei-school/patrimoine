package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import java.util.List;

public record ExtractedData<T>(String variableLink, List<T> linkDataList) {}
