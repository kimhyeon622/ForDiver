package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("buy")
public class BUYDTO {

    private int BUYNUM;             // 구매내역번호
    private String MEMID;           // 구매자 아이디
    private String BUYPAY;            // 결제금액
    private String BUYOPTION;        // 상품옵션. 인원수 또는 개수
    private String BUYRESDATE;       // 커스텀 예약 일자
    private String BUYDATE;         // 결제일자
    private int BUYCATEGORY;           // 상품 분류
    private String BUYNAME;         // 상품명

}
