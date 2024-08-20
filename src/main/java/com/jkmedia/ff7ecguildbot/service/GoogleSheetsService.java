package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.slashcommand.BattleType;
import java.io.IOException;

public interface GoogleSheetsService {

  void updateBattle(BattleType battleType, String username, int stage, double percentage)
      throws IOException;

  void insertBattleHistory(BattleType battleType, String username, int stage, double percentage)
      throws IOException;

  void changeSpreadsheet(String newSpreadsheetId);

  void updateAttemptLeft(String username, int attemptLeft) throws IOException;

  void recommendStageAssignment();
}
