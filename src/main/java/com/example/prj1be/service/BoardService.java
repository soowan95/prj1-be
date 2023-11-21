package com.example.prj1be.service;

import com.example.prj1be.dao.*;
import com.example.prj1be.domain.Board;
import com.example.prj1be.domain.BoardFile;
import com.example.prj1be.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BoardService {

  private final BoardMapper mapper;
  private final MemberService memberService;
  private final CommentMapper commentMapper;
  private final LikeMapper likeMapper;
  private final FileMapper fileMapper;

  private final S3Client s3;
  @Value("${aws.s3.bucket.name}")
  private String bucket;
  @Value("${image.file.prefix}")
  private String urlPrefix;

  public boolean save(Board board, MultipartFile[] files, Member login) throws IOException {

    board.setWriter(login.getId());

    int cnt = mapper.insert(board);

    // boardFile 데이블에 files 정보 저장
    // id, boardId, name
    if (files != null) {

      for (MultipartFile file : files) {
        fileMapper.insert(board.getId(), file.getOriginalFilename());

        // 실제 파일을 S3 bucket에 upload
        // 일단 local에 저장
        upload(board.getId(), file);
      }
    }

    return cnt == 1;
  }

  private void upload(Integer boardId, MultipartFile file) throws IOException {

    String key = "prj1/" + boardId + "/" + file.getOriginalFilename();

    PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build();

    s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
  }

  public boolean validate(Board board) {
    if (board == null) return false;
    if (board.getTitle().isEmpty() || board.getTitle().isBlank()) return false;
    if (board.getContent().isEmpty() || board.getContent().isBlank()) return false;
    return true;
  }

  public Map<String, Object> list(Integer page, String keyword) {
    Map<String, Object> map = new HashMap<>();
    Map<String, Object> pageInfo = new HashMap<>();

//    int countAll = mapper.countAll();
    int countAll = mapper.countAll("%" + keyword + "%");
    int lastPageNumber = (countAll - 1) / 10 + 1;
    int startPageNumber = (page - 1) / 10 * 10 + 1;
    int endPageNumber = startPageNumber + 9;
    endPageNumber = Math.min(endPageNumber, lastPageNumber);
    int prevPageNumber = startPageNumber - 10;
    int nextPageNumber = endPageNumber + 1;

    pageInfo.put("currentPageNumber", page);
    pageInfo.put("startPageNumber", startPageNumber);
    pageInfo.put("endPageNumber", endPageNumber);
    if (prevPageNumber > 0) {
      pageInfo.put("prevPageNumber", prevPageNumber);
    }
    if (nextPageNumber <= lastPageNumber) {
      pageInfo.put("nextPageNumber", nextPageNumber);
    }

    int from = (page - 1) * 10;
    map.put("boardList", mapper.selectAll(from, "%" + keyword + "%"));
    map.put("pageInfo", pageInfo);
    return map;
  }

  public Board get(Integer id) {
    Board board = mapper.selectById(id);

    List<BoardFile> boardFiles = fileMapper.selectNamesByBoardId(id);

    for (BoardFile boardFile : boardFiles) {
      String url = urlPrefix + "prj1/" + id + "/" + boardFile.getName();
      boardFile.setUrl(url);
    }

    board.setFiles(boardFiles);

    return board;
  }

  public boolean delete(Integer id) {
    // 자신이 쓴 글의 댓글 지우기
    commentMapper.deleteByBoardId(id);
    // 자신이 쓴 글의 좋아요 지우기
    likeMapper.deleteByBoardId(id);
    // s3에 업로드한 파일 지우기
    deleteFile(id);

    return mapper.deleteById(id) == 1;
  }

  private void deleteFile(Integer id) {
    // 파일명 조회
    List<BoardFile> boardFiles = fileMapper.selectNamesByBoardId(id);
    // s3 bucket objects 지우기
    for ( BoardFile file : boardFiles) {
      String key = "prj1/" + id + "/" + file.getName();

      DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
              .bucket(bucket)
              .key(key)
              .build();

      s3.deleteObject(objectRequest);
    }
    // 자신이 쓴 글의 파일 지우기
    fileMapper.deleteByBoardId(id);
  }

  public boolean update(Integer id, Board board, Member login) {
    Board dbBoard = mapper.selectById(id);

    if (board.getWriter().isBlank()) board.setWriter(login.getId());
    if (board.getTitle().isBlank()) board.setTitle(dbBoard.getTitle());
    if (board.getContent().isBlank()) board.setContent(dbBoard.getContent());

    return mapper.updateById(id, board) == 1;
  }

  public boolean hasAccess(Integer id, Member login) {
    if (memberService.isAdmin(login)) return true;
    Board board = mapper.selectById(id);

    return board.getWriter().equals(login.getId());
  }

  public boolean isLike(Integer id, String memberId) {
    return mapper.isLike(id, memberId) == 1;
  }
}
