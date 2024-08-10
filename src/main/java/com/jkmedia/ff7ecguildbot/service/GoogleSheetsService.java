package com.jkmedia.ff7ecguildbot.service;

import java.io.IOException;

public interface GoogleSheetsService {
  void updateMockBattle(String username, int stage, double percentage) throws IOException;

  void changeSpreadsheet(String newSpreadsheetId);

  void recommendStageAssignment();
}
