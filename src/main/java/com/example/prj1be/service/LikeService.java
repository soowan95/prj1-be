package com.example.prj1be.service;

import com.example.prj1be.dao.LikeMapper;
import com.example.prj1be.domain.Like;
import com.example.prj1be.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class LikeService {

  private final LikeMapper mapper;

  public boolean update(Like like, Member login) {
    // 처음 좋아요 누를 때 : insert
    // 다시 누르면 : delete
    like.setMemberId(login.getId());

    int count = 0;

    if (mapper.delete(like) == 0) {
      count = mapper.insert(like);
    }

    return count == 1;
  }
}
