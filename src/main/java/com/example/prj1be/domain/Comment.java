package com.example.prj1be.domain;

import com.example.prj1be.util.AppUtil;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Data
public class Comment {

  private Integer id;
  private String comment;
  private String memberId;
  private Integer boardId;
  private String nickName;
  private LocalDateTime inserted;

  public String getAgo() {
    return AppUtil.getAgo(inserted);
  }
}
