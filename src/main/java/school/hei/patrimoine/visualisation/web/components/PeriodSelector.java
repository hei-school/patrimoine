package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import school.hei.patrimoine.visualisation.web.states.PatrimoinesState;

public class PeriodSelector extends HorizontalLayout {
  public PeriodSelector(PatrimoinesState patrimoinesState) {
    var startDateSelector = new DatePicker("Debut projection");
    var endDateSelector = new DatePicker("Fin projection");
    startDateSelector.setWidthFull();
    endDateSelector.setWidthFull();
    startDateSelector.addValueChangeListener(event -> {
      endDateSelector.setMin(event.getValue());
    });
    setWidthFull();
    startDateSelector.addValueChangeListener(e -> {patrimoinesState.setEvolutionStart(e.getValue());});
    endDateSelector.addValueChangeListener(e -> {patrimoinesState.setEvolutionEnd(e.getValue());});
    add(startDateSelector, endDateSelector);
  }
}
