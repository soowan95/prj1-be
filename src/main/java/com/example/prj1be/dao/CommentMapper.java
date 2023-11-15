package com.example.prj1be.dao;

import com.example.prj1be.domain.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

  @Insert("""
  INSERT INTO comment (comment, boardId, memberId)
  VALUES (#{comment}, #{boardId}, #{memberId})
  """)
  int insert(Comment comment);
}
