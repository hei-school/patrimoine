package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import java.awt.*;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.CustomBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class FontSizeControllerButton extends JPanel {
  public FontSizeControllerButton(State state) {
    super(new FlowLayout(FlowLayout.RIGHT));

    var increaseFontButton = new Button("+");
    var decreaseFontButton = new Button("-");
    var fontSizeField = fontSizeTextField(state);

    increaseFontButton.addActionListener(e -> adjustControlledFontSize(state, 1, fontSizeField));
    decreaseFontButton.addActionListener(e -> adjustControlledFontSize(state, -1, fontSizeField));

    add(decreaseFontButton);
    add(fontSizeField);
    add(increaseFontButton);
  }

  private static JTextField fontSizeTextField(State state) {
    var fontSizeField = new JTextField(String.valueOf((int) state.get("fontSize")), 3);
    fontSizeField.setBorder(
        CustomBorder.builder()
            .padding(8, 8, 8, 8)
            .borderColor(Color.LIGHT_GRAY)
            .thickness(1)
            .radius(8)
            .build());

    fontSizeField.addActionListener(
        e -> {
          try {
            var newFontSize = Integer.parseInt(fontSizeField.getText());
            state.update("fontSize", Math.max(8, newFontSize));
          } catch (NumberFormatException ex) {
            state.update("fontSize", 14);
          }

          fontSizeField.setText(String.valueOf((int) state.get("fontSize")));
        });

    return fontSizeField;
  }

  private static void adjustControlledFontSize(State state, int delta, JTextField fontSizeField) {
    state.update("fontSize", Math.max(8, (int) state.get("fontSize") + delta));
    fontSizeField.setText(String.valueOf((int) state.get("fontSize")));
  }
}
