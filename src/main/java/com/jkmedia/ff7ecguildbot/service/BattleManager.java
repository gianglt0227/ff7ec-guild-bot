package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.object.MockBattleReport;

import java.util.List;
import java.util.SortedSet;

public interface BattleManager {
    void recommendStage(SortedSet<MockBattleReport> reports);
}
