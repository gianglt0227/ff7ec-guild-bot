package com.jkmedia.ff7ecguildbot.slashcommand.handler;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import com.jkmedia.ff7ecguildbot.slashcommand.BattleType;
import com.jkmedia.ff7ecguildbot.slashcommand.CommandHandlingException;
import com.jkmedia.ff7ecguildbot.slashcommand.Option;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Slf4j
public abstract class AbstractBattleReportHandler implements SlashCommandHandler {
  protected final GoogleSheetsService googleSheetsService;
  protected final OptionData stageOption;
  protected final OptionData percentageOption;

  protected AbstractBattleReportHandler(
      GoogleSheetsService googleSheetsService,
      OptionData stageOption,
      OptionData percentageOption) {
    this.googleSheetsService = googleSheetsService;
    this.stageOption = stageOption;
    this.percentageOption = percentageOption;
  }

  protected void doHandle(
      BattleType battleType, String username, SlashCommandInteractionEvent event)
      throws CommandHandlingException {
    int stage = getStage(event);
    double percentage = getPercentage(event);

    log.debug(
        "received command: {} {} {} from user {}",
        event.getCommandString(),
        stage,
        percentage,
        username);
    try {
      googleSheetsService.updateBattle(battleType, username, stage, percentage);
      googleSheetsService.insertBattleHistory(battleType, username, stage, percentage);
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
    return percentage / 100;
  }
}
