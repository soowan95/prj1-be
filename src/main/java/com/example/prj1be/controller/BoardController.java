package com.example.prj1be.controller;

import com.example.prj1be.domain.Board;
import com.example.prj1be.domain.Member;
import com.example.prj1be.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/board")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService service;

  @PostMapping("add")
  public ResponseEntity add(Board board,
                            @RequestParam(value = "file[]", required = false) MultipartFile[] files,
                            @SessionAttribute(value = "login", required = false) Member login) throws IOException {

    if (login == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    if (!service.validate(board)) {
      return ResponseEntity.badRequest().build();
    }

    if (service.save(board, files, login)) return ResponseEntity.ok().build();
    else return ResponseEntity.internalServerError().build();
  }

  @GetMapping("list")
  public Map<String, Object> list(@RequestParam(value = "p", defaultValue = "1") Integer page,
                                  @RequestParam(value = "k", defaultValue = "") String keyword,
                                  @RequestParam(value = "c", defaultValue = "all") String category) {

    return service.list(page, keyword, category);
  }

  @GetMapping("id/{id}")
  public Board get(@PathVariable Integer id,
                   @SessionAttribute(value = "login", required = false) Member login) {
    Board board = service.get(id);
    board.setIsLike(false);

    if (login != null) {
      board.setIsLike(service.isLike(id, login.getId()));
    }

    return board;
  }

  @DeleteMapping("remove/{id}")
  public ResponseEntity remove(@PathVariable Integer id, @SessionAttribute(value = "login", required = false) Member login) {
    if (login == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    if (!service.hasAccess(id, login)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    if (service.delete(id)) return ResponseEntity.ok().build();
    else return ResponseEntity.badRequest().build();
  }

  @PutMapping("update/{id}")
  public ResponseEntity update(@PathVariable Integer id,
                               Board board,
                               @RequestParam(value = "file[]", required = false) MultipartFile[] files,
                               @SessionAttribute(value = "login", required = false) Member login) throws IOException {
    if (login == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    if (!service.hasAccess(id, login)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    if (service.update(id, board, files, login)) return ResponseEntity.ok().build();
    else return ResponseEntity.badRequest().build();
  }
}
