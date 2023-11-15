package com.example.prj1be.service;

import com.example.prj1be.dao.CommentMapper;
import com.example.prj1be.domain.Comment;
import com.example.prj1be.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentMapper mapper;

  public boolean add(Comment comment, Member login) {
    comment.setMemberId(login.getId());

    return mapper.insert(comment) == 1;
  }

  public boolean validate(Comment comment) {
    if (comment == null) return false;

    if (comment.getBoardId() == null || comment.getBoardId() < 1) return false;

    if (comment.getComment().isEmpty() || comment.getComment().isBlank()) return false;

    return true;
  }

  public List<Comment> list(Integer boardId) {
    return mapper.selectByBoardId(boardId);
  }

  public boolean delete(Integer id) {
    return mapper.deleteById(id) == 1;
  }

  public boolean update(Comment comment) {

    return mapper.updateById(comment) == 1;
  }
}
