package com.jkmedia.ff7ecguildbot.slashcommand.handler;

import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public interface SlashCommandHandler {
  SlashCommand supportedCommand();

  String description();

  List<OptionData> options();

  default List<Permission> permissions() {
    return List.of();
  }

  void handleEvent(SlashCommandInteractionEvent event, Long channelId);
}
