package com.jkmedia.ff7ecguildbot.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.jkmedia.ff7ecguildbot.GoogleSheetUtil;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleSheetsServiceImpl implements GoogleSheetsService {

  private final Sheets sheetsService;
  private String spreadsheetId = "1jk9QMymcE_cg4m-b4rhKkSO0upOM4_mU2ggPhksfY_o";
  private static final String MOCK_BATTLE_SHEET = "Mock Battle";
  private static final String REAL_BATTLE_SHEET = "Real Battle";
  private static final String MOCK_BATTLE_HISTORY_SHEET = "Mock Battle History";
  private static final String REAL_BATTLE_HISTORY_SHEET = "Real Battle History";
  private final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss");

  @Override
  public void updateMockBattle(String username, int stage, double percentage) throws IOException {
    Integer userRowNum = searchUser(username);
    String stageRange = GoogleSheetUtil.columnNumberToLetter(stage + 1) + userRowNum;
    String time = dateTimeFormatter.format(LocalDateTime.now());

    updateCell(MOCK_BATTLE_SHEET, "A" + userRowNum, username);
    updateCell(MOCK_BATTLE_SHEET, stageRange, percentage);
    updateCell(MOCK_BATTLE_SHEET, "G" + userRowNum, time);
  }

  @Override
  public void insertMockBattleHistory(String username, int stage, double percentage)
      throws IOException {
    int rowNumToInsert = findLastRowNum(MOCK_BATTLE_SHEET);
    String time = dateTimeFormatter.format(LocalDateTime.now());
    updateCell(MOCK_BATTLE_HISTORY_SHEET, "A" + rowNumToInsert, username);
    updateCell(MOCK_BATTLE_HISTORY_SHEET, "B" + rowNumToInsert, stage);
    updateCell(MOCK_BATTLE_HISTORY_SHEET, "C" + rowNumToInsert, percentage);
    updateCell(MOCK_BATTLE_HISTORY_SHEET, "D" + rowNumToInsert, time);
  }

  @Override
  public void updateRealBattle(String username, int stage, double percentage, Integer attemptLeft)
      throws IOException {
    Integer userRowNum = searchUser(username);
    String time = dateTimeFormatter.format(LocalDateTime.now());
    String stageRange = GoogleSheetUtil.columnNumberToLetter(stage + 1) + userRowNum;

    updateCell(REAL_BATTLE_SHEET, "A" + userRowNum, username);
    updateCell(REAL_BATTLE_SHEET, stageRange, percentage);
    updateCell(REAL_BATTLE_SHEET, "G" + userRowNum, attemptLeft);
    updateCell(REAL_BATTLE_SHEET, "H" + userRowNum, time);
  }

  @Override
  public void insertRealBattleHistory(
      String username, int stage, double percentage, Integer attemptLeft) throws IOException {
    int rowNumToInsert = findLastRowNum(REAL_BATTLE_HISTORY_SHEET);
    String time = dateTimeFormatter.format(LocalDateTime.now());
    updateCell(REAL_BATTLE_HISTORY_SHEET, "A" + rowNumToInsert, username);
    updateCell(REAL_BATTLE_HISTORY_SHEET, "B" + rowNumToInsert, stage);
    updateCell(REAL_BATTLE_HISTORY_SHEET, "C" + rowNumToInsert, percentage);
    updateCell(REAL_BATTLE_HISTORY_SHEET, "D" + rowNumToInsert, attemptLeft);
    updateCell(REAL_BATTLE_HISTORY_SHEET, "E" + rowNumToInsert, time);
  }

  private int findLastRowNum(String sheetName) throws IOException {
    ValueRange valueRange =
        sheetsService
            .spreadsheets()
            .values()
            .get(spreadsheetId, String.format("'%s'!%s", sheetName, "A:A"))
            .execute();
    int lastRow = 2;
    if (!CollectionUtils.isEmpty(valueRange.getValues())) {
      for (List<Object> rows : valueRange.getValues()) {
        if (rows.get(0) == null) {
          break;
        }
        lastRow++;
      }
    }
    return lastRow;
  }

  private @NotNull Integer searchUser(String username) throws IOException {
    ValueRange result =
        sheetsService.spreadsheets().values().get(spreadsheetId, "A2:A31").execute();
    Integer userRowNum = null;
    if (CollectionUtils.isEmpty(result.getValues())) { // no user yet
      userRowNum = 2;
    } else {
      int currentRow = 1; // first row is a header
      for (List<Object> row : result.getValues()) {
        currentRow++;
        Object cell = row.get(0);
        if (cell != null && Objects.equals(username, cell.toString())) {
          userRowNum = currentRow;
          break;
        }
      }
      log.info("userRowNum: {}, currentRow: {}", userRowNum, currentRow);

      if (userRowNum == null) { // user not found
        userRowNum = currentRow + 1;
      }
    }
    return userRowNum;
  }

  private void updateCell(String sheetName, String range, Object value) throws IOException {
    sheetsService
        .spreadsheets()
        .values()
        .update(
            spreadsheetId,
            String.format("'%s'!%s", sheetName, range),
            new ValueRange().setValues(List.of(Collections.singletonList(value))))
        .setValueInputOption("USER_ENTERED")
        .execute();
  }

  @Override
  public void changeSpreadsheet(String newSpreadsheetId) {
    this.spreadsheetId = newSpreadsheetId;
  }

  @Override
  public void recommendStageAssignment() {
    throw new UnsupportedOperationException();
  }
}
