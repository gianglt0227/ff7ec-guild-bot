package com.jkmedia.ff7ecguildbot.slashcommand;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockCommandHandlerImpl implements SlashCommandHandler {
  private final GoogleSheetsService googleSheetsService;

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.MOCK;
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event) {
    String stage = event.getOption("stage").getAsString();
    String percentage = event.getOption("percentage").getAsString();
    String username = event.getUser().getName();

    log.debug("received command: /mock {} {} from user {}", stage, percentage, username);
    try {
      googleSheetsService.updateMockBattle(username, stage, percentage);
      event.reply("Updated the Mock battle sheet!").queue();
    } catch (Exception e) {
      log.error("", e);
      event.reply("Failed to update the sheet!").queue();
    }
  }
}
