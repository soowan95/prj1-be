package com.example.prj1be.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {

  private Integer id;
  private String comment;
  private String memberId;
  private Integer boardId;
  private LocalDateTime inserted;
}
