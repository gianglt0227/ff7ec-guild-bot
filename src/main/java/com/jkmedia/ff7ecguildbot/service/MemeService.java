package com.jkmedia.ff7ecguildbot.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public interface MemeService {
  String fetchRandomMeme() throws URISyntaxException, MalformedURLException;
}
