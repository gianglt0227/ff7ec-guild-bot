package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.object.Guild;
import java.util.Collection;

public interface GuildManagerService {
  Guild findByChannelId(Long channelId);

  Collection<Guild> findAll();
}
