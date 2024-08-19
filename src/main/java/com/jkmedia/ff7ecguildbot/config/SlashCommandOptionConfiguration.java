package com.jkmedia.ff7ecguildbot.config;

import com.jkmedia.ff7ecguildbot.slashcommand.Option;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlashCommandOptionConfiguration {

  @Bean("stageOption")
  public OptionData stageOption() {
    OptionData stageOption =
        new OptionData(
            OptionType.INTEGER, Option.STAGE.getValue(), "Which stage did you try (1 - 5)?", true);
    stageOption.setMinValue(1).setMaxValue(5);
    return stageOption;
  }

  @Bean("percentageOption")
  public OptionData percentageOption() {
    OptionData percentageOption =
        new OptionData(
            OptionType.NUMBER,
            Option.PERCENT_HP_REDUCED.getValue(),
            "%HP reduced on a SINGLE attempt. 2 decimal places only. Eg: 12.26",
            true);
    percentageOption.setMinValue(0).setMaxValue(100);
    return percentageOption;
  }

  @Bean("usernameOption")
  public OptionData usernameOption() {
    return new OptionData(
        OptionType.USER,
        Option.USERNAME.getValue(),
        "Discord display name of the user that you want to report for",
        true);
  }

  @Bean("attemptLeftOption")
  public OptionData attemptLeftOption() {
    OptionData attemptLeftOption =
        new OptionData(
            OptionType.INTEGER,
            Option.ATTEMPT_LEFT.getValue(),
            "How many attempt do you have left?",
            true);
    attemptLeftOption.setMinValue(0).setMaxValue(3);
    return attemptLeftOption;
  }
}
