package com.example.prj1be.service;

import com.example.prj1be.dao.BoardMapper;
import com.example.prj1be.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

  private final BoardMapper mapper;

  public void save(Board board) {
    mapper.insert(board);
  }
}
