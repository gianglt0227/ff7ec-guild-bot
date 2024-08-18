package com.jkmedia.ff7ecguildbot.slashcommand.handler;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import com.jkmedia.ff7ecguildbot.slashcommand.CommandHandlingException;
import com.jkmedia.ff7ecguildbot.slashcommand.Option;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RealBattleReportHandlerImpl implements SlashCommandHandler {
  private final GoogleSheetsService googleSheetsService;

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.REAL_BATTLE;
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
    int attemptLeft =
        Objects.requireNonNull(event.getOption(Option.ATTEMPT_LEFT.getValue())).getAsInt();

    log.debug("received command: /mock {} {} from user {}", stage, percentage, username);
    try {
      googleSheetsService.insertRealBattleHistory(username, stage, percentage, attemptLeft);
      googleSheetsService.updateAttemptLeft(username, attemptLeft);
      event.getHook().sendMessage("Updated the Mock battle sheet!").setEphemeral(true).queue();
    } catch (Exception e) {
      log.error("", e);
      event.getHook().sendMessage("Failed to update the sheet!").setEphemeral(true).queue();
    }
  }

  private static int getStage(SlashCommandInteractionEvent event) {
    return Objects.requireNonNull(event.getOption(Option.STAGE.getValue())).getAsInt();
  }

  private static double getPercentage(SlashCommandInteractionEvent event)
      throws CommandHandlingException {
    double percentage =
        Objects.requireNonNull(event.getOption(Option.PERCENT_HP_REDUCED.getValue())).getAsDouble();
    if (BigDecimal.valueOf(percentage).scale() > 2) {
      throw new CommandHandlingException(
          "Invalid percentage, must be an number from 0 to 100, and max 2 decimal places only. Eg: 22.34");
    }
    return percentage;
  }
}
