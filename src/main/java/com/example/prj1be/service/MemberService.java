package com.example.prj1be.service;

import com.example.prj1be.dao.*;
import com.example.prj1be.domain.Auth;
import com.example.prj1be.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberService {

  private final MemberMapper mapper;
  private final BoardMapper boardMapper;
  private final CommentMapper commentMapper;
  private final LikeMapper likeMapper;
  private final FileMapper fileMapper;

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
    // 0. 이 멤버가 작성한 댓글, 좋아요 삭제

    commentMapper.deleteByWriter(id);
    likeMapper.deleteByWriter(id);

    // 1-1. 이 멤버가 작성한 게시물의 댓글, 좋아요, 파일 삭제

    for (Integer i : mapper.selectBoardIdById(id)) {
      commentMapper.deleteByBoardId(i);
      likeMapper.deleteByBoardId(i);
      fileMapper.deleteByBoardId(i);
    }

    // 1. 이 멤버가 작성한 게시물 삭제

    boardMapper.deleteByWriter(id);

    // 2. 이 멤버 삭제

    return mapper.deleteById(id) == 1;
  }

  public boolean updateMember(String id, Member member) {
    Member originMember = mapper.selectById(id);

    if (member.getPassword().isEmpty()) member.setPassword(originMember.getPassword());

    return mapper.updateById(id, member) == 1;
  }

  public boolean login(Member member, WebRequest request) {
    Member dbMember = mapper.selectById(member.getId());

    if (dbMember != null && dbMember.getPassword().equals(member.getPassword())) {
      List<Auth> auth = mapper.selectAuthById(member.getId());
      dbMember.setAuth(auth);
      dbMember.setPassword("");
      request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
      return true;
    }

    return false;
  }

  public boolean hasAccess(String id, Member login) {
    if (isAdmin(login)) return true;

    return id.equals(login.getId());
  }

  public boolean isAdmin(Member login) {
    if (login.getAuth() != null) {
      return login.getAuth().stream().map(Auth::getName).anyMatch(a -> a.equals("admin"));
    }
    return false;
  }
}