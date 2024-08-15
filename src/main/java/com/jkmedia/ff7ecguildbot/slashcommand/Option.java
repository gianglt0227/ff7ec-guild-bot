package com.jkmedia.ff7ecguildbot.slashcommand;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Option {
  STAGE("stage"),
  PERCENTAGE("percentage"),
  USERNAME("username"),
  UNKNOWN("Unknown");

  private final String value;

  public static Option getEnum(String value) {
    for (Option enumItem : Option.values()) {
      if (Objects.equals(enumItem.value, value)) {
        return enumItem;
      }
    }
    return UNKNOWN;
  }
}
