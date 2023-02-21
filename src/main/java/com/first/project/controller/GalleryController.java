package com.first.project.controller;

import com.first.project.dto.*;
import com.first.project.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GalleryController {

    private ModelAndView mav = new ModelAndView();

    private final GalleryService gsvc;

    //미디어 포토 갤러리 게시글 작성 페이지로 이동
    @GetMapping("galleryWrite")
    public String galleryWriteForm(){
        return "GalleryWrite";
    }

    //미디어 포토 갤러리 게시글 작성
    //먼저 게시글 테이블에 게시글을 저장한 뒤 해당 게시글 번호를 가지고 옴.
    //가져온 게시글 번호를 set 하고 파일을 하나씩 업로드 및 데이터 베이스에 파일 이름으로 저장
    //모두 끝나면 게시글 목록으로 이동
    @PostMapping("galleryWrite")
    public ModelAndView galleryWrite(@ModelAttribute GalleryDTO gallery) throws IOException {

        //게시글 등록 - 먼저 썸네일부터 처리. 서비스에서 해주자
        int result1 = gsvc.galleryWrite1(gallery);

        //게시글 등록 성공시 해당 번호 가져오고 게시글 번호 SET
        if(result1 > 0){

            int num = gsvc.getPnum(gallery);

            gallery.setPNum(num);

        } else {
            System.out.println("게시글 등록 실패");
        }

        //파일 등록
        gsvc.galleryWrite2(gallery);

        //파일 등록 끝나면 갤러리 게시판으로 이동
        mav.setViewName("redirect:/galleryList");

        return mav;
    }

    //글 상세보기 페이지
    //조회수가 현재 테이블에 없어 제거함. 대신 목록에서 좋아요로 해결 + 좋아요 상위권 게시글 4개는 모든 페이지 상단에 따로 보이도록
    @GetMapping("galleryView")
    public String galleryView(@RequestParam("pNum") int pNum,
                              Model model){

        //게시글 불러오기
        GalleryDTO galleryView = gsvc.galleryView(pNum);

        //게시글의 이미지와 영상 불러오기
        List<GalleryDTO> galleryPhotos = gsvc.galleryDatas(pNum);

        //둘 다 모델에 담음
        model.addAttribute("view",galleryView);
        model.addAttribute("photo", galleryPhotos);

        return "galleryView";
    }

    //게시글 삭제
    //시간 나면 해당 게시글의 파일들도 전부 삭제(이 경우 부모 테이블보다 먼저 자식 테이블부터 처리해줘야 함
    //파일 삭제 → 게시글 파일 테이블에서 데이터 삭제 → 게시글 삭제
    @GetMapping("galleryDelete")
    public ModelAndView galleryDelete(@RequestParam("pNum") int pNum){

        //댓답글 좋아요 삭제. 댓답글을 부모로 갖는 좋아요도 제거해줘야 함
        //먼저 해당 게시글의 모든 댓답글 불러오고 그 pcmNum을 받아 삭제해야 함
        List<Integer> pcmNums = gsvc.realAllComment(pNum);

        for(int i = 0; i < pcmNums.size(); i++){

            gsvc.realCommentLikeDelete(pcmNums.get(i));
        }


        //댓답글 삭제. 게시글에 달린 댓답글이 있으면 부모로 갖기 때문에 먼저 제거해주어야 함
        //이건 찐 삭제라 이전에 만들었던 거랑 다름
        gsvc.commentDeleteForGalleryDelete(pNum);

        //게시글 좋아요 삭제
        //기존 거는 memId를 함께 받아와 삭제했지만 이번에는 모든 회원의 좋아요를 삭제해야 해서 새로 만들어야 함
        gsvc.galleryLikeDeleteForGalleryDelete(pNum);

        //게시글 파일 삭제
        //게시글 번호로 pContent 조회 및 삭제
        List<String> pContents = gsvc.collectContents(pNum);

        for(int i = 0; i < pContents.size(); i++){

            gsvc.contentNameDelete(pContents.get(i));

            //시간 나면 폴더에서도 삭제

        }

        //게시글 삭제
        int result = gsvc.galleryDelete(pNum);

        if(result > 0){
            mav.setViewName("redirect:/galleryList");
        }

        return mav;
    }

    //게시글 전체 목록
    @GetMapping("galleryList")
    public ModelAndView galleryList(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "12") int size,
                                    @RequestParam(defaultValue = "") String keyword){

        mav = gsvc.galleryList(page,size,keyword);

        return mav;
    }

    //Ajax를 이용한 버전. 다만 ModelAndView인 걸 보아하니 제대로 된 건 아님 ㅋㅋ
    @PostMapping("galleryPagingList")
    public @ResponseBody ModelAndView summerPagingList(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "") String keyword){

        mav = gsvc.galleryPagingList(page,size,keyword);

        return mav;
    }

    //해당 게시글의 좋아요 수 세기
    @PostMapping("galleryLikeCount")
    public @ResponseBody int galleryLikeCount(@RequestParam("pNum") int pNum){

        int count = gsvc.galleryLikeCount(pNum);

        return count;
    }

    //로그인한 사용자의 해당 댓글 좋아요 입력 여부 확인. 적재 시 또는 재클릭 시
    @PostMapping("galleryLikeCheck")
    public @ResponseBody String galleryLikeCheck(@ModelAttribute GalleryDTO gallery){

        String result="";

        GalleryDTO check = gsvc.galleryLikeCheck(gallery);

        if(check!=null){
            result = "NO";
        } else {
            result = "OK";
        }

        return result;
    }

    //게시글 좋아요 증가 = 입력. 증가 감소는 잘 되는데 그 다음 처리인 버튼 리메이크가 잘 안된다.
    @PostMapping("galleryLikeInsert")
    public @ResponseBody String galleryLikeInsert(@ModelAttribute GalleryDTO gallery){
        String response = "";

        int check = gsvc.galleryLikeInsert(gallery);

        if(check > 0){
            response = "OK";
        } else {
            response = "NO";
        }

        return response;
    }

    //게시글 좋아요 감소 = 삭제
    @PostMapping("galleryLikeDelete")
    public @ResponseBody String galleryLikeDelete(@ModelAttribute GalleryDTO gallery){
        String response = "";

        int check = gsvc.galleryLikeDelete(gallery);

        if(check > 0){
            response = "OK";
        } else {
            response = "NO";
        }

        return response;
    }

    //댓글 작성
    @PostMapping("pcommentWrite")
    public @ResponseBody String commentWrite(PCommentDTO comment){

        String result = "";

        int write =  gsvc.commentWrite(comment);

        if(write > 0){
            result = "OK";
        } else {
            result = "NO";
        }

        return result;
    }

    //댓글 목록 중 parent = 0인 순수 댓글 불러오기
    @PostMapping("pcommentList")
    public @ResponseBody List<PCommentDTO> pcommentList(@RequestParam("pNum") int pNum){

        List<PCommentDTO> commentList = gsvc.commentList(pNum);

        return commentList;
    }

    //답글 작성
    @PostMapping("pchildWrite")
    public @ResponseBody void pchildWrite(@ModelAttribute PCommentDTO comment){
        gsvc.childWrite(comment);
    }

    //답글 불러오기 1차 : 모든 답글들 불러오기
    @PostMapping("pchildList")
    public @ResponseBody List<PCommentDTO> childList(@RequestParam("pNum") int pNum){

        List<PCommentDTO> childList = gsvc.childList(pNum);

        return childList;
    }

    //답글 불러오기 2차 : 같은 부모를 가진 답글 collect 하기
    @PostMapping("pcollectChild")
    public @ResponseBody List<PCommentDTO> collectChild(@ModelAttribute PCommentDTO comment){

        List<PCommentDTO> childs = gsvc.collectChild(comment);

        return childs;
    }

    //댓답글 수정
    @PostMapping("pcommentModify")
    public @ResponseBody String commentModify(@ModelAttribute PCommentDTO comment){
        String result = "";
        int modify = gsvc.commentModify(comment);
        if(modify>0){
            result = "OK";
        } else {
            result = "NO";
        }
        return result;
    }

    //댓답글 삭제
    @PostMapping("pcommentDelete")
    public @ResponseBody String commentDelete(@RequestParam("pcmNum") int pcmNum){
        String result = "";
        int delete = gsvc.commentDelete(pcmNum);
        if(delete > 0){
            result = "OK";
        } else {
            result = "NO";
        }

        return result;
    }

    //게시글의 모든 댓답글 중 scbool이 1인 것만 불러옴
    @PostMapping("pallComment")
    public @ResponseBody List<PCommentDTO> allComment(@RequestParam("pNum") int pNum){
        List<PCommentDTO> comments = gsvc.allComment(pNum);

        return comments;
    }

    //불러온 pcmNum으로 검사해서 OK/NO
    //회원의 댓답글 좋아요 여부 확인
    @PostMapping("pcommentLikeCheck")
    public @ResponseBody String commentLikeCheck(@ModelAttribute PCommentLikeDTO commentLike){
        String response = "";

        PCommentLikeDTO check = gsvc.commentLikeCheck(commentLike);

        //이미 좋아요가 박혀있다면 NO, 없다면 OK
        if(check!=null){
            response = "NO";
        } else {
            response = "OK";
        }

        return response;
    }

    //댓답글 좋아요 증가
    @PostMapping("pcommentLikeInsert")
    public @ResponseBody String commentLikeInsert(@ModelAttribute PCommentLikeDTO commentLike){
        String response = "";

        int check = gsvc.commentLikeInsert(commentLike);

        if(check > 0){
            response = "OK";
        } else {
            response = "NO";
        }

        return response;
    }

    //댓답글 좋아요 감소
    @PostMapping("pcommentLikeDelete")
    public @ResponseBody String commentLikeDelete(@ModelAttribute PCommentLikeDTO commentLike){
        String response = "";

        int check = gsvc.commentLikeDelete(commentLike);

        if(check > 0){
            response = "OK";
        } else {
            response = "NO";
        }

        return response;
    }

    @PostMapping("pcommentLikeCount")
    public @ResponseBody int commentLikeCount(@RequestParam("pcmNum") int pcmNum){
        int count = gsvc.commentLikeCount(pcmNum);
        return count;
    }

    //pcheckForCommentModifyAndDelete : 댓답글 좋아요 수정과 삭제를 위한 작성자 아이디 조회
    @PostMapping("pcheckForCommentModifyAndDelete")
    public @ResponseBody String checkForCommentModifyAndDelete(@RequestParam("pcmNum") int num){

        String theId = gsvc.checkForCommentModifyAndDelete(num);

        return theId;
    }
}
