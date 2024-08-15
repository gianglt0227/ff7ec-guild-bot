package com.jkmedia.ff7ecguildbot.object;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MockBattleReportByAdmin extends MockBattleReport {
  private String targetUser;
}
