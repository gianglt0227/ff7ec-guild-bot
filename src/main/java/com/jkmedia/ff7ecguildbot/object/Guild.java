package com.jkmedia.ff7ecguildbot.object;

import lombok.Data;

@Data
public class Guild {
  private String guildName;
  private String guildId;
  private Long channelId;
  private String channelName;
  private String googleSpreadsheetId;
  private GuildSpreadsheet guildSpreadsheet;
}
