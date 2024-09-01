package com.jkmedia.ff7ecguildbot.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.google.common.util.concurrent.RateLimiter;
import com.jkmedia.ff7ecguildbot.GoogleSheetUtil;
import com.jkmedia.ff7ecguildbot.object.Guild;
import com.jkmedia.ff7ecguildbot.slashcommand.BattleType;
import com.jkmedia.ff7ecguildbot.slashcommand.CommandHandlingException;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
  private final GuildManagerService guildManagerService;
  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss");

  private static final String MOCK_BATTLE_SHEET = "Mock Battle";
  private static final String MOCK_BATTLE_HISTORY_SHEET = "Mock Battle History";
  private static final String REAL_BATTLE_SHEET = "Real Battle";
  private static final String REAL_BATTLE_HISTORY_SHEET = "Real Battle History";
  private static final String ATTEMPT_LEFT_SHEET = "Attempt Left";

  private final RateLimiter rateLimiter = RateLimiter.create(1);

  @Override
  public void updateBattle(Long channelId, BattleType battleType, String username, int stage, double percentage)
      throws IOException {
    String sheetName =
        switch (battleType) {
          case MOCK -> MOCK_BATTLE_SHEET;
          case REAL -> REAL_BATTLE_SHEET;
        };
    String spreadsheetId = getSpreadsheetId(channelId);
    Integer userRowNum = searchUser(spreadsheetId, sheetName, username);
    String stageRange = GoogleSheetUtil.columnNumberToLetter(stage + 1) + userRowNum;
    String time = dateTimeFormatter.format(LocalDateTime.now());

    updateCell(spreadsheetId, sheetName, "A" + userRowNum, username);
    updateCell(spreadsheetId, sheetName, stageRange, percentage);
    updateCell(spreadsheetId, sheetName, "G" + userRowNum, time);
  }

  @Override
  public void insertBattleHistory(
      Long channelId, BattleType battleType, String username, int stage, double percentage) throws IOException {
    String sheetName =
        switch (battleType) {
          case MOCK -> MOCK_BATTLE_HISTORY_SHEET;
          case REAL -> REAL_BATTLE_HISTORY_SHEET;
        };
    Guild guild = getGuild(channelId);
    Integer sheetIndex =
        switch (battleType) {
          case MOCK -> guild.getGuildSpreadsheet().getMockBattleHistorySheetIndex();
          case REAL -> guild.getGuildSpreadsheet().getRealBattleHistorySheetIndex();
        };

    String spreadsheetId = guild.getGoogleSpreadsheetId();
    int rowNumToInsert = findLastRowNum(spreadsheetId, sheetName) + 1;
    String time = dateTimeFormatter.format(LocalDateTime.now());

    updateCell(spreadsheetId, sheetName, "A" + rowNumToInsert, username);
    updateCell(spreadsheetId, sheetName, "B" + rowNumToInsert, stage);
    updateCell(spreadsheetId, sheetName, "C" + rowNumToInsert, percentage);
    updateCell(spreadsheetId, sheetName, "D" + rowNumToInsert, time);
    sort(
        channelId,
        sheetIndex,
        0,
        1,
        4,
        1030,
        new SortSpec().setDimensionIndex(3).setSortOrder("DESCENDING"));
  }

  @Override
  public void updateAttemptLeft(Long channelId, String username, int attemptLeft) throws IOException {
    String spreadsheetId = getSpreadsheetId(channelId);
    Integer userRowNum = searchUser(spreadsheetId, ATTEMPT_LEFT_SHEET, username);
    String time = dateTimeFormatter.format(LocalDateTime.now());

    updateCell(spreadsheetId, ATTEMPT_LEFT_SHEET, "A" + userRowNum, username);
    updateCell(spreadsheetId, ATTEMPT_LEFT_SHEET, "B" + userRowNum, attemptLeft);
    updateCell(spreadsheetId, ATTEMPT_LEFT_SHEET, "C" + userRowNum, time);
    updateCell(spreadsheetId, ATTEMPT_LEFT_SHEET, "D" + userRowNum, "");
  }

  @Override
  public void sort(
      Long channelId,
      Integer sheetId,
      Integer startColumnIndex,
      Integer startRowIndex,
      Integer endColumnIndex,
      Integer endRowIndex,
      SortSpec... sortSpecs)
      throws IOException {
    GridRange gridRange = new GridRange()
        .setSheetId(sheetId)
        .setStartColumnIndex(startColumnIndex)
        .setStartRowIndex(startRowIndex)
        .setEndColumnIndex(endColumnIndex)
        .setEndRowIndex(endRowIndex);
    SortRangeRequest sortRangeRequest =
        new SortRangeRequest().setRange(gridRange).setSortSpecs(Arrays.asList(sortSpecs));
    Request request = new Request().setSortRange(sortRangeRequest);
    BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest =
        new BatchUpdateSpreadsheetRequest().setRequests(List.of(request));

    sheetsService
        .spreadsheets()
        .batchUpdate(getSpreadsheetId(channelId), batchUpdateSpreadsheetRequest)
        .execute();
  }

  @Override
  public void restartAttempts(Long channelId) throws IOException {
    String spreadsheetId = getSpreadsheetId(channelId);
    int lastRowNum = findLastRowNum(spreadsheetId, ATTEMPT_LEFT_SHEET);
    String now = dateTimeFormatter.format(LocalDateTime.now());
    for (int i = 2; i <= lastRowNum; i++) {
      updateCell(spreadsheetId, ATTEMPT_LEFT_SHEET, "B" + i, "");
      updateCell(spreadsheetId, ATTEMPT_LEFT_SHEET, "C" + i, now);
      updateCell(spreadsheetId, ATTEMPT_LEFT_SHEET, "D" + i, "Reset by bot");
    }
  }

  private int findLastRowNum(String spreadsheetId, String sheetName) throws IOException {
    String range = getRangeOfUsernameColumn(sheetName);
    log.debug("Finding the last row in this range {} of spreadsheetId {}", range, spreadsheetId);
    ValueRange valueRange =
        sheetsService.spreadsheets().values().get(spreadsheetId, range).execute();
    int lastRow = 1;
    if (!CollectionUtils.isEmpty(valueRange.getValues())) {
      return valueRange.getValues().size();
    }
    return lastRow + 1;
  }

  private static String getRangeOfUsernameColumn(String sheetName) {
    return String.format("'%s'!%s", sheetName, "A:A");
  }

  private @NotNull Integer searchUser(String spreadsheetId, String sheetName, String username) throws IOException {
    String range = getRangeOfUsernameColumn(sheetName);
    ValueRange result =
        sheetsService.spreadsheets().values().get(spreadsheetId, range).execute();
    Integer userRowNum = null;

    if (CollectionUtils.isEmpty(result.getValues())) {
      userRowNum = 2; // default if there is no user yet
    } else {
      int currentRow = 1; // first row is a header
      for (List<Object> row : result.getValues()) {
        Object cell = row.get(0);
        if (cell != null && Objects.equals(username, cell.toString())) {
          userRowNum = currentRow;
          break;
        }
        currentRow++;
      }
      if (userRowNum == null) {
        userRowNum = currentRow;
      }
      log.info("userRowNum: {}, currentRow: {}", userRowNum, currentRow);
    }
    return userRowNum;
  }

  private void updateCell(String spreadsheetId, String sheetName, String range, Object value) throws IOException {
    rateLimiter.acquire();
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

  private String getSpreadsheetId(Long channelId) {
    Guild guild = guildManagerService.findByChannelId(channelId);
    if (guild != null) {
      return guild.getGoogleSpreadsheetId();
    } else {
      throw new CommandHandlingException("This bot is not available for this guild");
    }
  }

  private Guild getGuild(Long channelId) {
    Guild guild = guildManagerService.findByChannelId(channelId);
    if (guild != null) {
      return guild;
    } else {
      throw new CommandHandlingException("This bot is not available for this guild");
    }
  }
}
