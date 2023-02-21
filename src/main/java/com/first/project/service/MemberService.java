package com.first.project.service;


import com.first.project.dto.MemberDTO;
import com.first.project.dto.NOTICEDTO;
import com.first.project.dto.QUESTDTO;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

public interface MemberService {

    // 아이디 중복체크 메소드
    ModelAndView mjoin(MemberDTO member) throws IOException;

    ModelAndView mlist();

    ModelAndView mview(String memId);

    ModelAndView mLogin(MemberDTO member);



    ModelAndView mDelete(String memId);

    String idoverlap(String memId);
    ModelAndView m_plist(int page, int limit, String search, String category);
    ModelAndView m_insertmember();
    String mCheckEmail(String memEmail);

    ModelAndView mPwModifyEmail(String memId);

    ModelAndView mPwFindfinish(String memId,String memPw);

    ModelAndView myQuestList(String memId, int page, int limit, String search, String category);


    ModelAndView mRestroreList(int page, int limit, String search, String category);


    ModelAndView mRestore(String memId, int page, int limit, String search, String category);

    ModelAndView modiForm(String memId, String memPw);

    ModelAndView mModify(MemberDTO member,String memBirth2, String memAddrcopy)throws IllegalStateException, IOException;

    ModelAndView mOut(String memId, String memPw);

    String mAddBlackList(String memId);

    ModelAndView mQuestList(int page, int limit, String search, String category);

    ModelAndView mQuestView(int QUNUM, int beforepage);

    ModelAndView mQuest(QUESTDTO quest);

    ModelAndView mAnswer(QUESTDTO quest);

    ModelAndView mNoticeList(int page, int limit, String search, String category);

    String mNoticeWrite(NOTICEDTO notice);

    ModelAndView mNoticeModify(NOTICEDTO notice);

    ModelAndView mNoticeDelete(int nonum);

    String mSendAll(String mailtitle, String mailcontent);

    String mFindEmail(String memId);

    ModelAndView mPwFindForm(String memId);

    String mPwcheck(String memId, String memPw);


    ModelAndView mNoticeView(int nonum);

    ModelAndView mNoticeModifyForm(int nonum);

    ModelAndView mRestoresend(String memId, String RESREASON);

    String mFindEmailrestore(String memId);

    ModelAndView myBuyList(String memId, int page, int limit, String search, String category, String stringkeyword1);

    ModelAndView popUpmLogin(MemberDTO member);
}
