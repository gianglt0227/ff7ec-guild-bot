package com.jkmedia.ff7ecguildbot.slashcommand.handler;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import com.jkmedia.ff7ecguildbot.slashcommand.CommandHandlingException;
import com.jkmedia.ff7ecguildbot.slashcommand.Option;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;

import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockReportByAdminHandlerImpl extends MockReportHandlerImpl {
  private final OptionData usernameOption;

  public MockReportByAdminHandlerImpl(
      GoogleSheetsService googleSheetsService,
      OptionData stageOption,
      OptionData percentageOption,
      OptionData usernameOption) {
    super(googleSheetsService, stageOption, percentageOption);
    this.usernameOption = usernameOption;
  }

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.ADMIN_MOCK;
  }

  @Override
  public List<OptionData> options() {
    return List.of(stageOption, percentageOption, usernameOption);
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event) throws CommandHandlingException {
    String username =
        Objects.requireNonNull(event.getOption(Option.USERNAME.getValue())).getAsString();
    doHandle(username, event);
  }

  @Override
  public String description() {
    return "Submit mock battle result for other people (admin permission required)";
  }
}
