package com.example.prj1be.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Member {
  private String id;
  private String password;
  private String nickName;
  private String email;
  private List<Auth> auth;
  private LocalDateTime inserted;
}