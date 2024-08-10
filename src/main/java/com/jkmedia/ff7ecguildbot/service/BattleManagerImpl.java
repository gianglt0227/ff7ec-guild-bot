package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.object.MockBattleReport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.SortedSet;

@Service
public class BattleManagerImpl implements BattleManager {
  @Override
  public void recommendStage(SortedSet<MockBattleReport> mockBattleReports) {
    throw new UnsupportedOperationException();
  }
}
