package com.jkmedia.ff7ecguildbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AttemptRestartTask {
  private final GoogleSheetsService googleSheetsService;
  private final GuildManagerService guildManagerService;

  @Scheduled(cron = "0 0 9 * * *")
  public void restartAttempts() {
    log.debug("Resetting all attempts");

    guildManagerService.findAll().forEach(guild -> {
      try {
        googleSheetsService.restartAttempts(guild.getGoogleSpreadsheetId());
      } catch (Exception e) {
        log.error("", e);
      }
    });
  }
}
