package com.example.prj1be.controller;

import com.example.prj1be.domain.Member;
import com.example.prj1be.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
  private final MemberService service;

  @PostMapping("signup")
  public ResponseEntity signup(@RequestBody Member member) {
    if (service.validate(member)) {
      if (service.add(member)) {
        return ResponseEntity.ok().build();
      } else {
        return ResponseEntity.internalServerError().build();
      }
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping(value = "check", params = "id")
  public ResponseEntity checkId(String id) {
    if (service.getId(id) == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok().build();
    }
  }

  @GetMapping(value = "check", params = "email")
  public ResponseEntity checkEmail(String email) {
    if (service.getEmail(email) == null) return ResponseEntity.notFound().build();
    else return ResponseEntity.ok().build();
  }

  @GetMapping(value = "check", params = "nickName")
  public ResponseEntity checkNickName(String nickName) {
    if (service.getNickName(nickName) == null) return ResponseEntity.notFound().build();
    else return ResponseEntity.ok().build();
  }

  @GetMapping("list")
  public List<Member> list() {
    return service.list();
  }

  @GetMapping
  public ResponseEntity<Member> view(String id) {
    // todo: 로그인 했는지? -> 안했으면 401 응답
    // todo: 자기 정보인지? -> 아니면 403 응답
    Member member = service.getMember(id);

    return ResponseEntity.ok(member);
  }

  @DeleteMapping
  public ResponseEntity delete(String id) {
    // todo: 로그인 했는지? -> 안했으면 401 응답
    // todo: 자기 정보인지? -> 아니면 403 응답
    if (service.deleteMember(id)) return ResponseEntity.ok().build();
    return ResponseEntity.internalServerError().build();
  }

  @PutMapping
  public ResponseEntity update(String id, @RequestBody Member member) {
    if (service.getEmail(id, member.getEmail()) == null && service.getNickName(id, member.getNickName()) == null && service.updateMember(id, member))
      return ResponseEntity.ok().build();
    else if (service.getNickName(id, member.getNickName()) != null) return ResponseEntity.notFound().build();
    return ResponseEntity.badRequest().build();
  }

  @PostMapping("login")
  public ResponseEntity login(@RequestBody Member member) {
    System.out.println("member = " + member);
    if (service.login(member)) return ResponseEntity.ok().build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}