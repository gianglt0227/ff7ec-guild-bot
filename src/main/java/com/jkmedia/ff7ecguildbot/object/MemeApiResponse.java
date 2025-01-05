package com.jkmedia.ff7ecguildbot.object;

import java.util.List;
import lombok.Data;

@Data
public class MemeApiResponse {
  private String postLink;
  private String subreddit;
  private String title;
  private String url;
  private Boolean nsfw;
  private Boolean spoiler;
  private String author;
  private String ups;
  private List<String> preview;
}
