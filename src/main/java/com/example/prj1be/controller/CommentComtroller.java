package com.example.prj1be.controller;

import com.example.prj1be.domain.Comment;
import com.example.prj1be.domain.Member;
import com.example.prj1be.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/comment")
@RestController
@RequiredArgsConstructor
public class CommentComtroller {

  private final CommentService service;

  @PostMapping("add")
  public ResponseEntity add(@RequestBody Comment comment, @SessionAttribute(value = "login", required = false) Member login) {
    if (login == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    if (service.validate(comment)) {
      if (service.add(comment, login)) return ResponseEntity.ok().build();
      else return ResponseEntity.internalServerError().build();
    } else return ResponseEntity.badRequest().build();
  }
}
