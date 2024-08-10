package com.jkmedia.ff7ecguildbot.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscordBotService {
  private final DiscordBotListener discordBotListener;
  private String discordToken;

  @PostConstruct
  public void init() {
    //    String token = new
    JDA jda = JDABuilder.createDefault(discordToken).build();
    jda.addEventListener(discordBotListener);
    jda.upsertCommand("mock", "Submit mock battle result")
        .addOption(OptionType.STRING, "stage", "Stage of the mock battle", true)
        .addOption(OptionType.STRING, "percentage", "Percentage of completion", true)
        .queue();
  }

  @Value("${discord.token}")
  public void setDiscordToken(String discordToken) {
    this.discordToken = discordToken;
  }
}
