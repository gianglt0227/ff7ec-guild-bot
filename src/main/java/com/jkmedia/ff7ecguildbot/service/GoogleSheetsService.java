package com.jkmedia.ff7ecguildbot.service;

import java.io.IOException;

public interface GoogleSheetsService {

  void updateMockBattle(String username, int stage, double percentage) throws IOException;

  void insertMockBattleHistory(String username, int stage, double percentage) throws IOException;

  void updateRealBattle(String username, int stage, double percentage, Integer attemptLeft)
      throws IOException;

  void insertRealBattleHistory(String username, int stage, double percentage, Integer attemptLeft)
      throws IOException;

  void changeSpreadsheet(String newSpreadsheetId);

  void recommendStageAssignment();
}
