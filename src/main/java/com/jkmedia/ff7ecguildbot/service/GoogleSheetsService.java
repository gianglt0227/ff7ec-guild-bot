package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.slashcommand.BattleType;
import java.io.IOException;

public interface GoogleSheetsService {

  void updateBattle(Long channelId, BattleType battleType, String username, int stage, double percentage)
      throws IOException;

  void insertBattleHistory(Long channelId, BattleType battleType, String username, int stage, double percentage)
      throws IOException;

  void updateAttemptLeft(Long channelId, String username, int attemptLeft) throws IOException;

  void restartAttempts(Long channelId) throws IOException;
}
