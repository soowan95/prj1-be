package com.example.prj1be.dao;

import com.example.prj1be.domain.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

  @Insert("""
  INSERT INTO board (title, content, writer)
  VALUES (#{title}, #{content}, #{writer})
  """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int insert(Board board);

  @Select("""
  <script>
  SELECT b.id, b.title, writer, b.inserted, nickName, 
         COUNT(DISTINCT c.id) `commentCount`, 
         (SELECT COUNT(*) FROM boardlike WHERE boardId = b.id) `likeCount`,
         COUNT(DISTINCT bf.id) `imageCount`
  FROM board b JOIN member m ON b.writer = m.id
    LEFT JOIN comment c ON b.id = c.boardId
    LEFT JOIN boardfile bf ON b.id = bf.boardId
  WHERE
  <trim prefixOverrides="OR">
    <if test="category == 'all' || category == 'title'"> 
      OR title LIKE #{keyword} 
    </if>
    <if test="category == 'all' || category == 'content'">
      OR content LIKE #{keyword}
    </if>
  </trim>
  GROUP BY b.id
  ORDER BY b.id DESC 
  LIMIT #{page}, 10
  </script>
  """)
  List<Board> selectAll(Integer page, String keyword, String category);

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
  SET title = #{board.title}, content = #{board.content}
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

  @Select("""
  <script>
  SELECT COUNT(*) FROM board
  WHERE
  <trim prefixOverrides="OR">
    <if test="category == 'all' || category == 'title'"> 
      OR title LIKE #{keyword} 
    </if>
    <if test="category == 'all' || category == 'content'">
      OR content LIKE #{keyword}
    </if>
  </trim>
  </script>
  """)
  int countAll(String keyword, String category);
}
