package com.jkmedia.ff7ecguildbot.slashcommand;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockHandlerImpl implements SlashCommandHandler {
  private final GoogleSheetsService googleSheetsService;

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.MOCK;
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event) throws CommandHandlingException {
    int stage = 0;
    try {
      stage = event.getOption("stage").getAsInt();
    } catch (NumberFormatException e) {
      throw new CommandHandlingException("Invalid stage. Stage must be an integer from 1 to 5", e);
    }
    if (stage < 1 || stage > 5) {
      throw new CommandHandlingException("Invalid stage. Stage must be an integer from 1 to 5");
    }

    double percentage = 0;
    try {
      percentage = event.getOption("percentage").getAsDouble();
    } catch (NumberFormatException e) {
      throw new CommandHandlingException("Invalid percentage, must be an number from 0 to 100", e);
    }
    if (percentage < 0 || percentage > 100) {
      throw new CommandHandlingException("Invalid percentage, must be an number from 0 to 100");
    }

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
