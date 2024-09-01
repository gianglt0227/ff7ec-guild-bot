package com.jkmedia.ff7ecguildbot;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.local.LocalBucket;
import java.time.Duration;

public class GoogleSheetUtil {
  private GoogleSheetUtil() {}

  public static String columnNumberToLetter(int inputColumnNumber) {
    String outputColumnName = "";
    int base = 26;
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    int tempNumber = inputColumnNumber;
    while (tempNumber > 0) {
      int position = tempNumber % base;
      outputColumnName = (position == 0 ? 'Z' : chars.charAt(position > 0 ? position - 1 : 0)) + outputColumnName;
      tempNumber = (tempNumber - 1) / base;
    }
    return outputColumnName;
  }

  public static int columnLetterToNumber(String inputColumnName) {
    int outputColumnNumber = 0;

    if (inputColumnName == null || inputColumnName.isEmpty()) {
      throw new IllegalArgumentException("Input is not valid!");
    }

    int i = inputColumnName.length() - 1;
    int t = 0;
    while (i >= 0) {
      char curr = inputColumnName.charAt(i);
      outputColumnNumber = outputColumnNumber + (int) Math.pow(26, t) * (curr - 'A' + 1);
      t++;
      i--;
    }
    return outputColumnNumber;
  }

  public static LocalBucket buildRateLimiter(Integer quota) {
    return Bucket.builder()
        .addLimit(limit -> limit.capacity(quota)
            .refillGreedy(quota, Duration.ofMinutes(1))
            .initialTokens(quota))
        .build();
  }
}
