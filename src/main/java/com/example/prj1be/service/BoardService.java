package com.example.prj1be.service;

import com.example.prj1be.dao.BoardMapper;
import com.example.prj1be.dao.CommentMapper;
import com.example.prj1be.dao.LikeMapper;
import com.example.prj1be.domain.Board;
import com.example.prj1be.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

  private final BoardMapper mapper;
  private final MemberService memberService;
  private final CommentMapper commentMapper;
  private final LikeMapper likeMapper;

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

  public Map<String, Object> list(Integer page, String keyword) {
    Map<String, Object> map = new HashMap<>();
    Map<String, Object> pageInfo = new HashMap<>();

//    int countAll = mapper.countAll();
    int countAll = mapper.countAll("%" + keyword + "%");
    int lastPageNumber = (countAll - 1) / 10 + 1;
    int startPageNumber = (page - 1) / 10 * 10 + 1;
    int endPageNumber = startPageNumber + 9;
    endPageNumber = Math.min(endPageNumber, lastPageNumber);
    int prevPageNumber = startPageNumber - 10;
    int nextPageNumber = endPageNumber + 1;

    pageInfo.put("currentPageNumber", page);
    pageInfo.put("startPageNumber", startPageNumber);
    pageInfo.put("endPageNumber", endPageNumber);
    if (prevPageNumber > 0) {
      pageInfo.put("prevPageNumber", prevPageNumber);
    }
    if (nextPageNumber <= lastPageNumber) {
      pageInfo.put("nextPageNumber", nextPageNumber);
    }

    int from = (page - 1) * 10;
    map.put("boardList", mapper.selectAll(from, "%" + keyword + "%"));
    map.put("pageInfo", pageInfo);
    return map;
  }

  public Board get(Integer id) {
    return mapper.selectById(id);
  }

  public boolean delete(Integer id) {
    commentMapper.deleteByBoardId(id);

    likeMapper.deleteByBoardId(id);

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
