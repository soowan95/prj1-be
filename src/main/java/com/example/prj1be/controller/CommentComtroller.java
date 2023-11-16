package com.example.prj1be.controller;

import com.example.prj1be.domain.Comment;
import com.example.prj1be.domain.Member;
import com.example.prj1be.service.CommentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  @GetMapping("list/{id}")
  public List<Comment> list(@PathVariable("id") Integer boardId) {
    return service.list(boardId);
  }

  @GetMapping("count/{id}")
  public Integer count(@PathVariable("id") Integer boardId) {
    return service.count(boardId);
  }

  @DeleteMapping("delete")
  public ResponseEntity delete(Integer id) {
    if (service.delete(id)) return ResponseEntity.ok().build();
    return ResponseEntity.internalServerError().build();
  }

  @PutMapping("update")
  public ResponseEntity update(@RequestBody Comment comment) {
    if (service.update(comment)) return ResponseEntity.ok().build();
    return ResponseEntity.internalServerError().build();
  }
}
