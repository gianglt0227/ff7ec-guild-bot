package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.GoogleSheetUtil;
import com.jkmedia.ff7ecguildbot.config.GuildConfiguration;
import com.jkmedia.ff7ecguildbot.object.Guild;
import com.jkmedia.ff7ecguildbot.object.RateLimitMode;
import io.github.bucket4j.Bucket;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GuildManagerServiceImpl implements GuildManagerService {
  private final GuildConfiguration guildConfiguration;
  private Map<Long, Guild> channelGuildMap;
  private RateLimitMode rateLimitMode;
  private Integer googleSheetReadQuota;
  private Integer googleSheetWriteQuota;

  @PostConstruct
  public void init() {
    channelGuildMap = guildConfiguration.getGuilds().stream()
        .collect(Collectors.toMap(Guild::getChannelId, Function.identity()));
    if (rateLimitMode == RateLimitMode.PER_GUILD) {
      for (Guild guild : channelGuildMap.values()) {
        guild.getGuildSpreadsheet().setWriteRateLimit(GoogleSheetUtil.buildRateLimiter(googleSheetWriteQuota));
        guild.getGuildSpreadsheet().setReadRateLimit(GoogleSheetUtil.buildRateLimiter(googleSheetReadQuota));
      }
    } else {
      Bucket globalWriteBucket = GoogleSheetUtil.buildRateLimiter(googleSheetWriteQuota);
      Bucket globalReadBucket = GoogleSheetUtil.buildRateLimiter(googleSheetReadQuota);
      for (Guild guild : channelGuildMap.values()) {
        guild.getGuildSpreadsheet().setWriteRateLimit(globalWriteBucket);
        guild.getGuildSpreadsheet().setReadRateLimit(globalReadBucket);
      }
    }
  }

  @Override
  public Guild findByChannelId(Long channelId) {
    return channelGuildMap.get(channelId);
  }

  @Override
  public Collection<Guild> findAll() {
    return channelGuildMap.values();
  }

  @Value("${google-sheet.rate-limit.mode:GLOBAL}")
  public void setRateLimitMode(RateLimitMode rateLimitMode) {
    this.rateLimitMode = rateLimitMode;
  }

  @Value("${google-sheet.rate-limit.read-quota:60}")
  public void setGoogleSheetReadQuota(Integer googleSheetReadQuota) {
    this.googleSheetReadQuota = googleSheetReadQuota;
  }

  @Value("${google-sheet.rate-limit.write-quota:60}")
  public void setGoogleSheetWriteQuota(Integer googleSheetWriteQuota) {
    this.googleSheetWriteQuota = googleSheetWriteQuota;
  }
}
