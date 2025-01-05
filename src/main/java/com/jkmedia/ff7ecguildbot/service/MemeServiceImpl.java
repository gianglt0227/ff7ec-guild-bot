package com.jkmedia.ff7ecguildbot.service;

import com.jkmedia.ff7ecguildbot.object.MemeApiResponse;
import java.net.*;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemeServiceImpl implements MemeService {

  private final RestTemplate restTemplate;
  private String memeApiUrl;

  @Override
  public String fetchRandomMeme() throws URISyntaxException, MalformedURLException {
    URL url = new URL(memeApiUrl);
    MemeApiResponse response =
        restTemplate.getForEntity(url.toURI(), MemeApiResponse.class).getBody();

    return Objects.equals(Boolean.TRUE, response.getNsfw()) ? fetchRandomMeme() : response.getUrl();
  }

  @Value("${daily-reminder.meme-api-url:https://meme-api.com/gimme}")
  public void setMemeApiUrl(String memeApiUrl) {
    this.memeApiUrl = memeApiUrl;
  }
}
