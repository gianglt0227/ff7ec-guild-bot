package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.slashcommand.Option;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
import com.jkmedia.ff7ecguildbot.slashcommand.handler.SlashCommandHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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
      //    String token = new
      JDA jda = JDABuilder.createDefault(discordToken).build();
      jda.addEventListener(discordBotListener);

      for (SlashCommandHandler slashCommandHandler : slashCommandHandlers) {
        jda.upsertCommand(
                slashCommandHandler.supportedCommand().getValue(),
                slashCommandHandler.description())
            .addOptions(slashCommandHandler.options())
            .queue();
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
