package com.jkmedia.ff7ecguildbot.config;

import com.jkmedia.ff7ecguildbot.object.Guild;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "guild")
@Data
public class GuildConfiguration {
  private List<Guild> guilds;
}
