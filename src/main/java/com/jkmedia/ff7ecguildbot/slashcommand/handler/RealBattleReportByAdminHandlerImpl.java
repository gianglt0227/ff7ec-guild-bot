package com.jkmedia.ff7ecguildbot.slashcommand.handler;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import com.jkmedia.ff7ecguildbot.slashcommand.BattleType;
import com.jkmedia.ff7ecguildbot.slashcommand.CommandHandlingException;
import com.jkmedia.ff7ecguildbot.slashcommand.Option;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RealBattleReportByAdminHandlerImpl extends AbstractBattleReportHandler {
  private final OptionData usernameOption;

  public RealBattleReportByAdminHandlerImpl(
      GoogleSheetsService googleSheetsService,
      OptionData stageOption,
      OptionData percentageOption,
      OptionData usernameOption) {
    super(googleSheetsService, stageOption, percentageOption);
    this.usernameOption = usernameOption;
  }

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.ADMIN_REAL_BATTLE;
  }

  @Override
  public List<OptionData> options() {
    return List.of(usernameOption, stageOption, percentageOption);
  }

  @Override
  public List<Permission> permissions() {
    return List.of(Permission.MANAGE_CHANNEL, Permission.MODERATE_MEMBERS);
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event) throws CommandHandlingException {
    String username =
        Objects.requireNonNull(event.getOption(Option.USERNAME.getValue())).getAsString();
    doHandle(BattleType.REAL, username, event);
  }

  @Override
  public String description() {
    return "Submit mock battle result for other people (admin permission required)";
  }
}
