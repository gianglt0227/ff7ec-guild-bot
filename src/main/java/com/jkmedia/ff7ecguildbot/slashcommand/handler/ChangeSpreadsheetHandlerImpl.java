package com.jkmedia.ff7ecguildbot.slashcommand.handler;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Slf4j
@RequiredArgsConstructor
public class ChangeSpreadsheetHandlerImpl implements SlashCommandHandler {
  private final GoogleSheetsService googleSheetsService;

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.CHANGE_SPREADSHEET;
  }

  @Override
  public String description() {
    return "Change the spreadsheetId";
  }

  @Override
  public List<OptionData> options() {
    return List.of();
  }

  @Override
  public List<Permission> permissions() {
    return List.of(Permission.MANAGE_CHANNEL, Permission.MODERATE_MEMBERS);
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event) {
    googleSheetsService.changeSpreadsheet(
        Objects.requireNonNull(event.getOption("spreadsheetId")).getAsString());
  }
}
