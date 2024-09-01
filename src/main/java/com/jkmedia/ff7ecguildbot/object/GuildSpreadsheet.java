package com.jkmedia.ff7ecguildbot.object;

import lombok.Data;

@Data
public class GuildSpreadsheet {
  private Integer mockBattleSheetIndex;
  private Integer mockBattleHistorySheetIndex;
  private Integer realBattleSheetIndex;
  private Integer realBattleHistorySheetIndex;
  private Integer attemptLeftSheetIndex;
}
