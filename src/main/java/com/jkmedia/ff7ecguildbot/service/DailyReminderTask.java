package com.jkmedia.ff7ecguildbot.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
  private final MemeService memeService;
  private long dailyReminderChannelId;
  private String message;

  @Scheduled(cron = "${daily-reminder.cron.expression}")
  public void remind() {
    log.debug("Daily reminder !");
    try {
      String memeUrl = memeService.fetchRandomMeme();
      discordBotService.sendMessage(dailyReminderChannelId, message + " " + memeUrl);
    } catch (MalformedURLException | URISyntaxException e) {
      discordBotService.sendMessage(dailyReminderChannelId, message);
    } catch (Exception e) {
      log.error("", e);
    }
  }

  @Value("${daily-reminder.channel-id}")
  public void setDailyReminderChannelId(long dailyReminderChannelId) {
    this.dailyReminderChannelId = dailyReminderChannelId;
  }

  @Value("${daily-reminder.message}")
  public void setMessage(String message) {
    this.message = message;
  }
}
