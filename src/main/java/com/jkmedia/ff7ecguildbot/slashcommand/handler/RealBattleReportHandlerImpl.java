package com.jkmedia.ff7ecguildbot.slashcommand.handler;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import com.jkmedia.ff7ecguildbot.slashcommand.BattleType;
import com.jkmedia.ff7ecguildbot.slashcommand.CommandHandlingException;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RealBattleReportHandlerImpl extends AbstractBattleReportHandler {

  protected RealBattleReportHandlerImpl(
      GoogleSheetsService googleSheetsService, OptionData stageOption, OptionData percentageOption) {
    super(googleSheetsService, stageOption, percentageOption);
  }

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.REAL_BATTLE;
  }

  @Override
  public String description() {
    return "Submit real battle result";
  }

  @Override
  public List<OptionData> options() {
    return List.of(stageOption, percentageOption);
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event) throws CommandHandlingException {
    String username = event.getUser().getEffectiveName();
    doHandle(BattleType.REAL, username, event);
  }
}
