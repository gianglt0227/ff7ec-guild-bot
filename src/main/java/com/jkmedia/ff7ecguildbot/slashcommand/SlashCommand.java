package com.jkmedia.ff7ecguildbot.slashcommand;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlashCommand {
  MOCK("mock"),
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
