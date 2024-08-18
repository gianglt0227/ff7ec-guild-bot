package com.jkmedia.ff7ecguildbot.slashcommand.handler;

import com.jkmedia.ff7ecguildbot.slashcommand.CommandHandlingException;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommandHandler {
  SlashCommand supportedCommand();

  void handleEvent(SlashCommandInteractionEvent event) throws CommandHandlingException;
}
