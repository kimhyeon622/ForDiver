package com.first.project.service;

import com.first.project.dao.SummerDAO;
import com.first.project.dto.CodeDTO;
import com.first.project.dto.CommentDTO;
import com.first.project.dto.CommentLikeDTO;
import com.first.project.dto.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SummerServiceImpl implements SummerService {

    private ModelAndView mav = new ModelAndView();

    private final SummerDAO sdao;

    @Override
    public String summerWrite(CodeDTO code) {

        String data = "";

        int result = sdao.summerWrite(code);

        if (result > 0) {
            data = "OK";
        } else {
            data = "NO";
        }

        return data;
    }

    @Override
    public CodeDTO summerView(int codeNum) {
        return sdao.summerView(codeNum);
    }

    @Override
    public void summerViewCount(int codeNum) {
        sdao.summerViewCount(codeNum);
    }

    @Override
    public int summerDelete(int codeNum) {

        return sdao.summerDelete(codeNum);
    }

    @Override
    public ModelAndView summerList(int page, int size, String keyword) {

        //한 화면에 보여줄 페이지 갯수 : 10개
        int block = 10;

        //한 화면에 보여줄 게시글 갯수 : 10개, limit에서 size로 함

        //전체 게시글 갯수 불러오기
        int summerCount = sdao.summerCount(keyword);

        int startRow = (page-1) * size + 1;
        int endRow = page * size;

        int maxPage = (int)(Math.ceil((double)summerCount / size)); //flow, round, ceil 이 중 올림에 해당
        int startPage = (((int)(Math.ceil((double)page / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        if (endPage < maxPage) {
            endPage = maxPage;
        }

        //페이징 객체 생성
        PageDTO paging = new PageDTO();

        paging.setPage(page);
        paging.setStartRow(startRow);
        paging.setEndRow(endRow);
        paging.setMaxPage(maxPage);
        paging.setStartPage(startPage);
        paging.setEndPage(endPage);
        paging.setSize(size);
        paging.setKeyword(keyword);

        List<CodeDTO> pagingList = sdao.summerList(paging);

        mav.addObject("pagingList", pagingList);
        mav.addObject("paging", paging);

        mav.setViewName("SummerList");

        return mav;
    }

    @Override
    public ModelAndView summerPagingList(int page, int size, String keyword) {

        //한 화면에 보여줄 페이지 갯수 : 10개
        int block = 10;

        //한 화면에 보여줄 게시글 갯수 : 10개, limit에서 size로 함

        //전체 게시글 갯수 불러오기
        int summerCount = sdao.summerCount(keyword);

        int startRow = (page-1) * size + 1;
        int endRow = page * size;

        int maxPage = (int)(Math.ceil((double)summerCount / size)); //flow, round, ceil 이 중 올림에 해당
        int startPage = (((int)(Math.ceil((double)page / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        if (endPage < maxPage) {
            endPage = maxPage;
        }

        //페이징 객체 생성
        PageDTO paging = new PageDTO();

        paging.setPage(page);
        paging.setStartRow(startRow);
        paging.setEndRow(endRow);
        paging.setMaxPage(maxPage);
        paging.setStartPage(startPage);
        paging.setEndPage(endPage);
        paging.setSize(size);
        paging.setKeyword(keyword);

        List<CodeDTO> pagingList = sdao.summerList(paging);

        mav.addObject("pagingList", pagingList);
        mav.addObject("paging", paging);

        return mav;
    }

    @Override
    public int commentWrite(CommentDTO comment) {
        return sdao.commentWrite(comment);
    }

    @Override
    public List<CommentDTO> commentList(int codeNum) {
        return sdao.commentList(codeNum);
    }

    @Override
    public CodeDTO summerLikeCheck(CodeDTO code) {
        return sdao.summerLikeCheck(code);
    }

    @Override
    public int summerLikeCount(int codeNum) {
        return sdao.summerLikeCount(codeNum);
    }

    @Override
    public int summerLikeInsert(CodeDTO code) {
        return sdao.summerLikeInsert(code);
    }

    @Override
    public int summerLikeDelete(CodeDTO code) {
        return sdao.summerLikeDelete(code);
    }

    @Override
    public void childWrite(CommentDTO comment) {
        sdao.childWrite(comment);
    }

    @Override
    public List<CommentDTO> childList(int codeNum) {
        return sdao.childList(codeNum);
    }

    @Override
    public List<CommentDTO> commentLikeList(CommentDTO comment) {
        return sdao.commentLikeList(comment);
    }

    @Override
    public List<CommentDTO> allComment(int codeNum) {
        return sdao.allComment(codeNum);
    }

    @Override
    public int commentLikeCount(int scNum) {
        return sdao.commentLikeCount(scNum);
    }

    @Override
    public CommentLikeDTO commentLikeCheck(CommentLikeDTO commentLike) {
        return sdao.commentLikeCheck(commentLike);
    }

    @Override
    public int commentLikeInsert(CommentLikeDTO commentLike) {
        return sdao.commentLikeInsert(commentLike);
    }

    @Override
    public int commentLikeDelete(CommentLikeDTO commentLike) {
        return sdao.commentLikeDelete(commentLike);
    }

    @Override
    public List<CommentLikeDTO> commentLikeListCheck(CommentLikeDTO commentLike) {
        return sdao.commentLikeListCheck(commentLike);
    }

    @Override
    public List<CommentDTO> collectChild(CommentDTO comment) {
        return sdao.collectChild(comment);
    }

    @Override
    public int commentDelete(int scNum) {
        return sdao.commentDelete(scNum);
    }

    @Override
    public int commentModify(CommentDTO comment) {
        return sdao.commentModify(comment);
    }

    @Override
    public void commentDeleteForSummerDelete(int codeNum) {
        sdao.commentDeleteForSummerDelete(codeNum);
    }

    @Override
    public List<Integer> realAllComment(int codeNum) {
        return sdao.realAllComment(codeNum);
    }

    @Override
    public void realCommentLikeDelete(Integer integer) {
        sdao.realCommentLikeDelete(integer);
    }

    @Override
    public void summerLikeDeleteForSummerDelete(int codeNum) {
        sdao.summerLikeDeleteForSummerDelete(codeNum);
    }

    @Override
    public String checkForCommentModifyAndDelete(int num) {
        return sdao.checkForCommentModifyAndDelete(num);
    }

    @Override
    public int summerModify(CodeDTO code) {
        return sdao.summerModify(code);
    }


}


