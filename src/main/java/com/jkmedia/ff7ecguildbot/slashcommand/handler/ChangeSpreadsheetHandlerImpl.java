package com.jkmedia.ff7ecguildbot.slashcommand.handler;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeSpreadsheetHandlerImpl implements SlashCommandHandler {
  private final GoogleSheetsService googleSheetsService;

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.CHANGE_SPREADSHEET;
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event) {
    googleSheetsService.changeSpreadsheet(event.getOption("spreadsheetId").getAsString());
  }
}
