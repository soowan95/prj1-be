package com.example.prj1be.service;

import com.example.prj1be.dao.LikeMapper;
import com.example.prj1be.domain.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

  private final LikeMapper mapper;

  public void update(Like like) {
    // 처음 좋아요 누를 때 : insert
    // 다시 누르면 : delete
    if (mapper.delete(like) == 0) {
      int count = mapper.insert(like);
    }
  }
}
