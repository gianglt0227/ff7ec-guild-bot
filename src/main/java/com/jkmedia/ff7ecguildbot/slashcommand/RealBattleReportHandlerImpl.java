package com.jkmedia.ff7ecguildbot.slashcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class RealBattleReportHandlerImpl implements SlashCommandHandler {
  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.REAL_BATTLE;
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event) throws CommandHandlingException {}
}
