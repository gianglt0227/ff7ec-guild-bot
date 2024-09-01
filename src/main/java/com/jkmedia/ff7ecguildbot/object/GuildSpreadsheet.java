package com.jkmedia.ff7ecguildbot.object;

import io.github.bucket4j.Bucket;
import lombok.Data;

@Data
public class GuildSpreadsheet {
  private String googleSpreadsheetId;
  private Bucket writeRateLimit;
  private Bucket readRateLimit;
  private Integer mockBattleSheetIndex;
  private Integer mockBattleHistorySheetIndex;
  private Integer realBattleSheetIndex;
  private Integer realBattleHistorySheetIndex;
  private Integer attemptLeftSheetIndex;
}
