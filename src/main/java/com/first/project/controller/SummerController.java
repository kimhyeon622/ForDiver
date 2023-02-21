package com.first.project.controller;

import com.first.project.dto.CodeDTO;
import com.first.project.dto.CommentDTO;
import com.first.project.dto.CommentLikeDTO;
import com.first.project.dto.MemberDTO;
import com.first.project.service.SummerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class SummerController {

    private ModelAndView mav = new ModelAndView();

    private final SummerService ssvc;

    //글 작성 페이지로 이동
    @GetMapping("summerWrite")
    public String summerWriteForm(){
        return "SummerWrite";
    }

    //글 작성
    @PostMapping("summerWrite")
    public @ResponseBody String summerWrite(@ModelAttribute CodeDTO code){

        String data = ssvc.summerWrite(code);

        return data;
    }

    //게시글 상세보기 - Cookie 미사용. 사용으로 변경 도전
    @GetMapping("view")
    public ModelAndView summerView(@RequestParam("codeNum") int codeNum,
                                   HttpServletRequest request, HttpServletResponse response){

        //쿠키 배열
        Cookie[] cookies = request.getCookies();

        //비교용 쿠키
        Cookie viewCookie = null;

        //쿠키가 있을 경우
        if(cookies != null && cookies.length > 0){
            for (int i = 0; i < cookies.length; i++){
                //일치하는 쿠키를 비교용 쿠키에 추가
                if (cookies[i].getName().equals("cookie" + codeNum)){
                    System.out.println("일치하는 쿠키 추가");
                    viewCookie = cookies[i];
                }
            }
        }

        //쿠키가 없을 경우
        if(viewCookie == null){
            System.out.println("쿠키 없음");

            //새로운 쿠키
            Cookie newCookie = new Cookie("cookie" + codeNum, "|" + codeNum + "|");

            //쿠키 배열에 새로운 쿠키 추가
            response.addCookie(newCookie);

            //조회수 증가
            ssvc.summerViewCount(codeNum);

            //조회수가 증가된 상태의 페이지 데이터 가져옴
            CodeDTO codeView = ssvc.summerView(codeNum);

            mav.addObject("view",codeView);

        } else {
            //쿠키 확인용
            String value = viewCookie.getValue();

            System.out.println("cookie 값 : " + value);

            //조회수는 증가하지 않고 기존의 페이지 데이터 가져옴
            CodeDTO codeView = ssvc.summerView(codeNum);

            mav.addObject("view",codeView);
        }

        mav.setViewName("SummerView");

        return mav;

    }

    //게시글 삭제
    @GetMapping("delete")
    public ModelAndView summerDelete(@RequestParam("codeNum") int codeNum){

        //댓답글 좋아요 삭제. 댓답글을 부모로 갖는 좋아요도 제거해줘야 함
        //먼저 해당 게시글의 모든 댓답글 불러오고 그 scNum을 받아 삭제해야 함
        List<Integer> scNums = ssvc.realAllComment(codeNum);

        for(int i = 0; i < scNums.size(); i++){

            ssvc.realCommentLikeDelete(scNums.get(i));

        }


        //댓답글 삭제. 게시글에 달린 댓답글이 있으면 부모로 갖기 때문에 먼저 제거해주어야 함
        //이건 찐 삭제라 이전에 만들었던 거랑 다름
        ssvc.commentDeleteForSummerDelete(codeNum);

        //게시글 좋아요 삭제
        //기존 거는 memId를 함께 받아와 삭제했지만 이번에는 모든 회원의 좋아요를 삭제해야 해서 새로 만들어야 함
        ssvc.summerLikeDeleteForSummerDelete(codeNum);

        //게시글 삭제
        int result = ssvc.summerDelete(codeNum);

        if(result > 0){
            mav.setViewName("redirect:/list");
        }

        return mav;
    }

    //게시글 수정 페이지로 이동. 수정할 데이터는 제목, 내용
    @GetMapping("modify")
    public ModelAndView summerModifyForm(@RequestParam("codeNum") int codeNum){

        CodeDTO originalData = ssvc.summerView(codeNum);

        mav.addObject("modify",originalData);
        mav.setViewName("SummerModify");

        return mav;
    }

    //새로운 데이터로 게시글 수정 실행
    @PostMapping("summerModify")
    public @ResponseBody String summerModify(@ModelAttribute CodeDTO code){

        String result = "";

        int check = ssvc.summerModify(code);

        if(check > 0){
            result = "OK";
        } else {
            result = "NO";
        }

        return result;
    }

    //게시글 전체 목록
    @GetMapping("list")
    public ModelAndView summerList(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "12") int size,
                                   @RequestParam(defaultValue = "") String keyword){

        mav = ssvc.summerList(page,size,keyword);

        return mav;
    }

    //현재 오류로 잠시 더미데이터화
    @PostMapping("summerPagingList")
    public @ResponseBody ModelAndView summerPagingList(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "") String keyword){

        mav = ssvc.summerPagingList(page,size,keyword);

        return mav;
    }

    //댓글 작성
    @PostMapping("commentWrite")
    public @ResponseBody String commentWrite(CommentDTO comment){

        String result = "";

        int write =  ssvc.commentWrite(comment);

        if(write > 0){
            result = "OK";
        } else {
            result = "NO";
        }

        return result;
    }

    //댓글 목록 중 parent = 0인 순수 댓글 불러오기
    @PostMapping("commentList")
    public @ResponseBody List<CommentDTO> commentList(@RequestParam("codeNum") int codeNum){

        List<CommentDTO> commentList = ssvc.commentList(codeNum);

        return commentList;
    }

    //해당 게시글의 좋아요 수 세기
    @PostMapping("summerLikeCount")
    public @ResponseBody int summerLikeCount(@RequestParam("codeNum") int codeNum){

        int count = ssvc.summerLikeCount(codeNum);

        return count;
    }

    //로그인한 사용자의 해당 댓글 좋아요 입력 여부 확인. 적재 시 또는 재클릭 시
    @PostMapping("summerLikeCheck")
    public @ResponseBody String summerLikeCheck(@ModelAttribute CodeDTO code){

        String result="";

        CodeDTO check = ssvc.summerLikeCheck(code);

        if(check!=null){
            result = "NO";
        } else {
            result = "OK";
        }

        return result;
    }

    //게시글 좋아요 증가 = 입력. 증가 감소는 잘 되는데 그 다음 처리인 버튼 리메이크가 잘 안된다.
    @PostMapping("summerLikeInsert")
    public @ResponseBody String summerLikeInsert(@ModelAttribute CodeDTO code){
        String response = "";

        int check = ssvc.summerLikeInsert(code);

        if(check > 0){
            response = "OK";
        } else {
            response = "NO";
        }

        return response;
    }

    //게시글 좋아요 감소 = 삭제
    @PostMapping("summerLikeDelete")
    public @ResponseBody String summerLikeDelete(@ModelAttribute CodeDTO code){
        String response = "";

        int check = ssvc.summerLikeDelete(code);

        if(check > 0){
            response = "OK";
        } else {
            response = "NO";
        }

        return response;
    }

    //답글 작성
    @PostMapping("childWrite")
    public @ResponseBody void childWrite(@ModelAttribute CommentDTO comment){
        ssvc.childWrite(comment);
    }

    //답글 불러오기 1차 : 모든 답글들 불러오기
    @PostMapping("childList")
    public @ResponseBody List<CommentDTO> childList(@RequestParam int codeNum){
        List<CommentDTO> childList = ssvc.childList(codeNum);

        return childList;
    }

    //답글 불러오기 2차 : 같은 부모를 가진 답글 collect 하기
    @PostMapping("collectChild")
    public @ResponseBody List<CommentDTO> collectChild(@ModelAttribute CommentDTO comment){

        List<CommentDTO> childs = ssvc.collectChild(comment);

        return childs;
    }

    //댓답글 수정
    @PostMapping("commentModify")
    public @ResponseBody String commentModify(@ModelAttribute CommentDTO comment){
        String result = "";
        int modify = ssvc.commentModify(comment);
        if(modify>0){
            result = "OK";
        } else {
            result = "NO";
        }
        return result;
    }

    //댓답글 삭제
    @PostMapping("commentDelete")
    public @ResponseBody String commentDelete(@RequestParam("scNum") int scNum){
        String result = "";
        int delete = ssvc.commentDelete(scNum);
        if(delete > 0){
            result = "OK";
        } else {
            result = "NO";
        }

        return result;
    }

    //게시글의 모든 댓답글 중 scbool이 1인 것만 불러옴
    @PostMapping("allComment")
    public @ResponseBody List<CommentDTO> allComment(@RequestParam("codeNum") int codeNum){
        List<CommentDTO> comments = ssvc.allComment(codeNum);

        return comments;
    }

    //불러온 scNum으로 검사해서 OK/NO
    //회원의 댓답글 좋아요 여부 확인
    @PostMapping("commentLikeCheck")
    public @ResponseBody String commentLikeCheck(@ModelAttribute CommentLikeDTO commentLike){
        String response = "";

        CommentLikeDTO check = ssvc.commentLikeCheck(commentLike);

        //이미 좋아요가 박혀있다면 NO, 없다면 OK
        if(check!=null){
            response = "NO";
        } else {
            response = "OK";
        }

        return response;
    }

    //댓답글 좋아요 증가
    @PostMapping("commentLikeInsert")
    public @ResponseBody String commentLikeInsert(@ModelAttribute CommentLikeDTO commentLike){
        String response = "";

        int check = ssvc.commentLikeInsert(commentLike);

        if(check > 0){
            response = "OK";
        } else {
            response = "NO";
        }

        return response;
    }

    //댓답글 좋아요 감소
    @PostMapping("commentLikeDelete")
    public @ResponseBody String commentLikeDelete(@ModelAttribute CommentLikeDTO commentLike){
        String response = "";

        int check = ssvc.commentLikeDelete(commentLike);

        if(check > 0){
            response = "OK";
        } else {
            response = "NO";
        }

        return response;
    }

    //댓글 좋아요 수 세기
    @PostMapping("commentLikeCount")
    public @ResponseBody int commentLikeCount(@RequestParam("scNum") int scNum){
        int count = ssvc.commentLikeCount(scNum);
        return count;
    }

    //checkForCommentModifyAndDelete : 댓글 수정과 삭제 위한 해당 댓글 작성자 찾기
    @PostMapping("checkForCommentModifyAndDelete")
    public @ResponseBody String checkForCommentModifyAndDelete(@RequestParam("scNum") int num){

        String theId = ssvc.checkForCommentModifyAndDelete(num);

        return theId;
    }


}
