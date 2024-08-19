package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.object.MockBattleReport;
import java.util.List;

public interface BattleManager {
  void recommendStage(List<MockBattleReport> reports);
}
