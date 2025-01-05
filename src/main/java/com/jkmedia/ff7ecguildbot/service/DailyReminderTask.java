package com.jkmedia.ff7ecguildbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class DailyReminderTask {
  private final DiscordBotService discordBotService;
  private long dailyReminderChannelId;

  @Scheduled(cron = "${daily-reminder.cron.expression}")
  public void remind() {
    log.debug("Daily reminder !");
    try {
      discordBotService.sendMessage(dailyReminderChannelId, "@everyone Dailies reminder");
    } catch (Exception e) {
      log.error("", e);
    }
  }

  @Value("${daily-reminder.channel-id}")
  public void setDailyReminderChannelId(long dailyReminderChannelId) {
    this.dailyReminderChannelId = dailyReminderChannelId;
  }
}
