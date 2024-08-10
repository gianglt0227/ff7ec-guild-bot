package com.jkmedia.ff7ecguildbot.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.jkmedia.ff7ecguildbot.GoogleSheetUtil;
import java.io.*;
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

  @Override
  public void updateMockBattle(String username, int stage, double percentage) throws IOException {
    Integer userRowNum = searchUser(username);
    updateCell("A" + userRowNum, username);
    String stageRange = GoogleSheetUtil.columnNumberToLetter(stage + 1) + userRowNum;
    updateCell(stageRange, percentage);
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

  private void updateCell(String range, Object value) throws IOException {
    sheetsService
        .spreadsheets()
        .values()
        .update(
            spreadsheetId,
            range,
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
