package com.example.prj1be.domain;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Data
public class Board {

  private Integer id;
  private String title;
  private String content;
  private String writer;
  private String nickName;
  private Integer commentCount;
  private Integer likeCount;
  private LocalDateTime inserted;
  private Boolean isLike;

  public String getAgo() {
    LocalDateTime now = LocalDateTime.now();

    if (inserted.isBefore(now.minusYears(1))) {
      Period between = Period.between(inserted.toLocalDate(), now.toLocalDate());
      return between.get(ChronoUnit.YEARS) + "년 전";
    } else if (inserted.isBefore(now.minusMonths(1))) {
      Period between = Period.between(inserted.toLocalDate(), now.toLocalDate());
      return between.get(ChronoUnit.MONTHS) + "달 전";
    } else if (inserted.isBefore(now.minusDays(1))) {
      Period between = Period.between(inserted.toLocalDate(), now.toLocalDate());
      return between.get(ChronoUnit.DAYS) + "일 전";
    } else if (inserted.isBefore(now.minusHours(1))) {
      Duration between = Duration.between(inserted, now);
      return between.toHours() + "시간 전";
    } else {
      Duration between = Duration.between(inserted, now);
      return between.toMinutes() + "분 전";
    }
  }
}
