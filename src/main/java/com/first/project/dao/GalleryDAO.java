package com.first.project.dao;

import com.first.project.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Mapper
public interface GalleryDAO {

    int galleryWrite1(GalleryDTO gallery);

    int getPnum(GalleryDTO gallery);

    int galleryWrite2(GalleryDTO gallery);

    GalleryDTO galleryView(int pNum);

    List<GalleryDTO> galleryDatas(int pNum);

    int galleryDelete(int pNum);

    List<GalleryDTO> galleryList(PageDTO paging);

    int galleryCount(String keyword);

    int galleryLikeCount(int pNum);

    GalleryDTO galleryLikeCheck(GalleryDTO gallery);

    int galleryLikeInsert(GalleryDTO gallery);

    int galleryLikeDelete(GalleryDTO gallery);

    int commentWrite(PCommentDTO comment);

    List<PCommentDTO> commentList(int pNum);

    void childWrite(PCommentDTO comment);

    List<PCommentDTO> childList(int pNum);

    List<PCommentDTO> collectChild(PCommentDTO comment);

    int commentModify(PCommentDTO comment);

    int commentDelete(int pcmNum);

    List<PCommentDTO> allComment(int pNum);

    PCommentLikeDTO commentLikeCheck(PCommentLikeDTO commentLike);

    int commentLikeInsert(PCommentLikeDTO commentLike);

    int commentLikeDelete(PCommentLikeDTO commentLike);

    int commentLikeCount(int pcmNum);

    List<Integer> realAllComment(int pNum);

    void realCommentLikeDelete(Integer integer);

    void commentDeleteForGalleryDelete(int pNum);

    void galleryLikeDeleteForGalleryDelete(int pNum);

    List<String> collectContents(int pNum);

    void contentNameDelete(String s);

    String checkForCommentModifyAndDelete(int num);
}
