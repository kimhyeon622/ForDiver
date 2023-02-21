package com.first.project.service;

import com.first.project.dao.MemberDAO;
import com.first.project.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class MemberServicelmpl implements MemberService {
    // [1] ModelAndView 객체 생성
    private ModelAndView mav = new ModelAndView();

    // [2] DAO(Repository) 연결
    private final MemberDAO mdao;

    //세션 연결
    private final HttpSession session;

    private final HttpServletRequest request;

    // 암호화를 위한 객체
    private final PasswordEncoder pwEnc;

    private final JavaMailSender mailSender;

    // 회원가입 메소드
    @Override
    public ModelAndView mjoin(MemberDTO member) throws IllegalStateException, IOException {

        // 1. 파일 불러오기
        MultipartFile profile = member.getMemProfile();
        // 2. 파일 선택여부 확인
        if (profile != null &&!profile.isEmpty()) {
            // 3. 파일 저장 위치 설정(상대경로)
            Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/profile");

            // 4. 파일 이름 불러오기
            String originalFileName = profile.getOriginalFilename();

            // 5. 난수 생성하기
            String uuid = UUID.randomUUID().toString().substring(0, 8);

            // 6. 업로드 할 파일이름 생성하기(3번 +언더바(_) + 2번)
            String profileName = uuid + "_" + originalFileName;
            String savePath = path + "/" + profileName;

            // 7. 파일 선택시 MemberDTO member객체의 profileName필드에 업로드파일 이름 저장
            member.setMemPROFILENAME(profileName);

            // 8. 게시글 등록 성공시 파일업로드
            profile.transferTo(new File(savePath));
        }

        // (3) 주소 결합
        member.setMemAddr("(" + member.getMemAddr1() + ")" + member.getMemAddr2() + ", " + member.getMemAddr3());
        // (4) 비밀번호 암호화
        member.setMemPw(pwEnc.encode(member.getMemPw()));
        // (5) 입력
        int result = mdao.mJoin(member);

        // (6) 이동
        if (result > 0) {
            mav.setViewName("mLogin");
        } else {
            mav.setViewName("mJoin");
        }
        return mav;
    }

    // 회원목록 메소드
    public ModelAndView mlist() {
        List<MemberDTO> memberlist = mdao.mlist();

        // mav : Model And View
        // Model : Object
        // View : jsp

        if (!memberlist.isEmpty()) {
            mav.setViewName("M_List");
            mav.addObject("memberList", memberlist);
        } else {
            mav.setViewName("index");
        }
        return mav;
    }

    // 검색
    public ModelAndView mview(String mId) {
        MemberDTO memberview = mdao.mview(mId);

        // mav : Model And View
        // Model : Object
        // View : jsp
        memberview.setMemBirth(memberview.getMemBirth().substring(0, 10));
        mav.addObject("member", memberview);
        mav.setViewName("mView");


        // mav객체 안에 Mview.jsp에서 사용할 member의 이름을 "view"라고 선언해서 저장한다.
        return mav;
    }

    // 로그인
    public ModelAndView mLogin(MemberDTO member) {

        // (1) 비밀번호 암호화
        // (2) 로그인
        MemberDTO memberload = mdao.mLogin(member);
        memberload.setMemBirth(memberload.getMemBirth().substring(0, 10));
        if(memberload !=null){
            // 로그인 성공
            if (pwEnc.matches(member.getMemPw(), memberload.getMemPw())) {
                if(memberload.getMemIs().equals("1")){
                    String savePath = "C:/Users/user/Documents/workspace-sts-3.9.18.RELEASE/MEMBOARD/src/main/webapp/resources/profile/";
                    session.setAttribute("loginId", memberload.getMemId());
                    session.setAttribute("loginm", memberload);
                    mav.setViewName("index");
                }else if(memberload.getMemIs().equals("0")){
                    mav.setViewName("mOutLogin");
                }else{
                    mav.setViewName("mOutblackLogin");
                }

            }
            // 로그인실패
            else {
                //나중에 수정
                mav.setViewName("mLogin");
                mav.addObject("doing_loginID", member.getMemId());
                mav.addObject("reasonforerror", "아이디나 비밀번호가 올바르지 않습니다.");
            }
        }
        else{//아이디 없음
            mav.setViewName("mLogin");
            mav.addObject("doing_loginID", member.getMemId());
            mav.addObject("reasonforerror", "아이디나 비밀번호가 올바르지 않습니다.");
        }

        // mav객체 안에 Mview.jsp에서 사용할 member의 이름을 "view"라고 선언해서 저장한다.
        return mav;
    }


    // 회원삭제
    public ModelAndView mDelete(String mId) {
        int result = mdao.mDelete(mId);

        if (result > 0) {
            mav.setViewName("redirect:mLogout");
        } else {
            mav.setViewName("mView");
        }
        return mav;
    }




    // 아이디 중복검사 + ajax
    public String idoverlap(String memId) {

        String result = mdao.mCheckID(memId);

        if (result == null) {
            // 아이디 사용가능
            return "OK";
        } else {
            // 이미 사용중인 아이디
            return "NO";
        }
    }

    // 회원리스트_페이징+검색
    public ModelAndView m_plist(int page, int limit, String search, String category) {
        // 페이징 객체 생성
        PageDTO paging = new PageDTO();
        paging.setSearch(search);
        paging.setCategory(category);
        // 한 화면에 보여줄 페이지 번호 갯수
        int block = 5;

        int bCount = mdao.mCount(paging);
        // 전체 게시글 갯수를 DB에서 가져온다.
        System.out.println("검새된 회원개수 : "+bCount);
        // ----------여기부터는 bCount를 사용합니다.---------

        // 최대 게시글 갯수
        int maxPage = (int) (Math.ceil((double) bCount / limit));
        if(maxPage<1){
            maxPage=1;
        }
        // page 검사
        if (page > maxPage) {
            page = maxPage;
        }

        // ----------여기부터는 page를 사용합니다.---------

        // 표시될 게시글 묶음
        int startRow = (page - 1) * limit + 1;
        int endRow = page * limit;
        // 표시될 페이지 묶음
        int startPage = (((int) (Math.ceil((double) page / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        // endPage 검사
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        // ----------여기부터는 startRow, endRow, startPage, endPage를 사용합니다.---------


        paging.setPage(page);
        paging.setStartRow(startRow);
        paging.setEndRow(endRow);
        paging.setMaxPage(maxPage);
        paging.setStartPage(startPage);
        paging.setEndPage(endPage);
        paging.setLimit(limit);

        // 검색
        List<MemberDTO> pagingList = mdao.m_pList(paging);
        System.out.println("검새된 회원 : "+pagingList);
        // MODEL
        mav.addObject("pagingList", pagingList);
        mav.addObject("paging", paging);
        // VIEW
        mav.setViewName("mList");
        return mav;
    }

    // m_insertmember : 회원 72명 만들기
    public ModelAndView m_insertmember() {

        for (int j = 73; j < 180; j++) {
            String i = "" + j;
            if (j < 10) {
                i = "0" + i;
            }
            if (j < 100) {
                i = "0" + i;
            }
            if (j < 1000) {
                i = "0" + i;
            }
            MemberDTO member = new MemberDTO();
            member.setMemId("member" + i);
            member.setMemPw("member" + i + "!");
            member.setMemName("멤버" + i);
            member.setMemBirth("2022-01-01");
            member.setMemGender("남");
            member.setMemEmail("testforemail01@gmail.com");
            member.setMemPhone("010-0000-0000");
            member.setMemAddr1("07233");
            member.setMemAddr2("서울 영등포구 의사당대로 1");
            member.setMemAddr3("사무실");
            member.setMemPROFILENAME("");


            // (3) 주소 결합
            member.setMemAddr("(" + member.getMemAddr1() + ")" + member.getMemAddr2() + ", " + member.getMemAddr3());
            // (4) 비밀번호 암호화
            member.setMemPw(pwEnc.encode(member.getMemPw()));
            // (5) 입력
            int result = mdao.mJoin(member);
        }
        mav.setViewName("index");
        return mav;
    }

    public String mCheckEmail(String memEmail) {
        System.out.println("[2] 메일인증 service");
        String uuid = UUID.randomUUID().toString().substring(0, 6);

        //메일 보내기
        MimeMessage mail = mailSender.createMimeMessage();
        String mailContent = "<h2>안녕하세요. 인천일보 아카데미입니다.</h2><br/>"
                + "<h3>인증번호는 " + uuid + "입니다.</h3>";
        try {
            mail.setSubject("[이메일 인증] 인천일보 아카데미 이메일 인증", "UTF-8");
            mail.setText(mailContent, "UTF-8", "html");
            mail.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(memEmail));
            mailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();

        }
        System.out.println("메일 전송");
        return uuid;
    }

    //아이디를 받아 이메일을 찾은 뒤, 해당 이메일로 인증번호를 전송하고, 인증번호를 ajax로 return하는 메소드
    @Override
    public String mFindEmail(String memId) {
        //memId를 사용해서 email주소를 알아낸다.
        String findEmail = mdao.msearchEmail(memId);

        //Email에 인증번호를 보내고, 해당 인증번호를 uuid에 저장한다.
        String uuid = mCheckEmail(findEmail);

        //인증번호를 return한다. (ajax)
        return uuid;
    }

    //mPwModifyEmail : 비밀번호 변경을 위한 이메일 인증페이지로 이동
    @Override
    public ModelAndView mPwModifyEmail(String memId){
        String uuid=mFindEmail(memId);
        mav.addObject("uuid", uuid);
        System.out.println(uuid);
        mav.setViewName("mPwModifyEmail");
        return mav;
    }

    //이메일 인증이 끝나면 비번 바꾸기 창으로 가는 메소드
    @Override
    public ModelAndView mPwFindForm(String memId) {
        mav.addObject("memId", memId);
        mav.setViewName("mPwFindForm");
        return mav;
    }

    @Override
    public String mPwcheck(String memId, String memPw) {

        MemberDTO member = new MemberDTO();
        member.setMemId(memId);
        member.setMemPw(memPw);

        String encPw = mdao.mLogin(member).getMemPw();

        // 비번 인증 성공
        if (pwEnc.matches(member.getMemPw(), encPw)) {
            return "OK";
        }
        // 비번 인증 실패
        else{
            return "NO";
        }
    }


    //공지 상세보기
    @Override
    public ModelAndView mNoticeView(int nonum) {
        System.out.println("nonum : "+nonum);
        NOTICEDTO noview = mdao.mNoticeView(nonum);

        if(noview!=null){
            System.out.println("공지 상세보기");
            mav.addObject("noview", noview);
            mav.setViewName("mNoticeView");
        }
        return mav;
    }

    //공지 수정
    @Override
    public ModelAndView mNoticeModifyForm(int nonum) {
        System.out.println("nonum : "+nonum);
        NOTICEDTO noview = mdao.mNoticeView(nonum);

        if(noview!=null){
            System.out.println("공지 수정하기");
            mav.addObject("noview", noview);
            mav.setViewName("mNoticeModify");
        }
        return mav;
    }

    @Override
    public ModelAndView mRestoresend(String memId, String RESREASON) {
        RESTOREDTO res = new RESTOREDTO();
        res.setMEMID(memId);
        res.setRESREASON(RESREASON);
        int result = mdao.mRestoresend(res);

        if (result > 0) {

            mav.setViewName("index");
        } else {
            mav.setViewName("mRestoreForm");
        }
        return mav;
    }


    // mPwFindfinish : 비번 수정하고 로그인페이지로 이동
    @Override
    public ModelAndView mPwFindfinish(String memId,String memPw) {
        System.out.println("로그인 페이지로");
        MemberDTO member = new MemberDTO();
        member.setMemId(memId);

        //비밀번호 암호화
        member.setMemPw(pwEnc.encode(memPw));

        int result=mdao.mModifyPassword(member);

        // 이동
        if (result > 0) {//성공
            session.invalidate();
            mav.setViewName("mLogin");
        } else {//실패
            mav.setViewName("mPwFindForm");
        }
        return mav;
    }

    @Override
    public String mFindEmailrestore(String memId) {

        //memId에 대한 정보를 가져온다.
        MemberDTO member = mdao.mview(memId);

        //찾은 아이디가 탈퇴한 회원이 맞다면
        if(member.getMemIs().equals("0")){

            //Email에 인증번호를 보내고, 해당 인증번호를 uuid에 저장한다.
            String uuid = mCheckEmail(member.getMemEmail());

            //인증번호를 return한다. (ajax)
            return uuid;
        }
        //일반 회원이라면
        else if(member.getMemIs().equals("1")) {
            return "일반회원";
        }
        //블랙리스트라면
        else{
            return "블랙리스트";
        }
    }




    // 회원리스트_페이징+검색
    public ModelAndView myQuestList(String memId, int page, int limit, String search, String category) {
        // 페이징 객체 생성
        System.out.println("나의 문의내역");
        PageDTO paging = new PageDTO();
        paging.setSearch(search);
        paging.setCategory(category);
        paging.setStringkeyword1(memId);
        // 한 화면에 보여줄 페이지 번호 갯수
        int block = 5;

        int bCount = mdao.myQuestCount(paging);
        // 전체 게시글 갯수를 DB에서 가져온다.


        // ----------여기부터는 bCount를 사용합니다.---------

        // 최대 게시글 갯수
        int maxPage = (int) (Math.ceil((double) bCount / limit));
        if(maxPage<1){
            maxPage=1;
        }

        // page 검사
        if (page > maxPage) {
            page = maxPage;
        }

        // ----------여기부터는 page를 사용합니다.---------

        // 표시될 게시글 묶음
        int startRow = (page - 1) * limit + 1;
        int endRow = page * limit;
        // 표시될 페이지 묶음
        int startPage = (((int) (Math.ceil((double) page / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        // endPage 검사
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        // ----------여기부터는 startRow, endRow, startPage, endPage를 사용합니다.---------
        paging.setPage(page);
        paging.setStartRow(startRow);
        paging.setEndRow(endRow);
        paging.setMaxPage(maxPage);
        paging.setStartPage(startPage);
        paging.setEndPage(endPage);
        paging.setLimit(limit);

        // 검색
        List<QUESTDTO> QuestList = mdao.myQuestList(paging);
        for(int i=0;i<QuestList.size();i++){
            QUESTDTO questsample = QuestList.get(i);
            int lena=10;
            if(10>questsample.getQUCONTENT().length()){
                lena=questsample.getQUCONTENT().length();
            }
            questsample.setQUCONTENT(questsample.getQUCONTENT().substring(0,lena));
            QuestList.set(i,questsample);
        }
        System.out.println(QuestList);

        // MODEL
        mav.addObject("QuestList", QuestList);
        mav.addObject("paging", paging);
        // VIEW
        mav.setViewName("myQuestList");
        System.out.println("내 문의 내역 검색 끝");
        System.out.println(startRow);
        return mav;
    }

    //mRestroreList : 회원 복구 신청 목록
    @Override
    public ModelAndView mRestroreList(int page, int limit, String search, String category) {
        // 페이징 객체 생성
        PageDTO paging = new PageDTO();
        paging.setSearch(search);
        paging.setCategory(category);
        // 한 화면에 보여줄 페이지 번호 갯수
        int block = 5;

        int bCount = mdao.mRestroreCount(paging);
        // 전체 게시글 갯수를 DB에서 가져온다.


        // ----------여기부터는 bCount를 사용합니다.---------

        // 최대 게시글 갯수
        int maxPage = (int) (Math.ceil((double) bCount / limit));
        if(maxPage<1){
            maxPage=1;
        }
        // page 검사
        if (page > maxPage) {
            page = maxPage;
        }

        // ----------여기부터는 page를 사용합니다.---------

        // 표시될 게시글 묶음
        int startRow = (page - 1) * limit + 1;
        int endRow = page * limit;
        // 표시될 페이지 묶음
        int startPage = (((int) (Math.ceil((double) page / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        // endPage 검사
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        // ----------여기부터는 startRow, endRow, startPage, endPage를 사용합니다.---------
        paging.setPage(page);
        paging.setStartRow(startRow);
        paging.setEndRow(endRow);
        paging.setMaxPage(maxPage);
        paging.setStartPage(startPage);
        paging.setEndPage(endPage);
        paging.setLimit(limit);

        // 검색
        List<RESTOREDTO> RestroreList = mdao.mRestroreList(paging);

        // MODEL
        mav.addObject("RestroreList", RestroreList);
        mav.addObject("paging", paging);
        // VIEW
        mav.setViewName("mRestroreList");
        return mav;
    }


    //관리자에 의한 회원 복구 신청 실행 및 처리 후 결과에 따라 회원 복수 신청 실패 또는 성공 페이지로 이동
    @Override
    public ModelAndView mRestore(String memId, int page, int limit, String search, String category) {
        int result=mdao.mRestore(memId);
        // MODEL
        mav.addObject("memId", memId);
        // (6) 이동
        if (result > 0) {
            mav.setViewName("mRestoreResult");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<String> stringlist1 = new ArrayList<>();
            stringlist1.add("redirect:mRestroreList?");
            stringlist1.add("page="+page);
            stringlist1.add("&limit="+limit);
            stringlist1.add("&search="+search);
            stringlist1.add("&category="+category);
            for (int i = 0; i < stringlist1.size(); i++)
            {
                stringBuilder.append(stringlist1.get(i));
            }
            mav.setViewName(stringBuilder.toString());
        }
        return mav;
    }

    // 비번을 입력받고, 제대로 입력시 회원정보 수정 페이지로 이동하기
    public ModelAndView modiForm(String memId, String memPw) {

        MemberDTO member = new MemberDTO();
        member.setMemId(memId);
        member.setMemPw(memPw);

        String encPw = mdao.mLogin(member).getMemPw();

        // 비번 인증 성공
        if (pwEnc.matches(member.getMemPw(), encPw)) {
            MemberDTO memberinfo = mdao.mview(memId);
            memberinfo.setMemBirth(memberinfo.getMemBirth().substring(0, 10));
            mav.addObject("member", memberinfo);
            mav.setViewName("mModifyForm");
        }
        // 비번 인증 실패
        else{
            mav.setViewName("mModifyPwCheck");
        }
        return mav;
    }


    // 회원정보 수정하기, 성공시 view로, 여기서 비번과 email은 수정 불가능
    public ModelAndView mModify(MemberDTO member,String memBirth2, String memAddrcopy) throws IllegalStateException, IOException {

        // (1)프로필 사진 처리
        // 1. 파일 불러오기
        MultipartFile profile = member.getMemProfile();

        // 2. 파일 선택여부 확인
        if (!profile.isEmpty()) {
            // 3. 파일 저장 위치 설정(상대경로)
            Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/profile");

            //4. 기존에 있던 파일 삭제하기
            String deletePath = path + "/" + member.getMemPROFILENAME();
            File deleteFile = new File(deletePath);

            if (deleteFile.exists()) { // 삭제할 파일이 존재하면

                deleteFile.delete();
            } else {
                System.out.println("삭제할 파일이 존재하지않습니다.");
            }

            // 5. 파일 이름 불러오기
            String originalFileName = profile.getOriginalFilename();

            // 6. 난수 생성하기
            String uuid = UUID.randomUUID().toString().substring(0, 8);

            // 7. 업로드 할 파일이름 생성하기(3번 +언더바(_) + 2번)
            String profileName = uuid + "_" + originalFileName;
            String savePath = path + "/" + profileName;

            // 8. 파일 선택시 MemberDTO member객체의 profileName필드에 업로드파일 이름 저장
            member.setMemPROFILENAME(profileName);

            // 9. 게시글 등록 성공시 파일업로드
            profile.transferTo(new File(savePath));
        }
        String membernull=member.getMemAddr();

        // (2) 주소
        member.setMemAddr("(" + member.getMemAddr1() + ")" + member.getMemAddr2() + ", " + member.getMemAddr3());
        if(member.getMemAddr1().equals("")){
            System.out.println("주소 수정");
            member.setMemAddr(memAddrcopy);
        }


        // (3) 생일
        if(member.getMemBirth().equals("")){
            member.setMemBirth(memBirth2.substring(0, 10));
            System.out.println("생일 수정");
        }
        else{
            System.out.println(member.getMemBirth());
            System.out.println(memBirth2);
        }
        int result = mdao.mModify(member);

        if (result > 0) {
            //내 정보 수정을 성공했다면 수정한 정보를 가져옵니다.
            MemberDTO memberload = mdao.mLogin(member);
            memberload.setMemBirth(memberload.getMemBirth().substring(0, 10));
            //세션을 초기화합니다.
            session.invalidate();

            //세션을 새로 만듭니다.
            session.setAttribute("loginId", memberload.getMemId());
            session.setAttribute("loginm", memberload);

            mav.setViewName("redirect:/mView?memId=" + memberload.getMemId());
        } else {
            mav.setViewName("mView");
        }
        return mav;
    }


    //탈퇴하기
    @Override
    public ModelAndView mOut(String memId, String memPw) {

        MemberDTO member = new MemberDTO();
        member.setMemId(memId);
        member.setMemPw(memPw);

        String encPw = mdao.mLogin(member).getMemPw();

        // 비번 인증 성공
        if (pwEnc.matches(member.getMemPw(), encPw)){
            int result = mdao.mOut(memId);

            if (result > 0) {
                //탈퇴에 성공했습니다.
                //탈퇴성공창으로 이동합니다.
                mav.setViewName("mOutSucess");
                //세션을 초기화합니다.
                session.invalidate();
            } else {
                mav.addObject("false_reason","서버와 연결이 불안정합니다.");
                mav.setViewName("mOutPwCheck");
            }
        }else{
            mav.addObject("false_reason","비번이 올바르지 않습니다.");
            mav.setViewName("mOutPwCheck");
        }
        return mav;
    }


    //ajax로 Update 통해 블랙리스트 회원으로 처리. MEMIS = 2
    @Override
    public String mAddBlackList(String memId) {
        int result = mdao.mAddBlackList(memId);

        if (result > 0) {
            return "OK";
        } else {
            return "NO";
        }
    }

    @Override
    public ModelAndView mQuestList(int page, int limit, String search, String category) {
        // 페이징 객체 생성
        PageDTO paging = new PageDTO();
        paging.setSearch(search);
        paging.setCategory(category);
        // 한 화면에 보여줄 페이지 번호 갯수
        int block = 5;

        int bCount = mdao.mQuestCount(paging);
        // 전체 게시글 갯수를 DB에서 가져온다.


        // ----------여기부터는 bCount를 사용합니다.---------

        // 최대 게시글 갯수
        int maxPage = (int) (Math.ceil((double) bCount / limit));
        if(maxPage<1){
            maxPage=1;
        }
        // page 검사
        if (page > maxPage) {
            page = maxPage;
        }

        // ----------여기부터는 page를 사용합니다.---------

        // 표시될 게시글 묶음
        int startRow = (page - 1) * limit + 1;
        int endRow = page * limit;
        // 표시될 페이지 묶음
        int startPage = (((int) (Math.ceil((double) page / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        // endPage 검사
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        // ----------여기부터는 startRow, endRow, startPage, endPage를 사용합니다.---------
        paging.setPage(page);
        paging.setStartRow(startRow);
        paging.setEndRow(endRow);
        paging.setMaxPage(maxPage);
        paging.setStartPage(startPage);
        paging.setEndPage(endPage);
        paging.setLimit(limit);

        // 검색
        List<QUESTDTO> QuestList = mdao.mQuestList(paging);

        // MODEL
        mav.addObject("QuestList", QuestList);
        mav.addObject("paging", paging);
        // VIEW
        mav.setViewName("mQuestList");
        return mav;
    }

    @Override
    public ModelAndView mQuestView(int QUNUM, int beforepage) {
        System.out.println("QUNUM : "+QUNUM);
        QUESTDTO questview = mdao.mQuestView(QUNUM);
        if(questview!=null){
            // 문의 상세보기
            System.out.println("문의 상세보기");
            mav.addObject("questview", questview);
            mav.setViewName("mQuestView");
        }else{
            if(beforepage==1){
                //내 문의내역으로
                mav.setViewName("redirect:myQuestList");
            }else{
                //전체 문의내역으로
                mav.setViewName("redirect:mQuestList");
            }
        }

        return mav;
    }

    @Override
    public ModelAndView mQuest(QUESTDTO quest) {

        //문의 작성
        int result = mdao.mQuest(quest);


        if (result > 0) {

            //작성 성공시 문의번호를 가져오세요
            int qunum=mdao.mQuestnum();
            mav.setViewName("redirect:mQuestView?QUNUM="+qunum+"&beforepage="+1);
        } else {
            mav.addObject("quest", quest);
            mav.setViewName("mQuestWriteForm");
        }
        return mav;
    }

    @Override
    public ModelAndView mAnswer(QUESTDTO quest) {
        mdao.mAnswer(quest);
        mav.setViewName("redirect:mQuestView?QUNUM="+quest.getQUNUM()+"&beforepage=0");
        return mav;
    }


    //공지 목록
    @Override
    public ModelAndView mNoticeList(int page, int limit, String search, String category) {
        // 페이징 객체 생성
        PageDTO paging = new PageDTO();
        paging.setSearch(search);
        paging.setCategory(category);
        // 한 화면에 보여줄 페이지 번호 갯수
        int block = 5;

        int bCount = mdao.mnotiCount(paging);
        // 전체 게시글 갯수를 DB에서 가져온다.


        // ----------여기부터는 bCount를 사용합니다.---------

        // 최대 게시글 갯수
        int maxPage = (int) (Math.ceil((double) bCount / limit));
        if(maxPage<1){
            maxPage=1;
        }
        // page 검사
        if (page > maxPage) {
            page = maxPage;
        }

        // ----------여기부터는 page를 사용합니다.---------

        // 표시될 게시글 묶음
        int startRow = (page - 1) * limit + 1;
        int endRow = page * limit;
        // 표시될 페이지 묶음
        int startPage = (((int) (Math.ceil((double) page / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        // endPage 검사
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        // ----------여기부터는 startRow, endRow, startPage, endPage를 사용합니다.---------


        paging.setPage(page);
        paging.setStartRow(startRow);
        paging.setEndRow(endRow);
        paging.setMaxPage(maxPage);
        paging.setStartPage(startPage);
        paging.setEndPage(endPage);
        paging.setLimit(limit);

        // 검색
        List<NOTICEDTO> pagingList = mdao.mnoti_pList(paging);

        // MODEL
        mav.addObject("pagingList", pagingList);
        mav.addObject("paging", paging);
        // VIEW
        mav.setViewName("mNoticeList");
        return mav;
    }

    // 공지 작성
    @Override
    public String mNoticeWrite(NOTICEDTO notice) {
        int result = mdao.mNoticeWrite(notice);
        if (result > 0) {
            return "OK";
        } else {
            return "NO";
        }
    }

    //공지 수정
    @Override
    public ModelAndView mNoticeModify(NOTICEDTO notice) {
        int result = mdao.mNoticeModify(notice);
        if (result > 0) {
            mav.setViewName("mNoticeList");
        } else {
            mav.setViewName("mNoticeModify");
        }
        return mav;
    }


    //공지 삭제
    @Override
    public ModelAndView mNoticeDelete(int nonum) {
        int result = mdao.mNoticeDelete(nonum);
        if (result > 0) {
            mav.setViewName("mNoticeList");
        } else {
            mav.setViewName("mNoticeModify");
        }
        return mav;
    }


    //메일 작성
    @Override
    public String mSendAll(String mailtitle, String mailcontent) {
        List<MemberDTO> memberlist = mdao.mlist();
        MimeMessage mail=mailSender.createMimeMessage();
        String result="OK";
        for(int i=0;i<memberlist.size();i++){
            try {
                System.out.println("email : "+memberlist.get(i).getMemEmail());
                mail.setSubject(mailtitle,"UTF-8");
                mail.setText(mailcontent,"UTF-8", "html");
                mail.addRecipient(RecipientType.TO,new InternetAddress(memberlist.get(i).getMemEmail()));
                mailSender.send(mail);
            }catch(MessagingException e) {
                //e.printStackTrace();
                result="NO";
            }
            finally{

            }
        }
        return result;
    }

    @Override
    public ModelAndView myBuyList(String memId, int page, int limit, String search, String category, String stringkeyword1) {


        //category는 보통 구매 종류
        //stringkeyword1가 ""이면 전부, "0"이면 장비만, "1"이면 해양만, "2"이면 수영장만
        if(stringkeyword1.equals("0")){
            stringkeyword1=" AND BUYKIND='0'";
        }else if(stringkeyword1.equals("1")){
            stringkeyword1=" AND BUYKIND='1'";
        }else if(stringkeyword1.equals("2")){
            stringkeyword1=" AND BUYKIND='2'";
        }else{
            stringkeyword1="";
        }
        //stringkeyword1

        //////////////////////////////////구매내역 페이징///////////////////////////////////
        PageDTO paging = new PageDTO();

        paging.setSearch(search);
        paging.setCategory(category);
        paging.setStringkeyword1(stringkeyword1+" AND MEMID='"+memId+"'");
        // 한 화면에 보여줄 페이지 번호 갯수
        int block = 5;

        int bCount = mdao.myBuyListCount(paging);
        // 전체 게시글 갯수를 DB에서 가져온다.


        // ----------여기부터는 bCount를 사용합니다.---------

        // 최대 게시글 갯수
        int maxPage = (int) (Math.ceil((double) bCount / limit));
        if(maxPage<1){
            maxPage=1;
        }

        // page 검사
        if (page > maxPage) {
            page = maxPage;
        }

        // ----------여기부터는 page를 사용합니다.---------

        // 표시될 게시글 묶음
        int startRow = (page - 1) * limit + 1;
        int endRow = page * limit;
        // 표시될 페이지 묶음
        int startPage = (((int) (Math.ceil((double) page / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        // endPage 검사
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        // ----------여기부터는 startRow, endRow, startPage, endPage를 사용합니다.---------
        paging.setPage(page);
        paging.setStartRow(startRow);
        paging.setEndRow(endRow);
        paging.setMaxPage(maxPage);
        paging.setStartPage(startPage);
        paging.setEndPage(endPage);
        paging.setLimit(limit);

        // 검색
        List<BUYDTO> BuyList = mdao.myBuyList(paging);
        System.out.println(BuyList);
        for(BUYDTO index: BuyList){
            System.out.println(index.getBUYDATE());
        }

        // MODEL
        mav.addObject("BuyList", BuyList);
        mav.addObject("paging", paging);
        // VIEW
        mav.setViewName("myBuyList");

        return mav;
    }

    @Override
    public ModelAndView popUpmLogin(MemberDTO member) {

        MemberDTO memberload = mdao.mLogin(member);
        if(memberload !=null) {
            // 로그인 성공
            if (pwEnc.matches(member.getMemPw(), memberload.getMemPw())) {
                if (memberload.getMemIs().equals("1")) {
                    String savePath = "C:/Users/user/Documents/workspace-sts-3.9.18.RELEASE/MEMBOARD/src/main/webapp/resources/profile/";
                    session.setAttribute("loginId", memberload.getMemId());
                    session.setAttribute("loginm", memberload);
                    mav.setViewName("logintest");
                } else if (memberload.getMemIs().equals("0")) {
                    mav.setViewName("mOutLogin");
                } else {
                    mav.setViewName("mOutblackLogin");
                }
            }
            // 로그인실패
            else {
                //나중에 수정
                mav.setViewName("Loginmodel");
                mav.addObject("doing_loginID", member.getMemId());
            }
        }else{//아이디 없음
            mav.setViewName("mLogin");
            mav.addObject("doing_loginID", member.getMemId());
            mav.addObject("reasonforerror", "아이디나 비밀번호가 올바르지 않습니다.");
        }
        // mav객체 안에 Mview.jsp에서 사용할 member의 이름을 "view"라고 선언해서 저장한다.
        return mav;
    }


}
