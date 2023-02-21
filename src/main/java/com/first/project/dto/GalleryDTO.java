package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Alias("gallery")
public class GalleryDTO {

    // 게시글
    private int pNum; //게시글 번호
    private String memId; //게시글 작성자 아이디
    private LocalDateTime pDate; //게시글 작성일자
    private String pTitle; //게시글 제목
    private MultipartFile pPhoto;//대표 이미지 본 파일
    private String pPhotoName; //대표 이미지(썸네일) 이름, 사용자 미등록시 화면의 사진을 고르게 하여 썸네일로 등록시킬 수 있음

    // 게시글의 사진 또는 영상 파일
    private int pcNum; // 게시글 파일 번호
    private List<MultipartFile> pcFile; //게시글 파일
    private String pcFileName; //게시글 파일 이름
    private int pcPhoto; //사진/영상 구분 위한 데이터. 1이면 사진, 0이면 영상

}
