package com.jkmedia.ff7ecguildbot.slashcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommandHandler {
  SlashCommand supportedCommand();

  void handleEvent(SlashCommandInteractionEvent event);
}
