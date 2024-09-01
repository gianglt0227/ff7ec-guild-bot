package com.jkmedia.ff7ecguildbot.service;

import com.google.api.services.sheets.v4.model.SortSpec;
import com.jkmedia.ff7ecguildbot.slashcommand.BattleType;
import java.io.IOException;

public interface GoogleSheetsService {

  void updateBattle(String spreadsheetId, BattleType battleType, String username, int stage, double percentage)
      throws IOException;

  void insertBattleHistory(String spreadsheetId, BattleType battleType, String username, int stage, double percentage)
      throws IOException;

  void updateAttemptLeft(String spreadsheetId, String username, int attemptLeft) throws IOException;

  void sort(
      String spreadsheetId,
      Integer sheetId,
      Integer startColumnIndex,
      Integer startRowIndex,
      Integer endColumnIndex,
      Integer endRowIndex,
      SortSpec... sortSpecs)
      throws IOException;

  void restartAttempts(String spreadsheetId) throws IOException;
}
