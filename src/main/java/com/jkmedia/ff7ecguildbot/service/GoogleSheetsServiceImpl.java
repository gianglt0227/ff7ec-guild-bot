package com.jkmedia.ff7ecguildbot.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.jkmedia.ff7ecguildbot.GoogleSheetUtil;
import java.io.*;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleSheetsServiceImpl implements GoogleSheetsService {

  private final Sheets sheetsService;
  private String spreadsheetId = "1jk9QMymcE_cg4m-b4rhKkSO0upOM4_mU2ggPhksfY_o";

  @Override
  public void updateMockBattle(String username, int stage, double percentage) throws IOException {
    updateCell("A2", username);
    String range = GoogleSheetUtil.columnNumberToLetter(stage + 1) + "2";
    updateCell(range, percentage);
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
}
