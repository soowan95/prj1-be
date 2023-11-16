package com.example.prj1be.dao;

import com.example.prj1be.domain.Board;
import com.example.prj1be.domain.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

  @Insert("""
  INSERT INTO board (title, content, writer)
  VALUES (#{title}, #{content}, #{writer})
  """)
  int insert(Board board);

  @Select("""
  SELECT b.id, b.title, writer, b.inserted, nickName, COUNT(c.id) `commentCount`, (SELECT COUNT(*) FROM boardlike WHERE boardId = b.id) `likeCount`
  FROM board b JOIN member m ON b.writer = m.id
    LEFT JOIN comment c ON b.id = c.boardId
  GROUP BY b.id
  ORDER BY b.id DESC 
  """)
  List<Board> selectAll();

  @Select("""
  SELECT b.id, title, content, writer, b.inserted, nickName
  FROM board b JOIN prj1.member m on m.id = b.writer
  WHERE b.id = #{id}
  """)
  Board selectById(Integer id);

  @Delete("""
  DELETE FROM board
  WHERE id = #{id}
  """)
  int deleteById(Integer id);

  @Update("""
  UPDATE board
  SET title = #{board.title}, content = #{board.content}, writer = #{board.writer}, inserted = NOW()
  WHERE id = #{id}
  """)
  int updateById(Integer id, Board board);

  @Delete("""
  DELETE FROM board
  WHERE writer = #{writer}
  """)
  int deleteByWriter(String writer);

  @Select("""
  SELECT COUNT(id)
  FROM boardlike
  WHERE boardId = #{id} AND memberId = #{memberId}
  """)
  Integer isLike(Integer id, String memberId);
}
