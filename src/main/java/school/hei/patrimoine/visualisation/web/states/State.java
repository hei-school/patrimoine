package school.hei.patrimoine.visualisation.web.states;

import java.util.Observable;

sealed class State extends Observable permits PatrimoinesState {
  protected void change() {
    setChanged();
    notifyObservers();
  }
}
