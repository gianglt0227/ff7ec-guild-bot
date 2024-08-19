package com.jkmedia.ff7ecguildbot.slashcommand.handler;

import com.jkmedia.ff7ecguildbot.service.GoogleSheetsService;
import com.jkmedia.ff7ecguildbot.slashcommand.Option;
import com.jkmedia.ff7ecguildbot.slashcommand.SlashCommand;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttemptLeftHandlerImpl implements SlashCommandHandler {
  private final GoogleSheetsService googleSheetsService;
  private final OptionData attemptLeftOption;

  @Override
  public SlashCommand supportedCommand() {
    return SlashCommand.ATTEMPT_LEFT;
  }

  @Override
  public String description() {
    return "Report how many remaining attempts that you have";
  }

  @Override
  public List<OptionData> options() {
    return List.of(attemptLeftOption);
  }

  @Override
  public void handleEvent(SlashCommandInteractionEvent event) {
    try {
      String username = event.getUser().getEffectiveName();
      int attemptLeft =
          Objects.requireNonNull(event.getOption(Option.ATTEMPT_LEFT.getValue())).getAsInt();
      googleSheetsService.updateAttemptLeft(username, attemptLeft);
      event.getHook().sendMessage("Thanks for your update!").setEphemeral(true).queue();
    } catch (Exception e) {
      log.error("", e);
      event.getHook().sendMessage("Failed to update the sheet!").setEphemeral(true).queue();
    }
  }
}
