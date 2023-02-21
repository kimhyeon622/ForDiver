package com.first.project.dao;

import com.first.project.dto.CodeDTO;
import com.first.project.dto.CommentDTO;
import com.first.project.dto.CommentLikeDTO;
import com.first.project.dto.PageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Mapper
public interface SummerDAO {
    //작성 완료
    int summerWrite(CodeDTO code);

    //상세보기
    CodeDTO summerView(int codeNum);

    //조회수 증가
    void summerViewCount(int codeNum);

    //게시글 삭제
    int summerDelete(int codeNum);

    //페이징 처리 위한 열 세기
    int summerCount(String keyword);

    //페이징 처리된 게시글 목록
    List<CodeDTO> summerList(PageDTO paging);

    //댓글 작성
    int commentWrite(CommentDTO comment);

    //순수 댓글 목록 조회
    List<CommentDTO> commentList(int codeNum);

    //게시글 좋아요 여부 확인
    CodeDTO summerLikeCheck(CodeDTO code);

    //게시글 좋아요 갯수 세기
    int summerLikeCount(int codeNum);

    //게시글 좋아요 증가
    int summerLikeInsert(CodeDTO code);

    //게시글 좋아요 감소
    int summerLikeDelete(CodeDTO code);

    //게시글 작성
    void childWrite(CommentDTO comment);

    //답글 목록
    List<CommentDTO> childList(int codeNum);

    List<CommentDTO> commentLikeList(CommentDTO comment);

    List<CommentDTO> allComment(int codeNum);

    int commentLikeCount(int scNum);

    CommentLikeDTO commentLikeCheck(CommentLikeDTO commentLike);

    int commentLikeInsert(CommentLikeDTO commentLike);

    int commentLikeDelete(CommentLikeDTO commentLike);

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
