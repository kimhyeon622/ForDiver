package com.first.project.service;

import com.first.project.dto.CodeDTO;
import com.first.project.dto.CommentDTO;
import com.first.project.dto.CommentLikeDTO;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface SummerService {
    //글 작성
    String summerWrite(CodeDTO code);

    //게시글 상세보기
    CodeDTO summerView(int codeNum);

    //조회수 증가
    void summerViewCount(int codeNum);

    //게시글 삭제
    int summerDelete(int codeNum);

    //최초 이동 시
    ModelAndView summerList(int page, int size, String keyword);

    //ajax를 통한 페이징 처리
    ModelAndView summerPagingList(int page, int size, String keyword);

    //댓글 작성
    int commentWrite(CommentDTO comment);

    //순수 댓글 목록
    List<CommentDTO> commentList(int codeNum);

    //사용자의 게시글 좋아요 누른 여부
    CodeDTO summerLikeCheck(CodeDTO code);

    //해당 게시글의 좋아요 갯수
    int summerLikeCount(int codeNum);

    //게시글 좋아요 증가
    int summerLikeInsert(CodeDTO code);

    //게시글 좋아요 감소
    int summerLikeDelete(CodeDTO code);

    //답글 작성
    void childWrite(CommentDTO comment);

    //답글 목록
    List<CommentDTO> childList(int codeNum);

    //댓글 좋아요 목록
    List<CommentDTO> commentLikeList(CommentDTO comment);

    //모든 댓답글
    List<CommentDTO> allComment(int codeNum);

    //댓답글의 좋아요 갯수 확인
    int commentLikeCount(int scNum);

    CommentLikeDTO commentLikeCheck(CommentLikeDTO commentLike);

    int commentLikeInsert(CommentLikeDTO commentLike);

    int commentLikeDelete(CommentLikeDTO commentLike);

    //로그인 회원의 게시글 댓답글 좋아요 확인
    List<CommentLikeDTO> commentLikeListCheck(CommentLikeDTO commentLike);

    List<CommentDTO> collectChild(CommentDTO comment);

    int commentDelete(int scNum);

    int commentModify(CommentDTO comment);

    void commentDeleteForSummerDelete(int codeNum);

    List<Integer> realAllComment(int codeNum);

    void realCommentLikeDelete(Integer integer);

    void summerLikeDeleteForSummerDelete(int codeNum);

    String checkForCommentModifyAndDelete(int num);

    int summerModify(CodeDTO code);
}
