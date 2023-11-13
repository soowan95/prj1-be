package com.example.prj1be.service;

import com.example.prj1be.domain.Member;
import com.example.prj1be.dao.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberMapper mapper;

  public boolean add(Member member) {
    return mapper.insert(member) == 1;
  }

  public String getId(String id) {
    return mapper.selectId(id);
  }

  public String getEmail(String email) {
    return mapper.selectEmail(email);
  }

  public String getEmail(String id, String email) {

    if (mapper.selectEmailById(id).equals(email)) return null;
    return mapper.selectEmail(email);
  }

  public String getNickName(String nickName) {
    return mapper.selectNickName(nickName);
  }

  public String getNickName(String id, String nickName) {

    if (mapper.selectNickNameById(id).equals(nickName)) return null;
    return mapper.selectNickName(nickName);
  }

  public boolean validate(Member member) {
    if (member == null) {
      return false;
    }

    if (member.getEmail().isBlank()) {
      return false;
    }

    if (member.getPassword().isBlank()) {
      return false;
    }

    if (member.getId().isBlank()) {
      return false;
    }
    return true;
  }

  public List<Member> list() {
    return mapper.selectAll();
  }

  public Member getMember(String id) {
    return mapper.selectById(id);
  }

  public boolean deleteMember(String id) {
    return mapper.deleteById(id) == 1;
  }

  public boolean updateMember(String id, Member member) {
    Member originMember = mapper.selectById(id);

    if (member.getPassword().isEmpty()) member.setPassword(originMember.getPassword());

    return mapper.updateById(id, member) == 1;
  }
}