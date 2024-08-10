package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.object.MockBattleReport;
import java.util.SortedSet;
import org.springframework.stereotype.Service;

@Service
public class BattleManagerImpl implements BattleManager {
  @Override
  public void recommendStage(SortedSet<MockBattleReport> mockBattleReports) {
    throw new UnsupportedOperationException();
  }
}
