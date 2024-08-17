package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.slashcommand.Option;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscordBotService {
  private final DiscordBotListener discordBotListener;
  private String discordToken;

  @PostConstruct
  public void init() {
    try {
      //    String token = new
      JDA jda = JDABuilder.createDefault(discordToken).build();
      jda.addEventListener(discordBotListener);

      OptionData stageOption =
          new OptionData(
              OptionType.INTEGER,
              Option.STAGE.getValue(),
              "Stage of the mock battle (1 - 5)",
              true);
      stageOption.setMinValue(1).setMaxValue(5);

      OptionData percentageOption =
          new OptionData(
              OptionType.NUMBER,
              Option.PERCENT_HP_REDUCED.getValue(),
              "Percentage of boss HP reduced. 2 decimal places only. Eg: 12.26",
              true);
      percentageOption.setMinValue(0).setMaxValue(100);

      OptionData usernameOption =
          new OptionData(
              OptionType.USER,
              Option.USERNAME.getValue(),
              "Discord display name of the user that you want to report for",
              true);

      OptionData attemptUsedOption =
          new OptionData(
              OptionType.INTEGER,
              Option.ATTEMPT_LEFT.getValue(),
              "How many attempt did you used?",
              true);
      attemptUsedOption.setMinValue(1).setMaxValue(3);

      jda.upsertCommand(SlashCommand.MOCK.getValue(), "Submit mock battle result")
          .addOptions(stageOption, percentageOption)
          .queue();
      jda.upsertCommand(SlashCommand.REAL_BATTLE.getValue(), "Submit real battle result")
          .addOptions(stageOption, percentageOption, attemptUsedOption)
          .queue();

      jda.upsertCommand(
              Commands.slash(
                      SlashCommand.ADMIN_MOCK.getValue(),
                      "Submit mock battle result for other people (admin permission required)")
                  .setDefaultPermissions(
                      DefaultMemberPermissions.enabledFor(
                          Permission.MANAGE_CHANNEL, Permission.MODERATE_MEMBERS))
                  .addOptions(usernameOption, stageOption, percentageOption))
          .queue();
    } catch (Exception e) {
      log.error("", e);
    }
  }

  @Value("${discord.token}")
  public void setDiscordToken(String discordToken) {
    this.discordToken = discordToken;
  }
}
