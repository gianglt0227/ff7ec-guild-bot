package com.jkmedia.ff7ecguildbot.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import java.io.*;
import java.util.Arrays;
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
    ValueRange body =
        new ValueRange().setValues(List.of(Arrays.asList(username, stage, percentage)));
    sheetsService
        .spreadsheets()
        .values()
        .update(spreadsheetId, "B1", body)
        .setValueInputOption("RAW")
        .execute();
  }

  public void updateOrCreateRow(String username, String stage, String percentage, String sheetName)
      throws IOException {
    // Implement method to update or insert row in the sheet
  }

  public void changeSpreadsheet(String newSpreadsheetId) {
    this.spreadsheetId = newSpreadsheetId;
  }
}
