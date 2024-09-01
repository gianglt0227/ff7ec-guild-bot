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

@Slf4j
@Component
public class MockReportHandlerImpl extends AbstractBattleReportHandler {
  public MockReportHandlerImpl(
      GoogleSheetsService googleSheetsService, OptionData stageOption, OptionData percentageOption) {
    super(googleSheetsService, stageOption, percentageOption);
  }

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.MOCK;
  }

  @Override
  public String description() {
    return "Submit mock battle result";
  }

  @Override
  public List<OptionData> options() {
    return List.of(stageOption, percentageOption);
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event, String googleSpreadsheetId)
      throws CommandHandlingException {
    String username = event.getUser().getEffectiveName();
    doHandle(BattleType.MOCK, username, event, googleSpreadsheetId);
  }
}
