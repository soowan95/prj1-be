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
  SELECT id, title, writer, inserted
  FROM board
  ORDER BY id DESC 
  """)
  List<Board> selectAll();

  @Select("""
  SELECT *
  FROM board
  WHERE id = #{id}
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
}
