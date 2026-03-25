package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class PatriLangUndoActions {
  public static void register(JComponent component, UndoManager undoManager) {
    component.getInputMap().put(KeyStroke.getKeyStroke("ctrl Z"), "undo");
    component
        .getActionMap()
        .put(
            "undo",
            new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                  try {
                    undoManager.undo();
                  } catch (CannotUndoException ex) {
                    undoManager.discardAllEdits();
                  }
                }
              }
            });

    component.getInputMap().put(KeyStroke.getKeyStroke("ctrl Y"), "redo");
    component
        .getActionMap()
        .put(
            "redo",
            new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                  try {
                    undoManager.redo();
                  } catch (CannotRedoException ex) {
                    undoManager.discardAllEdits();
                  }
                }
              }
            });
  }
}
