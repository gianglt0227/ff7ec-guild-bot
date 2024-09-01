package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.config.GuildConfiguration;
import com.jkmedia.ff7ecguildbot.object.Guild;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GuildManagerServiceImpl implements GuildManagerService {
  private final GuildConfiguration guildConfiguration;
  private Map<Long, Guild> channelGuildMap;

  @PostConstruct
  public void init() {
    channelGuildMap = guildConfiguration.getGuilds().stream()
        .collect(Collectors.toMap(Guild::getChannelId, Function.identity()));
  }

  @Override
  public Guild findByChannelId(Long channelId) {
    return channelGuildMap.get(channelId);
  }

  @Override
  public Collection<Guild> findAll() {
    return channelGuildMap.values();
  }
}
