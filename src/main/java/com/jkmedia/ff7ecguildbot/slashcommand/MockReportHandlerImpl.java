package com.jkmedia.ff7ecguildbot.slashcommand;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockReportHandlerImpl implements SlashCommandHandler {
  private final GoogleSheetsService googleSheetsService;

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.MOCK;
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event) throws CommandHandlingException {
    String username = event.getUser().getEffectiveName();
    doHandle(username, event);
  }

  protected void doHandle(String username, SlashCommandInteractionEvent event)
      throws CommandHandlingException {
    int stage = getStage(event);
    double percentage = getPercentage(event);

    log.debug("received command: /mock {} {} from user {}", stage, percentage, username);
    try {
      googleSheetsService.updateMockBattle(username, stage, percentage);
      event.reply("Updated the Mock battle sheet!").queue();
    } catch (Exception e) {
      log.error("", e);
      event.reply("Failed to update the sheet!").queue();
    }
  }

  private static int getStage(SlashCommandInteractionEvent event) throws CommandHandlingException {
    int stage;
    String stageError = "Invalid stage. Stage must be an integer from 1 to 5";
    try {
      stage = event.getOption(Option.STAGE.getValue()).getAsInt();
    } catch (NumberFormatException e) {
      throw new CommandHandlingException(stageError, e);
    }
    if (stage < 1 || stage > 5) {
      throw new CommandHandlingException(stageError);
    }
    return stage;
  }

  private static double getPercentage(SlashCommandInteractionEvent event) throws CommandHandlingException {
    double percentage;
    String percentageError =
        "Invalid percentage, must be an number from 0 to 100, and max 2 decimal places only. Eg: 22.34";
    try {
      percentage = event.getOption(Option.PERCENTAGE.getValue()).getAsDouble();
    } catch (NumberFormatException e) {
      throw new CommandHandlingException(percentageError, e);
    }
    if (percentage < 0 || percentage > 100) {
      throw new CommandHandlingException(percentageError);
    }
    if (BigDecimal.valueOf(percentage).scale() > 2) {
      throw new CommandHandlingException(percentageError);
    }
    return percentage;
  }
}
