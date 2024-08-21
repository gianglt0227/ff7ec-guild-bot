package com.jkmedia.ff7ecguildbot.service;

import com.google.common.collect.*;
import com.jkmedia.ff7ecguildbot.object.MockBattleReport;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class BattleManagerImpl implements BattleManager {
  @Override
  public void recommendStage(List<MockBattleReport> mockBattleReports) {
    final double MAX_PERCENT = 120d;
    Multimap<Integer, MockBattleReport> recommendStageMap = ArrayListMultimap.create();

    for (int currentStage = 5; currentStage > 0; currentStage--) {
      int finalCurrentStage = currentStage;
      List<MockBattleReport> sortedReports = mockBattleReports.stream()
          .filter(mockBattleReport -> Objects.equals(mockBattleReport.getStage(), finalCurrentStage))
          .sorted(Comparator.comparing(MockBattleReport::getPercentage)
              .reversed())
          .toList();

      Double currentStagePercent = 0d;
      for (MockBattleReport report : sortedReports) {
        recommendStageMap.put(currentStage, report);
        currentStagePercent += report.getPercentage();
        if (currentStagePercent >= MAX_PERCENT) {
          break;
        }
      }
    }
  }
}
