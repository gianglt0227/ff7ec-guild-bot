package com.jkmedia.ff7ecguildbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Ff7ecGuildBotApplication {

  public static void main(String[] args) {
    SpringApplication.run(Ff7ecGuildBotApplication.class, args);
  }
}
