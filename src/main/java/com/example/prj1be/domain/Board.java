package com.example.prj1be.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
}
