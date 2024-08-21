package com.jkmedia.ff7ecguildbot.service;

import com.google.api.services.sheets.v4.model.SortSpec;
import com.jkmedia.ff7ecguildbot.slashcommand.BattleType;
import java.io.IOException;

public interface GoogleSheetsService {

  void updateBattle(BattleType battleType, String username, int stage, double percentage)
      throws IOException;

  void insertBattleHistory(BattleType battleType, String username, int stage, double percentage)
      throws IOException;

  void changeSpreadsheet(String newSpreadsheetId);

  void updateAttemptLeft(String username, int attemptLeft) throws IOException;

  void sort(
      Integer sheetId,
      Integer startColumnIndex,
      Integer startRowIndex,
      Integer endColumnIndex,
      Integer endRowIndex,
      SortSpec... sortSpecs)
      throws IOException;

  void recommendStageAssignment();

  void restartAttempts() throws IOException;
}
