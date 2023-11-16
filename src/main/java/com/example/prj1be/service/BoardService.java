package com.example.prj1be.service;

import com.example.prj1be.dao.BoardMapper;
import com.example.prj1be.dao.CommentMapper;
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
  private final MemberService memberService;
  private final CommentMapper commentMapper;

  public boolean save(Board board, Member login) {
    board.setWriter(login.getId());

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

  public boolean delete(Integer id) {
    commentMapper.deleteByBoardId(id);

    return mapper.deleteById(id) == 1;
  }

  public boolean update(Integer id, Board board, Member login) {
    Board dbBoard = mapper.selectById(id);

    if (board.getWriter().isBlank()) board.setWriter(login.getId());
    if (board.getTitle().isBlank()) board.setTitle(dbBoard.getTitle());
    if (board.getContent().isBlank()) board.setContent(dbBoard.getContent());

    return mapper.updateById(id, board) == 1;
  }

  public boolean hasAccess(Integer id, Member login) {
    if (memberService.isAdmin(login)) return true;
    Board board = mapper.selectById(id);

    return board.getWriter().equals(login.getId());
  }

  public boolean isLike(Integer id, String memberId) {
    return mapper.isLike(id, memberId) == 1;
  }
}
