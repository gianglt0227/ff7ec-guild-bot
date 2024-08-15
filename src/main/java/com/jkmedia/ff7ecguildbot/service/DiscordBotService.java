package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.slashcommand.Option;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
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

    OptionData stageOption =
        new OptionData(
            OptionType.STRING, Option.STAGE.getValue(), "Stage of the mock battle (1 - 5)", true);
    OptionData percentageOption =
        new OptionData(
            OptionType.STRING,
            Option.PERCENTAGE.getValue(),
            "Percentage of boss heath removed. 2 decimal places only. Eg: 12.26",
            true);
    OptionData usernameOption =
        new OptionData(
            OptionType.STRING,
            Option.USERNAME.getValue(),
            "Discord display name of the user that you want to report for",
            true);

    jda.upsertCommand(SlashCommand.MOCK.getValue(), "Submit mock battle result")
        .addOptions(stageOption, percentageOption)
        .queue();

    jda.upsertCommand(
            SlashCommand.ADMIN_MOCK.getValue(),
            "Submit mock battle result for other people (admin permission required)")
        .addOptions(usernameOption, stageOption, percentageOption)
        .queue();
  }

  @Value("${discord.token}")
  public void setDiscordToken(String discordToken) {
    this.discordToken = discordToken;
  }
}
