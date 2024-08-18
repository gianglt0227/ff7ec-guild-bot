package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.slashcommand.CommandHandlingException;
import com.jkmedia.ff7ecguildbot.slashcommand.handler.SlashCommandHandler;
import com.jkmedia.ff7ecguildbot.slashcommand.handler.SlashCommandHandlerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscordBotListener extends ListenerAdapter {
  private final SlashCommandHandlerFactory slashCommandHandlerFactory;

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    try {
      event.deferReply(true).queue();
      SlashCommandHandler slashCommandHandler =
          slashCommandHandlerFactory.getSlashCommandHandler(event.getName());
      slashCommandHandler.handleEvent(event);

    } catch (CommandHandlingException e) {
      event.getHook().sendMessage(e.getReplyMessage()).setEphemeral(true).queue();
      log.error(e.getReplyMessage(), e);

    } catch (Exception e) {
      log.error("Error while trying to handle slash command {}", event.getName(), e);
      event
          .getHook()
          .sendMessage("Error while trying to handle slash command! Please report to guild admin")
          .setEphemeral(true)
          .queue();
    }
  }
}
