package com.jkmedia.ff7ecguildbot.slashcommand;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Option {
  STAGE("stage"),
  PERCENT_HP_REDUCED("total-hp-percent-reduced"),
  ATTEMP_USED("attempt-used"),
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
