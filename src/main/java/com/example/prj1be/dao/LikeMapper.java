package com.example.prj1be.dao;

import com.example.prj1be.domain.Like;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeMapper {

  @Delete("""
  DELETE FROM boardLike
  WHERE boardId = #{boardId} AND memberId = #{memberId}
  """)
  int delete(Like like);

  @Insert("""
  INSERT INTO boardlike (boardId, memberId)
  VALUES (#{boardId}, #{memberId})
  """)
  int insert(Like like);

  @Delete("""
  DELETE FROM boardlike
  WHERE boardId = #{id}
  """)
  void deleteByBoardId(Integer id);

  @Delete("""
  DELETE FROM boardlike
  WHERE memberId = #{id}
  """)
  void deleteByWriter(String id);
}
