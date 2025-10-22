package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;

public class CasSetSetter {
  private final List<State.StateObserverCallbackRunnable> observers;

  private static final CasSetSetter INSTANCE = new CasSetSetter();

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
    var plannedCasSet = transpileToutCas(FileSideBar.getPlannedCasSetFile().getAbsolutePath());
    var doneCasSet = transpileToutCas(FileSideBar.getDoneCasSetFile().getAbsolutePath());

    globalState()
        .update(
            Map.of(
                "plannedCasSet", plannedCasSet,
                "doneCasSet", doneCasSet));

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

  public State globalState() {
    return AppContext.getDefault().globalState();
  }

  public void addObserver(State.StateObserverCallbackRunnable observer) {
    observers.add(observer);
  }

  public CasSet plannedCasSet() {
    return globalState().get("plannedCasSet");
  }

  public CasSet doneCasSet() {
    return globalState().get("doneCasSet");
  }
}
