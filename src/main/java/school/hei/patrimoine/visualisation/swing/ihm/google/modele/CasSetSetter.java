package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getDoneCasSetFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getPlannedCasSetFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.recouppement.model.CompteGetter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;

public class CasSetSetter {
  private static final CasSetSetter INSTANCE = new CasSetSetter();

  private final List<State.StateObserverCallbackRunnable> observers;

  public static CasSetSetter getInstance() {
    return INSTANCE;
  }

  private CasSetSetter() {
    this.observers = new ArrayList<>();
    globalState().subscribe("isAnyFileModified", this::updatedCasSet);
  }

  public void updatedCasSet() {
    if (!isAnyFileModified()) {
      return;
    }
    var doneCasSet = transpileToutCas(getDoneCasSetFile().getAbsolutePath());
    var plannedCasSet = transpileToutCas(getPlannedCasSetFile().getAbsolutePath());

    globalState()
        .update(
            Map.of(
                "casSetComptes",
                CompteGetter.getComptes(doneCasSet),
                "plannedCasSet",
                plannedCasSet,
                "doneCasSet",
                doneCasSet));

    this.observers.forEach(State.StateObserverCallbackRunnable::run);

    globalState().update("isAnyFileModified", false);
  }

  public Cas getCas(File file, CasSet casSet) {
    var fileName = file.getName();
    var baseName = fileName.contains(".") ? fileName.substring(0, fileName.indexOf('.')) : fileName;

    return casSet.set().stream()
        .filter(cas -> cas.patrimoine().nom().equals(baseName))
        .findFirst()
        .orElseThrow();
  }

  public boolean isAnyFileModified() {
    return globalState().get("isAnyFileModified") == null
        || (boolean) globalState().get("isAnyFileModified");
  }

  public CasSet doneCasSet() {
    return globalState().get("doneCasSet");
  }

  public CasSet plannedCasSet() {
    return globalState().get("plannedCasSet");
  }

  public State globalState() {
    return AppContext.getDefault().globalState();
  }

  public void addObserver(State.StateObserverCallbackRunnable observer) {
    observers.add(observer);
  }
}
