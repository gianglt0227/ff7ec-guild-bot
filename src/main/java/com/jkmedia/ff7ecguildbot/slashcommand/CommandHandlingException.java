package com.jkmedia.ff7ecguildbot.slashcommand;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommandHandlingException extends RuntimeException {
  private final String replyMessage;

  public CommandHandlingException(String replyMessage, Throwable e) {
    super(e);
    this.replyMessage = replyMessage;
  }
}
