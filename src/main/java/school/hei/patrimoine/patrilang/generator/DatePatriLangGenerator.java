package school.hei.patrimoine.patrilang.generator;

import static school.hei.patrimoine.patrilang.mapper.MonthMapper.monthToString;

import java.time.LocalDate;

public class DatePatriLangGenerator implements PatriLangGenerator<LocalDate> {
  @Override
  public String apply(LocalDate date) {
    return String.format(
        "le %s %s %s", date.getDayOfMonth(), monthToString(date.getMonth()), date.getYear());
  }
}
