package school.hei.patrimoine.visualisation.swing.ihm.jdatepicker;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormatter extends JFormattedTextField.AbstractFormatter {

  private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

  @Override
  public Object stringToValue(String text) throws ParseException {
    return dateFormatter.parseObject(text);
  }

  @Override
  public String valueToString(Object value) {
    if (value == null) {
      return "";
    }
    Calendar cal = (Calendar) value;
    return dateFormatter.format(cal.getTime());
  }
}
