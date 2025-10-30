package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;
import javax.swing.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import school.hei.patrimoine.visualisation.swing.ihm.selecteur.jdatepicker.DateFormatter;

public class DatePicker extends JDatePickerImpl {
  public DatePicker(LocalDate parDefaut) {
    super(getDefaultDatePanel(parDefaut), new DateFormatter());

    if (getComponent(0) instanceof JFormattedTextField textField) {
      textField.setColumns(10);
      textField.setBackground(Color.WHITE);
      textField.setFont(new Font("Arial", Font.PLAIN, 16));
      textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 35));
    }

    if (getComponent(1) instanceof JButton button) {
      button.setFont(new Font("Arial", Font.BOLD, 18));
      button.setPreferredSize(new java.awt.Dimension(40, 40));
    }

    setBackground(Color.WHITE);
  }

  public Instant toInstant() {
    var date = getDate();
    if (date == null) return null;

    return getDate().atStartOfDay(ZoneId.of("Indian/Antananarivo")).toInstant();
  }

  private LocalDate getDate() {
    var model = this.getModel();
    if (model.getValue() == null) return null;

    return LocalDate.of(model.getYear(), model.getMonth() + 1, model.getDay());
  }

  private static Properties getDefaultProperties() {
    var i18n = new Properties();
    i18n.put("text.today", "ce jour");

    return i18n;
  }

  private static JDatePanelImpl getDefaultDatePanel(LocalDate parDefaut) {
    return new JDatePanelImpl(
        new UtilDateModel(Date.from(parDefaut.atStartOfDay(ZoneId.systemDefault()).toInstant())),
        getDefaultProperties());
  }
}
