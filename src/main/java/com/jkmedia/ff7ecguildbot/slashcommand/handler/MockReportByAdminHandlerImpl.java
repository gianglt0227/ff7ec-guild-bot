package com.jkmedia.ff7ecguildbot.slashcommand.handler;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import com.jkmedia.ff7ecguildbot.slashcommand.CommandHandlingException;
import com.jkmedia.ff7ecguildbot.slashcommand.Option;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockReportByAdminHandlerImpl extends MockReportHandlerImpl {

  public MockReportByAdminHandlerImpl(GoogleSheetsService googleSheetsService) {
    super(googleSheetsService);
  }

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.ADMIN_MOCK;
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event) throws CommandHandlingException {
    String username = event.getOption(Option.USERNAME.getValue()).getAsString();
    doHandle(username, event);
  }
}
