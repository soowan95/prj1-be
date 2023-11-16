package com.example.prj1be.dao;

import com.example.prj1be.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

  @Insert("""
  INSERT INTO comment (comment, boardId, memberId)
  VALUES (#{comment}, #{boardId}, #{memberId})
  """)
  int insert(Comment comment);

  @Select("""
  SELECT c.id, comment, boardId, memberId, c.inserted, nickName
  FROM comment c JOIN member m ON c.memberId = m.id
  WHERE boardId = #{boardId}
  """)
  List<Comment> selectByBoardId(Integer boardId);

  @Delete("""
  DELETE FROM comment
  WHERE id = #{id}
  """)
  int deleteById(Integer id);

  @Update("""
  UPDATE comment
  SET comment = #{comment}, inserted = NOW()
  WHERE id = #{id}
  """)
  int updateById(Comment comment);

  @Select("""
  SELECT COUNT(*) `count`
  FROM comment
  WHERE boardId = #{boardId}
  """)
  int countByBoardId(Integer boardId);
}
