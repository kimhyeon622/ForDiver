package com.first.project.service;

import com.first.project.dto.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

public interface GalleryService {

    int galleryWrite1(GalleryDTO gallery) throws IOException;

    int getPnum(GalleryDTO gallery);

    void galleryWrite2(GalleryDTO gallery) throws IOException;

    GalleryDTO galleryView(int pNum);

    int galleryDelete(int pNum);

    ModelAndView galleryList(int page, int size, String keyword);

    ModelAndView galleryPagingList(int page, int size, String keyword);

    List<GalleryDTO> galleryDatas(int pNum);

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
