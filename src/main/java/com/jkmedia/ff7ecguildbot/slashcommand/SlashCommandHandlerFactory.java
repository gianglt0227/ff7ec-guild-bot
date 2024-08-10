package com.jkmedia.ff7ecguildbot.slashcommand;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlashCommandHandlerFactory {
  protected static final Map<SlashCommand, SlashCommandHandler> HANDLER_CACHE =
      new EnumMap<>(SlashCommand.class);

  private final List<SlashCommandHandler> slashCommandHandlers;

  @PostConstruct
  void initCache() {
    slashCommandHandlers.forEach(
        slashCommandHandler ->
            HANDLER_CACHE.put(slashCommandHandler.supportedCommand(), slashCommandHandler));
  }

  public SlashCommandHandler getSlashCommandHandler(String command) {
    SlashCommand slashCommand = SlashCommand.getEnum(command);
    if (HANDLER_CACHE.containsKey(slashCommand)) {
      return HANDLER_CACHE.get(slashCommand);
    } else {
      throw new UnsupportedOperationException(
          "No implementation found for slash command " + slashCommand.getValue());
    }
  }
}
