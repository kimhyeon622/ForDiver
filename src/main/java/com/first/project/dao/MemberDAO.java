package com.first.project.dao;


import com.first.project.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberDAO {



    int mJoin(MemberDTO member);

    MemberDTO mLogin(MemberDTO member);

    List<MemberDTO> mlist();

    MemberDTO mview(String memId);

    int mModify(MemberDTO member);

    int mDelete(String mId);

    String mCheckID(String memId);

    int mCount(PageDTO paging);

    List<MemberDTO> m_pList(PageDTO paging);


    int mModifyPassword(MemberDTO member);

    int myQuestCount(PageDTO paging);

    List<QUESTDTO> myQuestList(PageDTO paging);

    int mRestroreCount(PageDTO paging);

    List<RESTOREDTO> mRestroreList(PageDTO paging);

    int mRestore(String memId);

    int mOut(String memId);

    int mAddBlackList(String memId);

    int mQuestCount(PageDTO paging);

    List<QUESTDTO> mQuestList(PageDTO paging);

    QUESTDTO mQuestView(int qunum);

    int mQuest(QUESTDTO quest);

    int mQuestnum();

    void mAnswer(QUESTDTO quest);

    int mnotiCount(PageDTO paging);

    List<NOTICEDTO> mnoti_pList(PageDTO paging);

    int mNoticeWrite(NOTICEDTO notice);

    int mNoticeModify(NOTICEDTO notice);

    int mNoticeDelete(int nonum);

    String msearchEmail(String memId);

    NOTICEDTO mNoticeView(int NONUM);

    int mRestoresend(RESTOREDTO res);

    int myBuyListCount(PageDTO paging);

    List<BUYDTO> myBuyList(PageDTO paging);
}
