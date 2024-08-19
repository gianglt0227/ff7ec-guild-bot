package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.slashcommand.handler.SlashCommandHandler;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscordBotService {
  private final DiscordBotListener discordBotListener;
  private String discordToken;
  private final List<SlashCommandHandler> slashCommandHandlers;

  @PostConstruct
  public void init() {
    try {
      JDA jda = JDABuilder.createDefault(discordToken).build();
      jda.addEventListener(discordBotListener);

      for (SlashCommandHandler slashCommandHandler : slashCommandHandlers) {
        SlashCommandData commandData =
            Commands.slash(
                slashCommandHandler.supportedCommand().getValue(),
                slashCommandHandler.description());
        if (CollectionUtils.isNotEmpty(slashCommandHandler.options())) {
          commandData.addOptions(slashCommandHandler.options());
        }

        if (CollectionUtils.isNotEmpty(slashCommandHandler.permissions())) {
          commandData.setDefaultPermissions(
              DefaultMemberPermissions.enabledFor(slashCommandHandler.permissions()));
        }

        jda.upsertCommand(commandData).queue();
      }
    } catch (Exception e) {
      log.error("", e);
    }
  }

  @Value("${discord.token}")
  public void setDiscordToken(String discordToken) {
    this.discordToken = discordToken;
  }
}
