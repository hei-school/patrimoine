package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class PeriodSelector extends HorizontalLayout {
  public PeriodSelector() {
    var startDateSelector = new DatePicker("Debut projection");
    var endDateSelector = new DatePicker("Fin projection");
    startDateSelector.setWidthFull();
    endDateSelector.setWidthFull();
    startDateSelector.addValueChangeListener(event -> {
      endDateSelector.setMin(event.getValue());
    });
    setWidthFull();
    add(startDateSelector, endDateSelector);
  }
}
