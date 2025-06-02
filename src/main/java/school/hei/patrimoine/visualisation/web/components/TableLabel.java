package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Span;

public class TableLabel extends Span {
  public TableLabel(String text) {
    add(new Text(text));
    addClassName("table-label");
  }
}
