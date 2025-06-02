package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;

public class RoundedGrid<T> extends VerticalLayout {
  private final Grid<T> grid;
  public RoundedGrid(Class<T> beanType) {
    this.grid = new Grid<>(beanType, false);
    this.grid.getStyle().set("border-radius", "10px");
    this.grid.getStyle().set("overflow", "hidden");
    getStyle().set("margin", "0");
    getStyle().set("padding", "0");
    add(grid);
  }

  public <V> Grid.Column<T> addColumn(String header, ValueProvider<T, V> dataProvider) {
    return this.grid.addColumn(dataProvider).setHeader(header);
  }
}
