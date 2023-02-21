package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("divEq")
public class DivEqDTO {

    private String divEqNum;        // 장비 번호 (기본키)
    private String divEqName;       // 장비 이름
    private int divEqPrice;         // 장비 가격
    private String divEqPriceStr;   // 장비 가격 스트링
    private int divHave;            // 장비 수량
    private String divPhotoName;    // 장비 이미지
    private String divEqSortNum;    // 장비 일련번호


    private int eqCount;            // 선택한 개수
    private int totalPriceInt;
    private String totalPriceStr;      // 장바구니 수량 x 가격 (총가격) 문자열

}
