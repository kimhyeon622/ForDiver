package com.first.project.controller;

import com.first.project.dto.MemberDTO;
import com.first.project.dto.NOTICEDTO;
import com.first.project.dto.QUESTDTO;
import com.first.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MemberController {

    //mav 객체 생성
    ModelAndView mav = new ModelAndView();

    // session 객체 생성
    private final HttpSession session;
    // MemberService 연결
    private final MemberService msvc;

    // mJoinForm : 회원가입 페이지
    @RequestMapping(value = "/mJoinForm", method = RequestMethod.GET)
    public String mJoinForm() {
        return "mJoin";
    }

    // loginForm : 로그인 페이지
    @RequestMapping(value = "/mLoginForm", method = RequestMethod.GET)
    public String mLoginForm() {
        return "mLogin";
    }

//	// mCheckID : ID중복검사
//	@RequestMapping(value = "/mCheckID", method = RequestMethod.GET)
//	public ModelAndView mCheckID(@RequestParam("mId") String mId) {
//		System.out.println("아이디 중복검사를 시작합니다.");
//		System.out.println("[1] jsp->controller\n mId : " + mId);
//		mav = msvc.mCheckID(mId);
//		System.out.println("[6] service->controller\n mav : " + mav);
//		System.out.println(mav);
//		return mav;
//	}


    // idoverlap : 아이디중복체크
    @RequestMapping(value = "/idoverlap", method = RequestMethod.POST)
    public @ResponseBody String idoverlap(@RequestParam("memId") String memId) {
        String result = msvc.idoverlap(memId);
        return result;
    }

    // mJoin : 회원가입
    @RequestMapping(value = "/mJoin", method = RequestMethod.POST)
    public ModelAndView mJoin(@ModelAttribute MemberDTO member) throws IllegalStateException, IOException {

        mav = msvc.mjoin(member);
        return mav;
    }


    //mView : 회원상세보기 - 일반회원
    @GetMapping(value = "mView")
    public ModelAndView mViewGet(@RequestParam("memId") String memId) {

        mav = msvc.mview(memId);

        return mav;
    }

    // mView : 회원상세보기 - 관리자
    @RequestMapping(value = "/mView", method = RequestMethod.POST)
    public ModelAndView mView(@RequestParam(value = "memId", required = false, defaultValue = "none") String memId) {
        //post에서 memId로 아이디를 받되, 받지 않으면 session으로 받는다.
        if(memId.equals("none")){
            memId=String.valueOf(session.getAttribute("loginId"));
        }
        mav = msvc.mview(memId);
        return mav;
    }

    // mLogin : 로그인
    @RequestMapping(value = "/mLogin", method = RequestMethod.POST)
    public ModelAndView mLogin(@ModelAttribute MemberDTO member) {
        mav = msvc.mLogin(member);
        return mav;
    }

    // 로그인 창으로. location.href 위함
    @GetMapping("mLogin")
    public String mLoginGet(){
        return "mLogin";
    }

    // mLogout : 로그아웃
    @RequestMapping(value = "/mLogout", method = RequestMethod.GET)
    public String mLogout() {

        session.invalidate();
        return "index";
    }

    // mDelte : 회원정보삭제
    @RequestMapping(value = "/mDelte", method = RequestMethod.GET)
    public ModelAndView mDelte(@RequestParam("memId") String memId) {
        mav = msvc.mDelete(memId);
        return mav;
    }

    // mlist : 페이징처리 - 목록
    @RequestMapping(value = "/mlist", method = RequestMethod.GET) // 405 뜨면 method를 바꿔보자
    public ModelAndView mlist(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
                              @RequestParam (value = "search", required = false, defaultValue = "") String search,
                              @RequestParam (value = "category", required = false, defaultValue = "memId") String category) {
        mav = msvc.m_plist(page, limit, search,category);
        return mav;
    }

    // m_insertmember : 회원 72명 만들기
    @RequestMapping(value = "/m_insertmember", method = RequestMethod.GET) // 405 뜨면 method를 바꿔보자
    public ModelAndView m_insertmember() {
        mav = msvc.m_insertmember();
        return mav;
    }

    // mPwFindEmail : 로그인창에서 비밀번호 찾기를 위한 이메일 인증페이지로 이동
    @RequestMapping(value = "/mPwFindEmail", method = RequestMethod.GET)
    public String mPwFindEmail(){
        return "mPwFindEmail";
    }

    // mPwModifyEmail : 마이페이지에서 비밀번호 변경을 위한 이메일 인증페이지로 이동
    @RequestMapping(value = "/mPwModifyEmail", method = RequestMethod.POST)
    public ModelAndView mPwModifyEmail(){
        String memId2 = String.valueOf(session.getAttribute("loginId"));
        mav = msvc.mPwModifyEmail(memId2);
        return mav;
    }

    // mEmailCheck : 이메일인증
    @RequestMapping(value = "/mEmailCheck", method = RequestMethod.POST)
    public @ResponseBody String mEmailCheck(@RequestParam("memEmail") String memEmail) {
        System.out.println("[1] 메일인증 controller"+memEmail);
        String uuid = msvc.mCheckEmail(memEmail);
        System.out.println("[3] 메일인증 controller"+memEmail);
        return uuid;
    }


    //mFindEmail : 아이디를 받아 이메일을 찾은 뒤, 해당 이메일로 인증번호를 전송하고, 인증번호를 ajax로 return하는 메소드
    @RequestMapping(value = "/mFindEmail", method = RequestMethod.POST)
    public @ResponseBody String mFindEmail(@RequestParam("memId") String memId) {
        System.out.println("[1] 메일인증 controller");
        String uuid = msvc.mFindEmail(memId);
        System.out.println("[3] 메일인증 controller");
        return uuid;
    }

    //mFindEmailrestore : 아이디를 받아, 비회원 아이디인지 확인하고 맞다면 이메일 전송, 회원이면 '일반회원', 블랙리스트면 '블랙리스트'
    @RequestMapping(value = "/mFindEmailrestore", method = RequestMethod.POST)
    public @ResponseBody String mFindEmailrestore(@RequestParam("memId") String memId,HttpServletResponse response) {
        Cookie cookie = new Cookie("restore", null);
        cookie.setMaxAge(0); // 유효시간을 0으로 설정해서 바로 만료시킨다.
        response.addCookie(cookie); // 응답에 추가해서 없어지도록 함
        cookie = new Cookie("restore","438afew4");
        cookie.setDomain("localhost");
        cookie.setPath("/");
        // 30초간 저장
        cookie.setMaxAge(30);
        cookie.setSecure(true);
        response.addCookie(cookie);

        String uuid = msvc.mFindEmailrestore(memId);

        return uuid;
    }


    //mPwFindForm : 인증 후 비번 찾는 페이지로 이동
    @RequestMapping(value = "/mPwFindForm", method = RequestMethod.POST)
    public ModelAndView mPwFindForm(@RequestParam("memId") String memId ) {
        mav=msvc.mPwFindForm(memId);
        return mav;

    }

    // mPwFindfini  sh : 비번 수정하고 로그인페이지로 이동
    @RequestMapping(value = "/mPwFindfinish", method = RequestMethod.POST)
    public ModelAndView mPwFindfinish(
            @RequestParam("memId") String memId,
            @RequestParam("memPw") String memPw ) {
        return msvc.mPwFindfinish(memId,memPw);
    }




    //mRestoreForm : 탈퇴한 회원의 복구 신청 페이지로 이동
    @RequestMapping(value = "/mRestoreForm", method = RequestMethod.POST)
    public String mRestoreForm() {
        return "mRestore";
    }

    //mRestoresend : 복구 신청하기
    @RequestMapping(value = "/mRestoresend", method = RequestMethod.POST)
    public ModelAndView mRestoresend(
            @RequestParam (value = "memId") String memId,
            @RequestParam (value = "RESREASON") String RESREASON) {
        return msvc.mRestoresend(memId,RESREASON);
    }

    //mRestroreList : 회원 복구 신청 목록
    @RequestMapping(value = "/mRestroreList", method = RequestMethod.GET)
    public ModelAndView mRestroreList(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
            @RequestParam (value = "search", required = false, defaultValue = "") String search,
            @RequestParam (value = "category", required = false, defaultValue = "memId") String category
    ) {
        return msvc.mRestroreList(page,limit,search,category);
    }

    //mRestore : 관리자에 의한 회원 복구 신청 실행 및 처리 후 결과에 따라 회원 복수 신청 실패 또는 성공 페이지로 이동
    @RequestMapping(value = "/mRestore", method = RequestMethod.POST)
    public ModelAndView mRestore(@RequestParam("memId") String memId,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
                                 @RequestParam (value = "search", required = false, defaultValue = "") String search,
                                 @RequestParam (value = "category", required = false, defaultValue = "memId") String category) {
        return msvc.mRestore(memId,page,limit,search,category);
    }


    //mModifyPwCheck : 회원정보 수정 전 비밀번호 확인창으로 이동하기
    @RequestMapping(value = "/mModifyPwCheck", method = RequestMethod.POST)
    public String mModifyPwCheck() {
        String memId = String.valueOf(session.getAttribute("loginId"));
        return "mModifyPwCheck";
    }

    // mPwcheck : 비번 확인
    @RequestMapping(value = "/mPwcheck", method = RequestMethod.POST)
    public @ResponseBody String  mPwcheck(@RequestParam("memPw") String memPw) {
        String memId = String.valueOf(session.getAttribute("loginId"));
        return msvc.mPwcheck(memId,memPw);
    }

    // mModifyForm : 비번을 입력받고, 제대로 입력시 회원정보 수정 페이지로 이동하기
    @RequestMapping(value = "/mModifyForm", method = RequestMethod.POST)
    public ModelAndView mModifyForm(@RequestParam("memPw") String memPw) {
        String memId = String.valueOf(session.getAttribute("loginId"));
        return msvc.modiForm(memId,memPw);
    }

    // mModify : 회원정보 수정하기, 성공시 view로
    @RequestMapping(value = "/mModify", method = RequestMethod.POST)
    public ModelAndView mModify(@ModelAttribute MemberDTO member,
                                @RequestParam("memBirth2") String memBirth2,
                                @RequestParam("memAddrcopy") String memAddrcopy
    ) throws IllegalStateException, IOException {
        mav = msvc.mModify(member,memBirth2,memAddrcopy);
        return mav;
    }

    // mOutInfo : 회원탈퇴 안내 페이지로 이동
    @RequestMapping(value = "/mOutInfo", method = RequestMethod.POST)
    public String mOutInfo(){
        return "mOutInfo";
    }

    // mOutPwCheck : 회원 탈퇴를 위한 비밀번호 확인 페이지
    @RequestMapping(value = "/mOutPwCheck", method = RequestMethod.POST)
    public String mOutPwCheck(){
        return "mOutPwCheck";
    }

    // mOut : Update 통해 탈퇴한 회원으로 처리. MEMIS = 0
    @RequestMapping(value = "/mOut", method = RequestMethod.POST)
    public ModelAndView mOut(@RequestParam("memPw") String memPw) {
        String memId = String.valueOf(session.getAttribute("loginId"));
        return msvc.mOut(memId,memPw);
    }

    //mAddBlackList : ajax로 Update 통해 블랙리스트 회원으로 처리. MEMIS = 2
    @RequestMapping(value = "/mAddBlackList", method = RequestMethod.POST)
    public @ResponseBody String mAddBlackList(@RequestParam("memId") String memId) {
        return msvc.mAddBlackList(memId);
    }

    //myQuestList : 내 문의내역
    @RequestMapping(value = "/myQuestList", method = RequestMethod.GET)
    public ModelAndView myQuestList(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
            @RequestParam (value = "search", required = false, defaultValue = "") String search,
            @RequestParam (value = "category", required = false, defaultValue = "QUCONTENT") String category) {
        String memId = String.valueOf(session.getAttribute("loginId"));
        System.out.println(memId);
        return msvc.myQuestList(memId,page,limit,search,category);
    }

    //mQuestList : 전체 회원 문의 내역
    @RequestMapping(value = "/mQuestList", method = RequestMethod.GET)
    public ModelAndView mQuestList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                   @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
                                   @RequestParam (value = "search", required = false, defaultValue = "") String search,
                                   @RequestParam (value = "category", required = false, defaultValue = "MEMID") String category) {
        return msvc.mQuestList(page,limit,search,category);
    }

    // mQuestView : 회원 문의 내역 상세보기
    @RequestMapping(value = "/mQuestView", method = RequestMethod.GET)
    public ModelAndView mQuestView(@RequestParam("QUNUM") int QUNUM,
                                   @RequestParam("beforepage") int beforepage) {
        System.out.println(QUNUM);
        System.out.println(beforepage);
        mav = msvc.mQuestView(QUNUM,beforepage);
        return mav;
    }
    //mQuestWriteForm : 회원 문의 작성 페이지로
    @RequestMapping(value = "/mQuestWriteForm", method = RequestMethod.GET)
    public String mQuestWriteForm() {return "mQuestWriteForm";
    }

    // mQuest : 회원 문의 작성
    @RequestMapping(value = "/mQuest", method = RequestMethod.POST)
    public ModelAndView mQuest(@ModelAttribute QUESTDTO quest) {
        mav = msvc.mQuest(quest);
        return mav;
    }

    //mAnswer : ajax로 문의에 대해 관리자가 답변
    @RequestMapping(value = "/mAnswer", method = RequestMethod.POST)
    public ModelAndView mAnswer(@ModelAttribute QUESTDTO quest) {
        return msvc.mAnswer(quest);
    }

    //mNoticeList : 공지사항 목록
    @RequestMapping(value = "/mNoticeList", method = RequestMethod.GET) // 405 뜨면 method를 바꿔보자
    public ModelAndView mNoticeList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                    @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
                                    @RequestParam (value = "search", required = false, defaultValue = "") String search,
                                    @RequestParam (value = "category", required = false, defaultValue = "NOTITLE") String category) {
        mav = msvc.mNoticeList(page, limit, search,category);
        return mav;
    }

    //mNoticeView : 공지 보기
    @RequestMapping(value = "/mNoticeView", method = RequestMethod.GET) // 405 뜨면 method를 바꿔보자
    public ModelAndView mNoticeView(@RequestParam(value = "NONUM") int NONUM) {
        mav = msvc.mNoticeView(NONUM);
        return mav;
    }


    //mNoticeWriteForm : 공지사항 작성 페이지로
    @RequestMapping(value = "/mNoticeWriteForm", method = RequestMethod.GET) // 405 뜨면 method를 바꿔보자
    public String mNoticeWriteForm() {
        return "mNoticeWrite";
    }

    //mNoticeWrite : 공지사항 작성
    @PostMapping("mNoticeWrite")
    public @ResponseBody String mNoticeWrite(@ModelAttribute NOTICEDTO notice){
        System.out.println("공지사항 작성 시작하기");
        return msvc.mNoticeWrite(notice);
    }

    //mNoticeModifyForm : 공지사항 수정 페이지로
    @RequestMapping(value = "/mNoticeModifyForm", method = RequestMethod.POST) // 405 뜨면 method를 바꿔보자
    public ModelAndView mNoticeModifyForm(@RequestParam(value = "nonum") int nonum) {
        mav = msvc.mNoticeModifyForm(nonum);

        return mav;
    }

    //mNoticeModify : 공지사항 수정
    @RequestMapping(value = "/mNoticeModify", method = RequestMethod.POST) // 405 뜨면 method를 바꿔보자
    public ModelAndView mNoticeModify(@ModelAttribute NOTICEDTO notice) {
        mav = msvc.mNoticeModify(notice);
        return mav;
    }

    //mNoticeDelete : 공지사항 삭제
    @RequestMapping(value = "/mNoticeDelete", method = RequestMethod.POST) // 405 뜨면 method를 바꿔보자
    public ModelAndView mNoticeDelete(@RequestParam(value = "NONUM") int NONUM) {
        mav = msvc.mNoticeDelete(NONUM);
        return mav;
    }

    //mSendAllForm : 공지 메일 작성 페이지
    @RequestMapping(value = "/mSendAllForm", method = RequestMethod.GET) // 405 뜨면 method를 바꿔보자
    public String mSendAllForm() {
        return "mSend";
    }


    // mSendAll : 모든 회원에게 공지 메일 전송
    @RequestMapping(value = "/mSendAll", method = RequestMethod.POST)
    public @ResponseBody String mSendAll(@RequestParam(value = "mailtitle") String mailtitle,
                                         @RequestParam(value = "mailcontent") String mailcontent) {
        String result = msvc.mSendAll(mailtitle,mailcontent);
        return result;
    }

    //myBuyList : 구매 내역, Get 방식
    @RequestMapping(value = "/myBuyList", method = RequestMethod.GET) // 405 뜨면 method를 바꿔보자
    public ModelAndView myBuyList(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
            @RequestParam (value = "search", required = false, defaultValue = "") String search,
            @RequestParam (value = "category", required = false, defaultValue = "memId") String category,
            @RequestParam (value = "stringkeyword1", required = false, defaultValue = "") String stringkeyword1) {
        String memId = String.valueOf(session.getAttribute("loginId"));
        mav = msvc.myBuyList(memId,page,limit,search,category,stringkeyword1);
        return mav;

    }

    //myBuyList : 구매 내역, Post 방식
    @RequestMapping(value = "/myBuyList", method = RequestMethod.POST) // 405 뜨면 method를 바꿔보자
    public ModelAndView myBuyList2(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
            @RequestParam (value = "search", required = false, defaultValue = "") String search,
            @RequestParam (value = "category", required = false, defaultValue = "memId") String category,
            @RequestParam (value = "stringkeyword1", required = false, defaultValue = "") String stringkeyword1) {
        String memId = String.valueOf(session.getAttribute("loginId"));
        mav = msvc.myBuyList(memId,page,limit,search,category,stringkeyword1);
        return mav;
    }

    // mLogin : 로그인
    @RequestMapping(value = "/popUpmLogin", method = RequestMethod.POST)
    public ModelAndView popUpmLogin(@ModelAttribute MemberDTO member) {
        mav = msvc.popUpmLogin(member);
        return mav;
    }

    // logintest : 팝업창에서 로그인시 다시 전페이지로 되돌려보내기 위한 중간 페이지
    @GetMapping("/logintest")
    public String logintest(){
        return "logintest";
    }

}
