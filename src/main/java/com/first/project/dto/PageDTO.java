package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("paging")
public class PageDTO {
    private int page; //현재 페이지
    private int size; //페이지에 보여줄 갯수, limit
    private int startRow;
    private int endRow;
    private int maxPage;
    private int startPage;
    private int endPage;
    private String keyword; //검색어

    //기존의 DTO에 새로운 값 추가
    private int limit; //=size

    private String search="";
    private String category="mName";

    //맘대로 데이터 넣는 공간
    private String stringkeyword1="";
    private String stringkeyword2="";
    private String stringkeyword3="";
    private int intkeyword1=0;
    private int intkeyword2=0;
    private int intkeyword3=0;

}
