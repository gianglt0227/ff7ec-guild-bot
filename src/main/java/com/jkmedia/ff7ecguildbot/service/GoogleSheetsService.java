package com.jkmedia.ff7ecguildbot.service;

import java.io.IOException;

public interface GoogleSheetsService {
  void updateMockBattle(String username, String stage, String percentage) throws IOException;
}
