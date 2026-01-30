package school.hei.patrimoine.modele.fec;

import lombok.Getter;

@Getter
public enum JournalCode {
  JN("Journal");

  private final String journalLib;

  JournalCode(String journalLib) {
    this.journalLib = journalLib;
  }
}
