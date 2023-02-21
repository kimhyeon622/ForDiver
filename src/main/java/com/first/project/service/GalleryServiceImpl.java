package com.first.project.service;

import com.first.project.dao.GalleryDAO;
import com.first.project.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private ModelAndView mav = new ModelAndView();
    private final GalleryDAO gdao;


    @Override
    public int galleryWrite1(GalleryDTO gallery) throws IOException {

        //파일 가져오기
        MultipartFile pPhoto = gallery.getPPhoto();

        //파일 이름 불러오기 = 사진 이름
        String originalFileName = pPhoto.getOriginalFilename();

        //난수(20자 이상 생성) uuid 생성
        String uuid = UUID.randomUUID().toString().substring(0,8);

        //업로드할 파일 이름 생성(uuid + 파일 이름)
        String fileName = uuid + "_" + originalFileName;

        //파일 저장 경로(상대 경로)설정 - 바탕화면, 문서, 어떤 폴더 등 관계없이 찾아냄
        Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/thumbnail");

        //
        String savePath = path + "/" +  fileName;

        //파일 선택 여부 확인하고 업로드 + 썸네일 이름 set
        if(!pPhoto.isEmpty()) {
            pPhoto.transferTo(new File(savePath));
            gallery.setPPhotoName(fileName);
        }

        return gdao.galleryWrite1(gallery);
    }

    @Override
    public int getPnum(GalleryDTO gallery) {

        System.out.println(gallery);

        return gdao.getPnum(gallery);
    }

    @Override
    public void galleryWrite2(GalleryDTO gallery) throws IOException {

        List<MultipartFile> files = gallery.getPcFile();

        for(MultipartFile theFile : files){

            //파일 이름 불러오기 = 사진 이름
            String originalFileName = theFile.getOriginalFilename();

            //난수(20자 이상 생성) uuid 생성
            String uuid = UUID.randomUUID().toString().substring(0,8);

            //업로드할 파일 이름 생성(uuid + 파일 이름)
            String fileName = uuid + "_" + originalFileName;

            //파일 저장 경로(상대 경로)설정 - 바탕화면, 문서, 어떤 폴더 등 관계없이 찾아냄
            Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/photos");

            //
            String savePath = path + "/" +  fileName;

            //파일 선택 여부 확인하고 업로드 + 썸네일 이름 set
            if(!theFile.isEmpty()) {
                theFile.transferTo(new File(savePath));
                gallery.setPcFileName(fileName);

                //확장자 확인하여 pcPhoto에 1(image)또는 0(video)부여
                Boolean checkExtension = fileName.contains("mp4");

                System.out.println(fileName);
                System.out.println(checkExtension);

                if(checkExtension){
                    gallery.setPcPhoto(0);
                    gdao.galleryWrite2(gallery);
                } else {
                    gallery.setPcPhoto(1);
                    gdao.galleryWrite2(gallery);
                }
            }

        }

    }

    @Override
    public GalleryDTO galleryView(int pNum) {
        return gdao.galleryView(pNum);
    }

    @Override
    public List<GalleryDTO> galleryDatas(int pNum) {
        return gdao.galleryDatas(pNum);
    }

    @Override
    public int galleryDelete(int pNum) {
        return gdao.galleryDelete(pNum);
    }

    @Override
    public ModelAndView galleryList(int page, int size, String keyword) {

        //한 화면에 보여줄 페이지 갯수 : 10개
        int block = 10;

        //한 화면에 보여줄 게시글 갯수 : 10개, limit에서 size로 함

        //전체 게시글 갯수 불러오기
        int galleryCount = gdao.galleryCount(keyword);

        System.out.println(galleryCount);

        int startRow = (page-1) * size + 1;
        int endRow = page * size;

        int maxPage = (int)(Math.ceil((double)galleryCount / size)); //flow, round, ceil 이 중 올림에 해당
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

        System.out.println(paging);

        List<GalleryDTO> pagingList = gdao.galleryList(paging);

        mav.addObject("pagingList", pagingList);
        mav.addObject("paging", paging);

        mav.setViewName("GalleryList");

        return mav;
    }

    //보류
    @Override
    public ModelAndView galleryPagingList(int page, int size, String keyword) {
        return null;
    }

    //게시글 좋아요 수 세기 - 위치 때문에 주석 추가
    @Override
    public int galleryLikeCount(int pNum) {
        return gdao.galleryLikeCount(pNum);
    }

    @Override
    public GalleryDTO galleryLikeCheck(GalleryDTO gallery) {
        return gdao.galleryLikeCheck(gallery);
    }

    @Override
    public int galleryLikeInsert(GalleryDTO gallery) {
        return gdao.galleryLikeInsert(gallery);
    }

    @Override
    public int galleryLikeDelete(GalleryDTO gallery) {
        return gdao.galleryLikeDelete(gallery);
    }

    @Override
    public int commentWrite(PCommentDTO comment) {
        return gdao.commentWrite(comment);
    }

    @Override
    public List<PCommentDTO> commentList(int pNum) {
        return gdao.commentList(pNum);
    }

    @Override
    public void childWrite(PCommentDTO comment) {
        gdao.childWrite(comment);
    }

    @Override
    public List<PCommentDTO> childList(int pNum) {
        return gdao.childList(pNum);
    }

    @Override
    public List<PCommentDTO> collectChild(PCommentDTO comment) {
        return gdao.collectChild(comment);
    }

    @Override
    public int commentModify(PCommentDTO comment) {
        return gdao.commentModify(comment);
    }

    @Override
    public int commentDelete(int pcmNum) {
        return gdao.commentDelete(pcmNum);
    }

    @Override
    public List<PCommentDTO> allComment(int pNum) {
        return gdao.allComment(pNum);
    }

    @Override
    public PCommentLikeDTO commentLikeCheck(PCommentLikeDTO commentLike) {
        return gdao.commentLikeCheck(commentLike);
    }

    @Override
    public int commentLikeInsert(PCommentLikeDTO commentLike) {
        return gdao.commentLikeInsert(commentLike);
    }

    @Override
    public int commentLikeDelete(PCommentLikeDTO commentLike) {
        return gdao.commentLikeDelete(commentLike);
    }

    @Override
    public int commentLikeCount(int pcmNum) {
        return gdao.commentLikeCount(pcmNum);
    }

    @Override
    public List<Integer> realAllComment(int pNum) {
        return gdao.realAllComment(pNum);
    }

    @Override
    public void realCommentLikeDelete(Integer integer) {
        gdao.realCommentLikeDelete(integer);
    }

    @Override
    public void commentDeleteForGalleryDelete(int pNum) {
        gdao.commentDeleteForGalleryDelete(pNum);
    }

    @Override
    public void galleryLikeDeleteForGalleryDelete(int pNum) {
        gdao.galleryLikeDeleteForGalleryDelete(pNum);
    }

    @Override
    public List<String> collectContents(int pNum) {
        return gdao.collectContents(pNum);
    }

    @Override
    public void contentNameDelete(String s) {
        gdao.contentNameDelete(s);
    }

    @Override
    public String checkForCommentModifyAndDelete(int num) {
        return gdao.checkForCommentModifyAndDelete(num);
    }


}
