package com.jkmedia.ff7ecguildbot.slashcommand;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlashCommand {
  ADMIN_MOCK("admin-mock"),
  ADMIN_REAL_BATTLE("admin-real-battle"),
  CHANGE_SPREADSHEET("change-spreadsheet"),
  MOCK("mock"),
  REAL_BATTLE("real-battle"),
  UNKNOWN("Unknown");

  private final String value;

  public static SlashCommand getEnum(String value) {
    for (SlashCommand enumItem : SlashCommand.values()) {
      if (Objects.equals(enumItem.value, value)) {
        return enumItem;
      }
    }
    return UNKNOWN;
  }
}
