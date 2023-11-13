package com.example.prj1be.dao;

import com.example.prj1be.domain.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {

  @Insert("""
        INSERT INTO member (id, password, email, nickName)
        VALUES (#{id}, #{password}, #{email}, #{nickName})
        """)
  int insert(Member member);

  @Select("""
        SELECT id FROM member
        WHERE id = #{id}
        """)
  String selectId(String id);

  @Select("""
        SELECT email FROM member
        WHERE email = #{email}
        """)
  String selectEmail(String email);

  @Select("""
  SELECT email FROM member
  WHERE id = #{id}
  """)
  String selectEmailById(String id);

  @Select("""
  SELECT nickName FROM member
  WHERE nickName = #{nickName}
  """)
  String selectNickName(String nickName);

  @Select("""
  SELECT nickName FROM member
  WHERE id = #{id}
  """)
  String selectNickNameById(String id);

  @Select("""
        SELECT id, password, nickName, email, inserted
        FROM member
        ORDER BY inserted DESC
        """)
  List<Member> selectAll();

  @Select("""
  SELECT *
  FROM member
  WHERE id = #{id}
  """)
  Member selectById(String id);

  @Delete("""
  DELETE FROM member
  WHERE id = #{id}
  """)
  int deleteById(String id);

  @Update("""
  UPDATE member
  SET password = #{member.password}, email = #{member.email}, nickName = #{member.nickName}
  WHERE id = #{id}
  """)
  int updateById(String id, Member member);
}