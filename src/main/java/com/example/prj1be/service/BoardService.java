package com.example.prj1be.service;

import com.example.prj1be.dao.BoardMapper;
import com.example.prj1be.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

  private final BoardMapper mapper;

  public boolean save(Board board) {
    return mapper.insert(board) == 1;
  }

  public boolean validate(Board board) {
    if (board == null) return false;
    if (board.getTitle().isEmpty() || board.getTitle().isBlank()) return false;
    if (board.getContent().isEmpty() || board.getContent().isBlank()) return false;
    if (board.getWriter().isEmpty() || board.getWriter().isBlank()) return false;
    return true;
  }

  public List<Board> list() {
    return mapper.selectAll();
  }
}
