package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.object.Guild;
import com.jkmedia.ff7ecguildbot.slashcommand.CommandHandlingException;
import com.jkmedia.ff7ecguildbot.slashcommand.handler.SlashCommandHandler;
import com.jkmedia.ff7ecguildbot.slashcommand.handler.SlashCommandHandlerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscordBotListener extends ListenerAdapter {
  private final SlashCommandHandlerFactory slashCommandHandlerFactory;
  private final GuildManagerService guildManagerService;

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    try {
      event.deferReply(true).queue();
      SlashCommandHandler slashCommandHandler =
          slashCommandHandlerFactory.getSlashCommandHandler(event.getName());
      slashCommandHandler.handleEvent(event, getChannelId(event));

    } catch (CommandHandlingException e) {
      event.getHook().sendMessage(e.getReplyMessage()).setEphemeral(true).queue();
      log.error(e.getReplyMessage(), e);

    } catch (Exception e) {
      log.error("Error while trying to handle slash command {}", event.getName(), e);
      event.getHook()
          .sendMessage("Error while trying to handle slash command! Please report to guild admin")
          .setEphemeral(true)
          .queue();
    }
  }

  private Long getChannelId(SlashCommandInteractionEvent event) {
    long channelId = event.getChannel().getIdLong();
    Guild guild = guildManagerService.findByChannelId(channelId);
    if (guild != null) {
      return channelId;
    } else {
      throw new CommandHandlingException("The bot is not available for this guild");
    }
  }
}
