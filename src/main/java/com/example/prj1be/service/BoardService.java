package com.example.prj1be.service;

import com.example.prj1be.dao.BoardMapper;
import com.example.prj1be.domain.Board;
import com.example.prj1be.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

  private final BoardMapper mapper;

  public boolean save(Board board, Member login) {
    board.setWriter(login.getNickName());

    return mapper.insert(board) == 1;
  }

  public boolean validate(Board board) {
    if (board == null) return false;
    if (board.getTitle().isEmpty() || board.getTitle().isBlank()) return false;
    if (board.getContent().isEmpty() || board.getContent().isBlank()) return false;
    return true;
  }

  public List<Board> list() {
    return mapper.selectAll();
  }

  public Board get(Integer id) {
    return mapper.selectById(id);
  }

  public boolean remove(Integer id) {
    return mapper.deleteById(id) == 1;
  }

  public boolean update(Integer id, Board board) {
    return mapper.updateById(id, board) == 1;
  }
}
