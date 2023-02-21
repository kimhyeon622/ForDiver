package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("divEqList")
public class DivEqListDTO {

    private String divEqNum;        // 장비 번호 (기본키)
    private String divEqName;       // 장비 이름
    private int divEqPrice;         // 장비 가격
    private String divEqPriceStr;   // 장비 가격 스트링
    private int divHave;            // 장비 수량
    private String divPhotoName;    // 장비 이미지
    private String divEqSortNum;    // 장비 일련번호

    private int startRow;	// 페이지 시작 게시글번호
    private int endRow;		// 페이지 마지막 게시글번호

}
